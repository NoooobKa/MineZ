package com.NBK.MineZ.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.Items.CustomItems;
import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.main.Config;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.mobs.EntityMap;
import com.NBK.MineZ.mobs.MobsConfig;

import net.minecraft.server.v1_8_R3.ItemArmor;

public final class Util {
	
	private static RandomCollection<ItemStack> supportKitElements;
	
	static {
		supportKitElements = new RandomCollection<ItemStack>().add(20, CustomItems.ALOE.getCustomStack().getItemStack()).add(20, CustomItems.ANTIBIOTIC.getCustomStack().getItemStack()).add(20, CustomItems.HEALING.getCustomStack().getItemStack()).add(20, CustomItems.REVITALIZER.getCustomStack().getItemStack()).add(20, CustomItems.STIMULANT.getCustomStack().getItemStack());
	}
	
	public static boolean isChestHelper(ItemStack is) {
		return is != null && is.hasItemMeta() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().contains(Lang.TAG_CHEST_HELPER.toString());
	}
	
	public static boolean isAreaHelper(ItemStack is) {
		return is != null && is.hasItemMeta() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().contains(Lang.TAG_AREA_HELPER.toString());
	}
	
	public static String getItemName(ItemStack item) {
	    net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
	    return nmsStack.getItem().a(nmsStack);
	}
	
	public static boolean isEquipment(ItemStack item) {
		return item != null && item.getItemMeta().getDisplayName() == null && CraftItemStack.asNMSCopy(item).getItem() instanceof ItemArmor;
	}
	
	public static boolean isToolOrCombat(ItemStack item) {
		if (item != null) {
			String m = item.getType().name();
			return item.getItemMeta().getDisplayName() == null && (m.contains("SWORD") || m.contains("BOW") || m.contains("HOE") || m.contains("SPADE") || m.contains("AXE"));
		}
		return false;
	}
	
	public static short getRandomWeaponDurablityProcent(double maxDur) {
		double oneProcent = maxDur/100;
		double random = new Random().nextInt(Config.WEAPON_DURABLITY_MAX.toInt() - Config.WEAPON_DURABLITY_MIN.toInt()) + Config.WEAPON_DURABLITY_MIN.toInt();
		return (short) (maxDur - oneProcent * random);
	}
	
	public static ItemStack getRandomSupportKitElement() {
		return supportKitElements.next();
	}
	
    public static short getDirectionByPlayerLocation(Location l) {
    	final float yaw = l.getYaw();
        double rotation = (yaw - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
    	if (rotation > 225 && rotation < 315) {
    		return 2;
    	}else {
    		if (rotation >= 315 || (rotation < 45)) {
    			return 5;
    		}else {
    			if (rotation >= 45 && rotation <= 135) {
    				return 3;
    			}
    		}
    	}
    	return 4;
    }
	
    public static void damageItem(ItemStack is, int damageDur) {
    	if (is != null) {
    		if (is.getType().getMaxDurability() > is.getDurability() + damageDur) {
    			is.setType(Material.AIR);
    		}
    		is.setDurability((short) (is.getDurability() + damageDur));
    	}
    }
    
    public static Location getLocationByString(String s) {
    	String[] args = s.split(",");
    	return new Location(Bukkit.getWorld(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]));
    }
    
    public static String getStringByLocation(Location l) {
    	return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + "," + l.getZ();
    }
    
    public static String getCheckMarkImage() {
        return ChatColor.GREEN + "✔";
    }
    
    public static String getCrossImage() {
        return ChatColor.DARK_RED + "✘";
    }
    
    public static HashMap<Integer, ItemStack> allSimilar(final ItemStack item, final Inventory inv) {
        final HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        if (item != null) {
            final ItemStack[] inventory = inv.getContents();
            for (int i = 0; i < inventory.length; ++i) {
            	if (inventory[i] == null)continue;
                if (item.isSimilar(inventory[i])) {
                    slots.put(i, inventory[i]);
                }
            }
        }
        return slots;
    }
    
    @SuppressWarnings("deprecation")
	public static HashMap<Integer, ItemStack> allSimilarWithoutMeta(final ItemStack item, final Inventory inv) {
        final HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        if (item != null) {
            final ItemStack[] inventory = inv.getContents();
            for (int i = 0; i < inventory.length; ++i) {
            	if (inventory[i] == null)continue;
                if (item.getTypeId() == inventory[i].getTypeId() && item.getDurability() == inventory[i].getDurability()) {
                    slots.put(i, inventory[i]);
                }
            }
        }
        return slots;
    }
    
