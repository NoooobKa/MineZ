package com.NBK.MineZ.data;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.UTFConfig;

public class PlayerConfig implements IData{
	
	private File rawFile;
	private UTFConfig playerConfig;
	private boolean isLiving;
	private boolean isBleading;
	private boolean isInfection;
	private int XP;
	private int totalKills;
	private int kills;
	private int deaths;
	private int careerKilledMobs;
	private int currentKilledMobs;
	private int cured;
	private int careerCured;
	private String serializedStartLoot;
	private String serializedAchievements;
	
	public PlayerConfig(UUID id) {
		checkPlayerFile(id);
		this.isLiving = getPlayerConfig().getBoolean("Player.IsLiving");
		this.isBleading = getPlayerConfig().getBoolean("Player.IsBleading");
		this.isInfection = getPlayerConfig().getBoolean("Player.IsInfection");
		this.XP = getPlayerConfig().getInt("Player.XP");
		this.totalKills = getPlayerConfig().getInt("Player.TotalKills");
		this.kills = getPlayerConfig().getInt("Player.Kills");
		this.deaths = getPlayerConfig().getInt("Player.Deaths");
		this.careerKilledMobs = getPlayerConfig().getInt("Player.Career-Killed-Mobs");
		this.currentKilledMobs = getPlayerConfig().getInt("Player.Current-Killed-Mobs");
		this.cured = getPlayerConfig().getInt("Player.Cured");
		this.careerCured = getPlayerConfig().getInt("Player.Career-Cured");
		this.serializedStartLoot = getPlayerConfig().getString("StartLoot");
		this.serializedAchievements = getPlayerConfig().getString("Achievements");
	} 
	
	public File getPlayerFile() {
		return rawFile;
	}
	
	public UTFConfig getPlayerConfig() {
		return playerConfig;
	}
	
	private void checkPlayerFile(UUID id) {
		if (!new File(DataFolder.getDataFolder(), id.toString() + ".yml").exists()) {
			createPlayerFile(id);
		}else {
			loadPlayerFile(id);
		}
	}
	
	private void loadPlayerFile(UUID id) {
		this.rawFile = new File(DataFolder.dataFolder, id.toString() + ".yml");
		this.playerConfig = new UTFConfig(this.rawFile);
	}
	
	private void createPlayerFile(UUID id) {
		File playerFile = new File(DataFolder.getDataFolder(), id.toString() + ".yml");
		try {
			playerFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		UTFConfig playerConfig = new UTFConfig(playerFile);
		playerConfig.set("Player.IsLiving", false);
		playerConfig.set("Player.IsBleading", false);
		playerConfig.set("Player.IsInfection", false);
		playerConfig.set("Player.XP", 0);
		playerConfig.set("Player.TotalKills", 0);
		playerConfig.set("Player.Kills", 0);
		playerConfig.set("Player.Deaths", 0);
		playerConfig.set("Player.Career-Killed-Mobs", 0);
		playerConfig.set("Player.Current-Killed-Mobs", 0);
		playerConfig.set("Player.Cured", 0);
		playerConfig.set("Player.Career-Cured", 0);
		playerConfig.set("StartLoot", "");
		playerConfig.set("Achievements", "");
		this.rawFile = playerFile;
		this.playerConfig = playerConfig;
		save();
	}
	
	public void save() {
		try {
			getPlayerConfig().save(getPlayerFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setAsynchronously(String path, Object o) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				getPlayerConfig().set(path, o);
				save();
			}
		}.runTaskAsynchronously(MineZMain.INSTANCE);
	}
	
	@Override
	public boolean isLiving() {
		return isLiving;
	}

	@Override
	public void setLiving(boolean b) {
		this.isLiving = b;
		setAsynchronously("Player.IsLiving", b);
	}

	@Override
	public boolean isBleading() {
		return isBleading;
	}

	@Override
	public void setBleading(boolean b) {
		this.isBleading = b;
		setAsynchronously("Player.IsBleading", b);
	}

	@Override
	public boolean isInfection() {
		return isInfection;
	}

	@Override
	public void setInfection(boolean b) {
		this.isInfection = b;
		setAsynchronously("Player.IsInfection", b);
	}

	@Override
	public int getXP() {
		return XP;
	}

	@Override
	public void setXP(int x) {
		this.XP = x;
		setAsynchronously("Player.XP", x);
	}

	@Override
	public int getTotalKills() {
		return totalKills;
	}

	@Override
	public void setTotalKills(int x) {
		this.totalKills = x;
		setAsynchronously("Player.TotalKills", x);
	}
	
	@Override
	public int getKills() {
		return kills;
	}

	@Override
	public void setKills(int x) {
		this.kills = x;
		setAsynchronously("Player.Kills", x);
	}

	@Override
	public int getDeaths() {
		return deaths;
	}

	@Override
	public void setDeaths(int x) {
		this.deaths = x;
		setAsynchronously("Player.Deaths", x);
	}

	@Override
	public int getCareerKilledMobs() {
		return careerKilledMobs;
	}

	@Override
	public void setCareerKilledMobs(int x) {
		this.careerKilledMobs = x;
		setAsynchronously("Player.Career-Killed-Mobs", x);
	}

	@Override
	public int getCurrentKilledMobs() {
		return currentKilledMobs;
	}

	@Override
	public void setCurrentKilledMobs(int x) {
		this.currentKilledMobs = x;
		setAsynchronously("Player.Current-Killed-Mobs", x);
	}

	@Override
	public int getCured() {
		return cured;
	}

	@Override
	public void setCured(int x) {
		this.cured = x;
		setAsynchronously("Player.Cured", x);
	}
	
	@Override
	public int getCareerCured() {
		return careerCured;
	}

	@Override
	public void setCarrerCured(int x) {
		this.careerCured = x;
		setAsynchronously("Player.Career-Cured", x);
	}

	@Override
	public String getSerializedStartLoot() {
		return serializedStartLoot;
	}

	@Override
	public void setSerializedStartLoot(String loot) {
		this.serializedStartLoot = loot;
		setAsynchronously("StartLoot", loot);
	}

	@Override
	public String getSerializedAchievements() {
		return serializedAchievements;
	}

	@Override
	public void setSerializedAchievements(String a) {
		this.serializedAchievements = a;
		setAsynchronously("Achievements", a);
	}
	
	private static class DataFolder{
		private final static File dataFolder;
		
		static {
			dataFolder = loadDataFolder();
		}
		
		private static File loadDataFolder() {
			File data = new File(MineZMain.INSTANCE.getDataFolder().getPath() + "\\Players");
			if (!data.exists()) {
				data.mkdirs();
			}
			return data;
		}
		
		public static File getDataFolder() {
			return dataFolder;
		}
		
	}
	
}
