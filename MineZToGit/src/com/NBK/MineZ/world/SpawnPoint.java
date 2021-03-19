package com.NBK.MineZ.world;

import java.io.File;
import java.io.IOException;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.UTFConfig;
import com.NBK.MineZ.util.Util;

public class SpawnPoint {

	private static final NavigableMap<Integer, Location> spawns;
	private static final UTFConfig config;
	
	static {
		spawns = new TreeMap<>();
		config = SpawnPointConfig.getConfig();
	}
	
	private static NavigableMap<Integer, Location> getMap() {
		return SpawnPoint.spawns;
	}
	
	public static void addSpawn(int n, Location spawn, boolean newSpawn) {
		if (!getMap().containsKey(n)) {
			getMap().put(n, spawn);
		}
		if (newSpawn) {
			registerSpawn(n, spawn);
		}
	}
	
	public static boolean registerSpawn(int n, Location spawn) {
		if (!getConfig().contains("Spawns." + n)) {
			getConfig().set("Spawns." + n, Util.getStringByLocation(spawn));
			saveConfig();
			return true;
		}
		return false;
	}
	
	public static NavigableMap<Integer, Location> getSpawns() {
		return getMap();
	}
	
	public static int getSpawnsAmount() {
		return getMap().size();
	}
	
	public static Location getSpawn(int n) {
		if (exists(n)) {
			return getMap().get(n);
		}
		return null;
	}
	
	public static boolean exists(int n) {
		if (getMap().containsKey(n)) {
			return true;
		}
		return false;
	}
	
	public static Location getRandomSpawnLocation() {
		return (Location) getMap().values().toArray()[new Random().nextInt(getSpawnsAmount())];
	}
	
	public static int getRandomSpawn() {
		return (int) getMap().keySet().toArray()[new Random().nextInt(getSpawnsAmount())];
	}
	
	public static UTFConfig getConfig() {
		return SpawnPoint.config;
	}
	
	public static void saveConfig() {
		SpawnPointConfig.save();
	}
	
	public static void loadSpawns() {
		UTFConfig config = getConfig();
		if (!config.contains("Spawns")) {
			config.createSection("Spawns");
			saveConfig();
			return;
		}
		Set<String> spawns = config.getConfigurationSection("Spawns").getKeys(false);
		if (spawns.size() == 0) {
			Bukkit.getServer().getConsoleSender().sendMessage("§cNeed set spawns!");
		}
		for (String s : spawns) {
			addSpawn(Integer.valueOf(s), Util.getLocationByString(config.getString("Spawns." + s)), false);
		}
	}
	
	public static void registerManager(Plugin p) {
		new SpawnPointManager(p);
	}
	
	private static class SpawnPointManager implements Listener{
		public SpawnPointManager(Plugin p) {
			Bukkit.getPluginManager().registerEvents(this, p);
		}
		
		@EventHandler
		public void onInteract(PlayerInteractEvent e) {
			if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains(Lang.TAG_SPAWN_HELPER.toString())) {
				if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					addSpawn(getSpawnsAmount(), e.getClickedBlock().getLocation().add(0, 1, 0), true);
					e.getPlayer().sendMessage("Spawn: §6" + getSpawnsAmount() + " §fadded at §aX§f: §6" + e.getClickedBlock().getX() + " §aY§f: §6" + e.getClickedBlock().getY() + " §aZ§f: §6" + e.getClickedBlock().getZ());
				}
			}
		}
	}
	
	private static class SpawnPointConfig {
		private static final File file;
		private static final UTFConfig config;
		
		static {
			file = loadFile();
			config = loadConfig();
		}
		
		private static File loadFile() {
			File file = new File(MineZMain.INSTANCE.getDataFolder(), "MineZSpawns.yml");
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
			return new UTFConfig(file);
		}
		
		public static UTFConfig getConfig() {
			return SpawnPointConfig.config;
		}
		
		public static void save() {
			try {
				getConfig().save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
