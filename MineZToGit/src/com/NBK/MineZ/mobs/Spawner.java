package com.NBK.MineZ.mobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.main.Config;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.CustomStack;
import com.NBK.MineZ.util.UTFConfig;
import com.NBK.MineZ.util.Util;

public class Spawner {

	private static final UTFConfig config;
	private static final Map<Integer, Spawner> spawners;
	private EntityMap type;
	private int time;
	private Location location;
	private Inventory toolInv;
	private int id;
	
	static {
		config = SpawnerConfig.getConfig();
		spawners = new HashMap<>();
	}
	
	public Spawner(EntityMap type, int time, Location location, boolean addToConfig) {
		this.type = type;
		this.time = time;
		this.location = location;
		this.toolInv = createToolInv();
		this.id = spawners.size();
		spawn();
		if (addToConfig) {
			addToConfig();
		}
		spawners.put(id, this);
	}
	
	public static Spawner getSpawner (int i) {
		if (Spawner.spawners.containsKey(i)) {
			return spawners.get(i);
		}
		return null;
	}
	
	public int getId() {
		return id;
	}
	
	public EntityMap getType() {
		return type;
	}
	
	public int getTime() {
		return time;
	}
	
	public Location getLocation() {
		return location;
	}
	
	private Inventory createToolInv() {
		Inventory inv = Bukkit.createInventory(null, 9, "Spawner tool " + id);
		inv.setItem(0, new CustomStack(Material.ENDER_PEARL).setName("Click to telepot to spawner").getItemStack());
		inv.setItem(8, new CustomStack(Material.BARRIER).setName("Click to remove spawner").getItemStack());
		return inv;
	}
	
	public Inventory getToolInv() {
		return toolInv;
	}
	
	public void forceSpawn() {
		type.spawn(location);
	}
	
