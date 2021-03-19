package com.NBK.MineZ.world;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.UTFConfig;
import com.NBK.MineZ.util.Util;

public final class AreasManager {

	private static AreasManager manager;
	private final NavigableMap<String, Area> areas;
	private final File file;
	private final UTFConfig configFile;
	
	private AreasManager() {
		this.areas = new TreeMap<>();
		this.file = checkFile();
		this.configFile = new UTFConfig(file);
		loadAreas();
	}
	
	private File checkFile() {
		File file = new File(MineZMain.INSTANCE.getDataFolder(), "AreasConfig.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	private void loadAreas() {
		if (!getConfig().contains("Areas")) {
			getConfig().createSection("Areas");
			return;
		}
		for (String s : getConfig().getConfigurationSection("Areas").getKeys(false)) {
			String path = "Areas." + s;
			Location l1 = Util.getLocationByString(getConfig().getString(path + ".FirstLocation"));
			Location l2 = Util.getLocationByString(getConfig().getString(path + ".SecondLocation"));
			Area a = new Area(s, l1, l2);
			a.setCanMobsSpawn(getConfig().getBoolean(path + ".CanMobsSpawn"));
			a.setPvp(getConfig().getBoolean(path + ".Pvp"));
			a.setVisible(getConfig().getBoolean(path + ".Visible"));
			if (!areas.containsKey(a.getName())) {
				areas.put(a.getName(), a);
			}else {
				Bukkit.getLogger().info(Lang.TAG_AREA + "§4Mistake§f. + §cArea §a" + a.getName() + " §calredy exists");
			}
		}
	}
	
	private void updateCfg(Area a) {
		String path = "Areas." + a.getName();
		if (!getConfig().contains(path)) {
			getConfig().createSection(path);
		}
		getConfig().set(path + ".FirstLocation", Util.getStringByLocation(a.getFirstPoint()));
		getConfig().set(path + ".SecondLocation", Util.getStringByLocation(a.getSecondPoint()));
		getConfig().set(path + ".CanMobsSpawn", a.isCanMobsSpawn());
		getConfig().set(path + ".Pvp", a.isPvp());
		getConfig().set(path + ".Visible", a.isVisible());
		save();
	}
	
	private void save() {
		try {
			getConfig().save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private UTFConfig getConfig() {
		return configFile;
	}
	
	public static AreasManager getManager() {
		if (manager == null) {
			manager = new AreasManager();
		}
		return manager;
	}
	
	public boolean addArea(Area a, boolean debug) {
		if (!areas.containsKey(a.getName())) {
			areas.put(a.getName(), a);
			updateCfg(a);
			Bukkit.getConsoleSender().sendMessage(Lang.TAG_AREA.toString() + ChatColor.LIGHT_PURPLE + a.getName() + " §fWas add");
			return true;
		}
		Bukkit.getConsoleSender().sendMessage(Lang.TAG_AREA.toString() + ChatColor.LIGHT_PURPLE + a.getName() + " §fCreation error, possibly such a name already exists");
		return false;
	}
	
	public boolean addArea(Area a, Player p) {
		if (!areas.containsKey(a.getName())) {
			areas.put(a.getName(), a);
			updateCfg(a);
			p.sendMessage(Lang.TAG_AREA.toString() + ChatColor.LIGHT_PURPLE + a.getName() + " §fWas add");
			return true;
		}
		p.sendMessage(Lang.TAG_AREA.toString() + ChatColor.LIGHT_PURPLE + a.getName() + " §fCreation error, possibly such a name already exists");
		return false;
	}
	
	public boolean removeArea(Area a, Player whoRemove) {
		Bukkit.broadcastMessage("1 " + a.getName() + " " + getConfig().contains(a.getName()));
		if (getConfig().contains("Areas." + a.getName())) {
			getConfig().set("Areas." + a.getName(), null);
			save();
			if (whoRemove != null) {
				whoRemove.sendMessage(Lang.TAG_AREA + "§7Area §a" + a.getName() + " §7was removed§f.");
			}
			areas.remove(a.getName());
			return true;
		}
		return false;
	}
	
	public Collection<Area> getAreas() {
		return areas.values();
	}
	
	public Map<String, Area> getAreasMap() {
		return areas;
	}
	
	public boolean containsName(String name) {
		return areas.keySet().contains(name);
	}
	
	public Area getAreaByName(String name) {
		return containsName(name) ? areas.get(name) : null;
	}
	public Collection<Area> getAreasByLocation(Location l) {
		Collection<Area> c = new ArrayList<Area>();
		for (Area a : getAreas()) {
			if (a.enterInToLoc(l)) {
				c.add(a);
			}
		}
		return c;
	}
	
}
