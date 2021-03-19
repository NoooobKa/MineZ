package com.NBK.MineZ.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.Plugin;

import com.NBK.MineZ.util.UTFConfig;

public enum Config {

	BLEADING_EFFECT_PERIOD("Game.Bleading.Effect-Period", 10),
	BLEADING_EFFECT_DAMAGE("Game.Bleading.Effect-Damage", 1),
	BLEEDING_CHANCE("Game.Bleeding.Chance", 0.05),
	DESPAWN_TIME_BUTTON("Game.Despawn-Time-Button", 90),
	DESPAWN_TIME_WEB("Game.Despawn-Time-Web", 300),
	GRANDE_COOLDOWN("Items.Granade.Cooldown", 30),
	INFECTION_EFFECT_PERIOD("Game.Infection.Effect-Period", 10),
	INFECTION_EFFECT_DAMAGE("Game.Infection.Effect-Damage", 1),
	INFECTION_CHANCE("Game.Infection.Chance", 0.05),
	LOGIN_DELAY("Game.Login.Delay", 15),
	THIRST_LEVEL_PERIOD("Game.Thirst.Level-Period", 120),
	THIRST_DAMAGE("Game.Thirst.Damage", 3),
	THIRST_DAMAGE_PERIOD("Game.Thirst.Damage-Period", 3),
	TRAPCHESTP_PROBABILITY("Chests.Trap-Probability", 10),
	RESPAWN_TIME_MELONBLOCK("Game.Respawn-Time-MelonBlock", 3600),
	RESPAWN_TIME_WHEAT("Game.Respawn-Time-Wheat", 3600),
	RESPAWN_TIME_GRAVE("Game.Respawn-Time-Grave", 600),
	RESPAWN_TIME_MUSHROOM("Game.Respawn-Time-Mushroom", 1200),
	SUPPORT_KIT_MAX_ELEMENTS("SupportKit.Max-Elements", 10),
	SPAWNER_NEARBY_PLAYER_RADIUS("Spawner.Nearby-Player-Radius-To-Spawn", 50),
	USE_MySQL("MySQL.Use-MySQL", false),
	MySQL_HOSTNAME("MySQL.Host-Name", "hostname"),
	MySQL_DATABASE("MySQL.Database", "database"),
	MySQL_USERNAME("MySQL.User-Name", "username"),
	MySQL_PASSWORD("MySQL.Password", "password"),
	WEAPON_DURABLITY_MAX("Chests.Weapon-Durablity.Max", 60),
	WEAPON_DURABLITY_MIN("Chests.Weapon-Durablity.Min", 10);
	
    private final String path;
    private final Object def;
    private static UTFConfig configFile;
    private static File rawFile;
    
    private Config(final String path, final Object def) {
        this.path = path;
        this.def = def;
    }
	
    public static void set(final String path, final Object val) {
        if (Config.configFile != null) {
            Config.configFile.set(path, val);
        }
    }
    
    public static void save() {
        if (Config.rawFile != null && Config.configFile != null) {
            try {
                Config.configFile.save(Config.rawFile);
            }
            catch (IOException ex) {}
        }
    }
    
    public String getPath() {
        return this.path;
    }
    
    public Object getDefault() {
        return this.def;
    }
    
    public static void setFile(final UTFConfig config) {
        Config.configFile = config;
    }
    
    public static void setRawFile(final File f) {
        Config.rawFile = f;
    }
    
    public File toFile() {
        return new File(Config.configFile.getString(this.path));
    }
    
    public int toInt() {
        if (!(this.def instanceof Integer)) {
            return 0;
        }
        return Config.configFile.getInt(this.path, (int)this.def);
    }
    
    public boolean toBoolean() {
        return this.def instanceof Boolean && Config.configFile.getBoolean(this.path, (boolean)this.def);
    }
    
    public double toDouble() {
    	if (this.def instanceof Double) {
    		return Config.configFile.getDouble(this.path, (double) this.def);
    	}
    	return 0.0;
    }
    
    @Override
    public String toString() {
    	if (!(this.def instanceof String)) {
    		return null;
    	}
    	return Config.configFile.getString(this.path, (String) this.def);
    }
    
	public static void checkConfig(Plugin p) {
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdirs();
		}
		File file = new File(p.getDataFolder(), "MainConfig.yml");
		boolean exist = true;
		if (!file.exists()) {
			exist = false;
			try {
				file.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
		UTFConfig configFile = new UTFConfig(file);
		if (!exist) {
			for (Config c : Config.values()) {
				configFile.set(c.getPath(), c.getDefault());
			}
		}
		try {
			configFile.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Config.setRawFile(file);
		Config.setFile(configFile);
	}
    
}