	private void spawn() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (MZPlayer p : MZPlayer.getPlayers()) {
					if (p.isLiving() && p.getPlayer().getLocation().distance(location) < Config.SPAWNER_NEARBY_PLAYER_RADIUS.toInt()) {
						forceSpawn();
					}
				}
			}
		}.runTaskTimer(MineZMain.INSTANCE, 0, 20 * time);
	}
	
	private void addToConfig() {
		String path = "Spawners." + config.getConfigurationSection("Spawners").getKeys(false).size();
		config.set(path + ".Location", Util.getStringByLocation(location));
		config.set(path + ".Type", type.name());
		config.set(path + ".Time", time);
		config.set(path + ".ID", id);
		SpawnerConfig.saveConfig();
	}
	
	public void remove() {
		for (String key : config.getConfigurationSection("Spawners").getKeys(false)) {
			if (Integer.valueOf(config.getInt("Spawners." + key + ".ID")) == id) {
				config.set("Spawners." + key, null);
				spawners.remove(id);
			}
		}
	}
	
	public static UTFConfig getConfig() {
		return Spawner.config;
	}
	
	public static SpawnerGUI getGUI() {
		return new SpawnerGUI(); 
	}
	
	public static void loadAllSpawners() {
		for (String key : config.getConfigurationSection("Spawners").getKeys(false)) {
			String path = "Spawners." + key + ".";
			new Spawner(EntityMap.valueOf(config.getString(path + "Type")), config.getInt(path + "Time"), Util.getLocationByString(config.getString(path + "Location")), false);
		}
	}
	
	public static void registerListener(Plugin p) {
		SpawnerGUI.registerListener(p);
	}
	
	private static class SpawnerConfig {
		
		private static final File file;
		private static final UTFConfig configFile;
		
		static {
			file = loadFile();
			configFile = loadConfig();
		}
		
		private static File loadFile() {
			File file = new File(MineZMain.INSTANCE.getDataFolder(), "MineZSpawners.yml");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return file;
		}
		
		private static UTFConfig loadConfig() {
			UTFConfig config = new UTFConfig(file);
			if (!config.contains("Spawners")) {
				config.createSection("Spawners");
				try {
					config.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return config;
		}
		
		public static UTFConfig getConfig() {
			return SpawnerConfig.configFile;
		}
		
		public static void saveConfig() {
			try {
				getConfig().save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static class SpawnerGUI {
		
		private Inventory mainInv;
		private List<Inventory> invsList;
		
		private SpawnerGUI() {
			this.mainInv = Bukkit.createInventory(null, 9, "§aSpawner manager");
			this.invsList = loadSpawnerGUI();
			mainInv.setItem(0, new CustomStack(Material.STICK).setName("§bSelect spawner marker").getItemStack());
			mainInv.setItem(1, new CustomStack(Material.MOB_SPAWNER).setName("§bClick me to customize spawners").getItemStack());
			mainInv.setItem(8, new CustomStack(Material.COMPASS).setName("§a" + Lang.BACK.toString()).addStringInLore("Will take you back to MainGUI").getItemStack());
		}
		
		public Inventory getMainGUI() {
			return mainInv;
		}
		
		public List<Inventory> getInvs(){
			return invsList;
		}
		
		private List<Inventory> loadSpawnerGUI() {
			List<Inventory> invs = new ArrayList<>();
			int size = getConfig().getConfigurationSection("Spawners").getKeys(false).size();
			int spawnerIndex = 0;
			int invsSize = (int) Math.ceil(size/35.0);
			for (int i = 0; i < invsSize; i++) {
				Inventory inv = Bukkit.createInventory(null, 45, "§2Spawners " + (i + 1) + "§7/§2" + invsSize);
				if (i != 0)inv.setItem(18, new CustomStack(Material.ARROW).setName("§a" + Lang.BACK.toString()).getItemStack());
				if (i != invsSize-1)inv.setItem(26, new CustomStack(Material.ARROW).setName("§a" + Lang.NEXT.toString()).getItemStack());
				for (int j = 0; j < inv.getSize(); j++) {
					if (spawnerIndex == size)break;
					if (j == 0 || j%9 == 0 || (j + 1)%9 == 0)continue;
					Spawner s = spawners.get(spawnerIndex);
					inv.setItem(j, new CustomStack(Material.MOB_SPAWNER).setName(String.valueOf(s.getId())).setDurablity((short) (s.getType() == EntityMap.ZOMBIE ? 54 : s.getType() == EntityMap.GIANT_ZOMBIE ? 53 : 57)).
							addStringInLore("§6Type§f: " + s.getType().name()).
							addStringInLore("§6Time§f: " + s.getTime()).
							addStringInLore("§6Location§f: " + (s.getLocation().getX() + " " + s.getLocation().getY() + " " + s.getLocation().getZ())).getItemStack());
					spawnerIndex++;
				}
				invs.add(inv);
			}
			return invs;
		}
		
		public Inventory next(Inventory inv) {
			if (invsList.contains(inv)) {
				for (int i = 0; i < invsList.size(); i++) {
					if (invsList.get(i).getTitle().equals(inv.getTitle())) {
						return invsList.get(i + 1);
					}
				}
			}
			return null;
		}
		
		public Inventory back(Inventory inv) {
			if (invsList.contains(inv)) {
				for (int i = 0; i < invsList.size(); i++) {
					if (invsList.get(i).getTitle().equals(inv.getTitle())) {
						return invsList.get(i - 1);
					}
				}
			}
			return null;
		}
		
		public static void registerListener(Plugin p) {
			new GuiListener(p);
		}
		
		private static class GuiListener implements Listener{
			
			public GuiListener(Plugin p) {
				Bukkit.getPluginManager().registerEvents(this, p);
			}
			
			@EventHandler
			public void onInventory(InventoryClickEvent e) {
				if (e.getInventory() != null && e.getInventory().getTitle() != null) {
					if (e.getInventory().getTitle().contains("Spawner manager")) {
						e.setCancelled(true);
						SpawnerGUI gui = new SpawnerGUI();
						switch (e.getSlot()) {
						case 0:
							Inventory inv = Bukkit.createInventory(null, (int) (Math.ceil(EntityMap.values().length / 9.0) * 9), "Spawner selector");
							for (int i = 0; i < EntityMap.values().length; i++) {
								inv.setItem(i, new CustomStack(Material.MOB_SPAWNER).setName(EntityMap.values()[i].name()).getItemStack());
							}
							e.getWhoClicked().openInventory(inv);
							break;
						case 1:
							if (gui.getInvs().size() > 0)e.getWhoClicked().openInventory(gui.getInvs().get(0));
							break;
						default:
							break;
						}
					}
					
					if (e.getInventory().getTitle().contains("Spawner selector")) {
						e.setCancelled(true);
						if (e.getSlot() > -1 && e.getSlot() < EntityMap.values().length) {
							((Player)e.getWhoClicked()).setItemInHand(new CustomStack(Material.STICK).setName(EntityMap.values()[e.getSlot()].name()).addStringInLore(Lang.TAG_SPAWNER_HELPER.toString()).getItemStack());
							e.getWhoClicked().closeInventory();
						}
					}
					
					if (e.getInventory().getTitle().contains("Spawners")) {
						e.setCancelled(true);
						if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
							ItemStack is = e.getCurrentItem();
							if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
								String name = is.getItemMeta().getDisplayName();
								if (is.getType() == Material.MOB_SPAWNER) {
									e.getWhoClicked().openInventory(Spawner.spawners.get(Integer.valueOf(name)).getToolInv());
								}
							}
						}
					}
					
					if (e.getInventory().getTitle().contains("Spawner tool")) {
						e.setCancelled(true);
						Spawner s = Spawner.getSpawner(Integer.valueOf(e.getInventory().getTitle().split("\\s")[2]));
						switch (e.getSlot()) {
						case 0:
							e.getWhoClicked().teleport(s.getLocation());
							break;
						case 8:
							s.remove();
							break;
						default:
							break;
						}
					}
				}
			}
			
			@EventHandler
			public void onInteract(PlayerInteractEvent e) {
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains(Lang.TAG_SPAWNER_HELPER.toString())) {
						EntityMap em = EntityMap.valueOf(e.getItem().getItemMeta().getDisplayName());
						new Spawner(em, 1100, e.getClickedBlock().getLocation(), true);
					}
				}
			}
			
		}
	}
	
}
