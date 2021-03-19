package com.NBK.MineZ.world;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.main.Config;
import com.NBK.MineZ.util.RandomCollection;
import com.NBK.MineZ.util.Util;

public class ProtectionListener implements Listener{
	
	private Plugin plugin;
	private RandomCollection<ItemStack> graveRobbingContent;
	
	public ProtectionListener(Plugin p) {
		this.plugin = p;
		this.graveRobbingContent = new RandomCollection<ItemStack>();
		Bukkit.getPluginManager().registerEvents(this, p);
	}
	
	@EventHandler
	public void onItemDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Item && (e.getCause() == DamageCause.ENTITY_EXPLOSION || e.getCause() == DamageCause.BLOCK_EXPLOSION))e.setCancelled(true);
	}
	
	@EventHandler
	public void onHanding(HangingBreakEvent e) {
		if (e.getCause() != RemoveCause.ENTITY) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void bpe(BlockPlaceEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			if (e.getBlockPlaced().getType() == Material.WEB) {
				e.setCancelled(false);
				new BukkitRunnable() {
					@Override
					public void run() {
						e.getBlock().setType(Material.AIR);
					}
				}.runTaskLater(plugin, Config.DESPAWN_TIME_WEB.toInt() * 20);
				return;
			}else {
				if (e.getBlockPlaced().getType() == Material.STONE_BUTTON) {
					e.setCancelled(false);
					new BukkitRunnable() {
						
						@Override
						public void run() {
							e.getBlock().setType(Material.AIR);
							
						}
					}.runTaskLater(plugin, Config.DESPAWN_TIME_BUTTON.toInt() * 20);
					return;
				}else {
					if (e.getBlockPlaced().getType() == Material.CAKE_BLOCK) {
						e.setCancelled(false);
						return;
					}
				}
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
			Player p = e.getPlayer();
			if (p.getItemInHand() != null && p.getItemInHand().getType() == Material.WOOD_HOE) {
				if (p.getItemInHand().getType() == Material.WOOD_HOE) {
					if (e.getBlock().getType() == Material.MELON_BLOCK) {
						e.getBlock().setType(Material.AIR);
						Util.damageItem(p.getItemInHand(), (short) 2);
						p.getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5, 1, 0.5), new ItemStack(Material.MELON));
						new BukkitRunnable() {
							@Override
							public void run() {
								e.getBlock().setType(Material.MELON_BLOCK);
							}
						}.runTaskLater(plugin, Config.RESPAWN_TIME_MELONBLOCK.toInt() * 20);
					}
					if (e.getBlock().getType() == Material.CROPS) {
						e.getBlock().setType(Material.AIR);
						Util.damageItem(p.getItemInHand(), (short) 4);
						p.getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5, 1, 0.5), new ItemStack(Material.WHEAT));
						new BukkitRunnable() {
							@SuppressWarnings("deprecation")
							@Override
							public void run() {
								e.getBlock().setType(Material.WHEAT);
								e.getBlock().setData((byte) 7);
							}
						}.runTaskLater(plugin, Config.RESPAWN_TIME_WHEAT.toInt() * 20);
					}
					if (e.getBlock().getType() == Material.BROWN_MUSHROOM || e.getBlock().getType() == Material.RED_MUSHROOM) {
						new BukkitRunnable() {
							@Override
							public void run() {
								e.getBlock().setType(e.getBlock().getType());
							}
						}.runTaskLater(plugin, Config.RESPAWN_TIME_MUSHROOM.toInt() * 20);
						p.getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5, 1, 0.5), new ItemStack(e.getBlock().getType()));
						e.getBlock().setType(Material.AIR);
					}
					
				}
				if (p.getItemInHand().getType() == Material.IRON_SPADE) {
					if (e.getBlock().getType() == Material.MOSSY_COBBLESTONE) {
						e.getBlock().setType(Material.COBBLESTONE);
						e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation().add(0.5, 1, 0.5), graveRobbingContent.next());
						Util.damageItem(p.getItemInHand(), (short)10);
						new BukkitRunnable() {
							@Override
							public void run() {
								e.getBlock().setType(Material.MOSSY_COBBLESTONE);
							}
						}.runTaskLater(plugin, Config.RESPAWN_TIME_GRAVE.toInt() * 20);
					}
					if (e.getBlock().getType() == Material.WEB) {
						e.getBlock().setType(Material.AIR);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPIE(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) && e.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
				//new Trap().onTrap((Chest) e.getClickedBlock().getState());
			}
			if (e.getAction() == Action.LEFT_CLICK_BLOCK && p.getTargetBlock((HashSet<Byte>)null, 5).getType() == Material.FIRE) {
				e.setCancelled(true);
			}
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Material m = e.getClickedBlock().getType();
				if (m == Material.FURNACE || m == Material.ENDER_CHEST || m == Material.ENCHANTMENT_TABLE || m == Material.ANVIL || m == Material.DROPPER || m == Material.HOPPER || m == Material.DISPENSER) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPIEE(PlayerInteractEntityEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.SURVIVAL)return;
		if (e.getRightClicked().getType() == EntityType.ITEM_FRAME) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void edbee(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof ItemFrame && e.getDamager() instanceof Player && ((Player)e.getDamager()).getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onExplode(BlockExplodeEvent e) {
		e.setCancelled(true);
		e.blockList().clear();
	}
}