    public static HashMap<Integer, ItemStack> allSimilarWithStartIndex(final ItemStack item, final Inventory inv, int startIndex) {
        final HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        if (item != null) {
            final ItemStack[] inventory = inv.getContents();
            if (startIndex <= inventory.length) {
                for (int i = startIndex; i < inventory.length; ++i) {
                	if (inventory[i] == null)continue;
                    if (item.isSimilar(inventory[i])) {
                        slots.put(i, inventory[i]);
                    }
                }
            }
        }
        return slots;
    }
    
    public static HashMap<Integer, ItemStack> allSimilarWithEndIndex(final ItemStack item, final Inventory inv, int endIndex) {
        final HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        if (item != null) {
            final ItemStack[] inventory = inv.getContents();
            if (endIndex <= inventory.length) {
                for (int i = 0; i < endIndex; ++i) {
                	if (inventory[i] == null)continue;
                    if (item.isSimilar(inventory[i])) {
                        slots.put(i, inventory[i]);
                    }
                }
            }
        }
        return slots;
    }
    
    public static int firstEmptyWithStartIndex(Inventory inv, int startIndex) {
        final ItemStack[] inventory = inv.getContents();
        if (startIndex >= 0 && startIndex <= inventory.length) {
            for (int i = startIndex; i < inventory.length; ++i) {
                if (inventory[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static int firstEmptyWithEndIndex(Inventory inv, int endIndex) {
        final ItemStack[] inventory = inv.getContents();
        if (endIndex >= 0 && endIndex <= inventory.length) {
            for (int i = 0; i < endIndex; ++i) {
                if (inventory[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static ItemStack firstSword(Inventory inv) {
    	final ItemStack[] inventory = inv.getContents();
    	for (ItemStack is : inventory) {
    		if (is != null && is.getType().name().contains("SWORD")) {
    			return is;
    		}
    	}
    	return null;
    }
    
    static public void spawnZompieAroundPlayers() {
    	Random r = new Random();
    	new BukkitRunnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					MZPlayer mzp = MZPlayer.getPlayer(p);
					if (!mzp.isLiving() || mzp.getLastArea() == null)continue;
					int Xmin = 10;
					int Xmax = 23;
					int Zmin = 10;
					int Zmax = 23;
					Location l = p.getLocation().clone();
					l.setY(l.getY() - 10);
					l.setX(l.getX() + (r.nextBoolean() ? (r.nextInt(Xmax) + Xmin) : -1 * r.nextInt(Xmax) + Xmin));
					l.setZ(l.getZ() + (r.nextBoolean() ? (r.nextInt(Zmax) + Zmin) : -1 * r.nextInt(Zmax) + Zmin));
					do {
						Material m = l.getBlock().getType();
						Material m2 = l.getBlock().getRelative(0, 1, 0).getType();
						if (m == Material.AIR && m2 == Material.AIR) {
							if (MobsConfig.PIG_ZOMBIE_AXIS_VALUE_TO_SPAWN.toString().equals("X") ? l.getX() < l.getX() : l.getZ() < l.getZ()) {
								if (r.nextDouble() > 0.05) {
									EntityMap.ZOMBIE.spawn(l);
								}else {
									EntityMap.PIG_ZOMBIE.spawn(l);
								}
							}
							break;
						}
						l.setY(l.getY() + 1);
					} while (l.getY() < 250);
				}
			}
		}.runTaskTimer(MineZMain.INSTANCE, 100, 700);
    }
    
    public static Collection<Player> getNearbyPlayers(Location location, int radius){
    	Collection<Player> players = new ArrayList<Player>();
    	for (Player p : Bukkit.getOnlinePlayers()) {
    		if (p.getLocation().distance(location) <= radius) {
    			players.add(p);
    		}
    	}
    	return players;
    }
    
    public static void removeChests(Location point, int radius) {
    	new BukkitRunnable() {
			@Override
			public void run() {
				Collection<Location> toRemove = new ArrayList<>();
				for (MineZChest chest : MineZChest.getChests()) {
					for (Block b : chest.getBlocks()) {
						if (point.distance(b.getLocation()) <= radius) {
							toRemove.add(b.getLocation());
						}
					}
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						for (Location loc : toRemove) {
							Block b = loc.getBlock();
							if (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) {
								Chest chest = (Chest) b.getState();
								chest.getInventory().clear();
								b.setType(Material.AIR);
							}
						}
					}
				}.runTask(MineZMain.INSTANCE);
			}
		}.runTaskAsynchronously(MineZMain.INSTANCE);
    }
    
    public static void broadcastMessage(final String message, final Location location, final int radius, final boolean sendToNoLiving) {
        for (final Player p : getNearbyPlayers(location, radius)) {
            if (sendToNoLiving) {
                p.sendMessage(message);
            }
            else {
                if (!MZPlayer.getPlayer(p).isLiving()) {
                    continue;
                }
                p.sendMessage(message);
            }
        }
    }
    
}
