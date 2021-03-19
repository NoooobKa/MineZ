package com.NBK.MineZ.main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.Items.CustomizedStackItems;
import com.NBK.MineZ.Items.ItemListener;
import com.NBK.MineZ.Items.StackSizeListener;
import com.NBK.MineZ.Items.SupportKit;
import com.NBK.MineZ.chest.ChestListener;
import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.chest.custom.CivCommon;
import com.NBK.MineZ.chest.custom.CivTool;
import com.NBK.MineZ.chest.custom.FoodCommon;
import com.NBK.MineZ.chest.custom.FoodUnCommon;
import com.NBK.MineZ.chest.custom.MillCommon;
import com.NBK.MineZ.chest.custom.MillEpic;
import com.NBK.MineZ.chest.custom.MillIUnCommon;
import com.NBK.MineZ.chest.custom.MillRare;
import com.NBK.MineZ.chest.custom.PotCommon;
import com.NBK.MineZ.chest.custom.PotRare;
import com.NBK.MineZ.chest.custom.PotUnCommon;
import com.NBK.MineZ.combat.Logout;
import com.NBK.MineZ.combat.PvpListener;
import com.NBK.MineZ.commands.MineZCmd;
import com.NBK.MineZ.commands.OverrideTimingsCommand;
import com.NBK.MineZ.data.MySQL;
import com.NBK.MineZ.game.GameListener;
import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.mobs.EntityMap;
import com.NBK.MineZ.mobs.MobsConfig;
import com.NBK.MineZ.mobs.Spawner;
import com.NBK.MineZ.world.AreaListener;
import com.NBK.MineZ.world.ProtectionListener;
import com.NBK.MineZ.world.SpawnPoint;
import com.NBK.MineZ.world.SpawnPointGUI;

public class MineZMain extends JavaPlugin{

	public static MineZMain INSTANCE;
	private MySQL mysql;
	public static OverrideTimingsCommand otc;
	
	public void onEnable() {
		INSTANCE = this;
		Config.checkConfig(this);
		Lang.checkConfig(this);
		CustomizedStackItems.checkConfig();
		MobsConfig.checkConfig(this);
		if (Config.USE_MySQL.toBoolean()) {
			MySQLConnect();
		}
		MZPlayer.registerListener(this);
		registerChests();
		new ChestListener(this);
		new MineZCmd(this);
		new ProtectionListener(this);
		new AreaListener(this);
		new StackSizeListener(this);
		new ItemListener(this);
		new GameListener(this);
		new PvpListener(this);
		new Logout(this);
		Spawner.loadAllSpawners();
		Spawner.registerListener(this);
		SupportKit.registerListener(this);
		SpawnPoint.registerManager(this);
		SpawnPoint.loadSpawns();
		SpawnPointGUI.registerListener(this);
		EntityMap.registerAll();
		new BukkitRunnable() {
			@Override
			public void run() {
				MineZChest.runAll();
			}
		}.runTask(this);
		otc = new OverrideTimingsCommand("OverTimings");
		for (World w : Bukkit.getWorlds()) {
			w.setAutoSave(false);
			clearWorld(w);
		}
	}
	
	@Override
	public void onDisable() {
		if (Config.USE_MySQL.toBoolean())mysql.Disconnect();
		unloadWorlds();
	}
	
	public MySQL getMySQL() {
		return mysql;
	}
	
	private void MySQLConnect() {
		mysql = new MySQL(Config.MySQL_HOSTNAME.toString(), Config.MySQL_DATABASE.toString(), Config.MySQL_USERNAME.toString(), Config.MySQL_PASSWORD.toString());
		mysql.Connect();
		mysql.ExecuteCommand("CREATE TABLE IF NOT EXISTS `minez_players` (  `id` int NOT NULL AUTO_INCREMENT,  `UUID` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,  `IsLiving` tinyint NOT NULL,  `IsBleading` tinyint NOT NULL,  `IsInfection` tinyint NOT NULL,  `XP` int NOT NULL, `TotalKills` int NOT NULL, `Kills` int NOT NULL,  `Deaths` int NOT NULL,  `KilledMobs` int NOT NULL,  `TotalKilledMobs` int NOT NULL,  `Cured` int NOT NULL,  `TotalCured` int NOT NULL,  `Achievements` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,  `StartLoot` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
	}
	
	private void registerChests() {
		new CivCommon("CivCommon", 1800);
		new CivTool("CivTool", 1800);
		new MillCommon("MillCommon", 1800);
		new MillIUnCommon("MillUnCommon", 1800);
		new MillRare("MillRare", 3000);
		new MillEpic("MillEpic", 3000);
		new FoodCommon("FoodCommon", 1800);
		new FoodUnCommon("FoodUnCommon", 3000);
		new PotCommon("PotCommon", 1800);
		new PotUnCommon("PotUnCommon", 1800);
		new PotRare("PotRare", 3000);
	}
	
	private void clearWorld(World w) {
		for (Entity ent : w.getEntities()) {
			if (ent instanceof LivingEntity) {
				ent.remove();
			}
		}
	}
	
	private void unloadWorlds() {
		for (World w : Bukkit.getWorlds()) {
			Bukkit.getServer().unloadWorld(w, false);
		}
	}
	
}
