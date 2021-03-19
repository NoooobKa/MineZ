package com.NBK.MineZ.mobs;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.Plugin;

import com.NBK.MineZ.util.UTFConfig;

public enum MobsConfig {
	
	MOBS_DEFAULT_SPEED("Default.Speed", 0.387),
	MOBS_AXIS("Default.Axis" , "X"),
	ZOMBIE_START_HEALTH("Zombie.Start-Health", 7.0),
	ZOMBIE_SPEED_MULTIPLY("Zombie.Speed-Multiply", 1.0),
	ZOMBIE_AXIS_VALUE_TIER_TWO("Zombie.Boost.Axis-Tier-Two-Value", 2000),
	ZOMBIE_AXIS_VALUE_TIER_THREE("Zombie.Boost.Axis-Tier-Three-Value", 3000),
	ZOMBIE_BOOST_HEALTH("Zombie.Boost.Boost-Health", 7),
	PIG_ZOMBIE_SPEED_MULTYPLY("Pig-Zombie.Speed-Multyply", 1.0),
	PIG_ZOMBIE_MAX_SPAWN_BABY("Pig-Zombie.Max-Baby-Spawn", 4),
	PIG_ZOMBIE_AXIS_VALUE_TO_SPAWN("Pig-Zombie.Axis-Value-To-Spawn", 2000),
	GIANT_HEALTH("Giant.Health", 200.0),
	GIANT_SPEED_MULTYPLY("Giant.Speed-Multyply", 1.0);
	private final String path;
    private final Object def;
    private static UTFConfig configFile;
    private static File rawFile;
	
	private MobsConfig(final String path, final Object def) {
		this.path = path;
		this.def = def;
	}
	
    public static void set(final String path, final Object val) {
        if (MobsConfig.configFile != null) {
            MobsConfig.configFile.set(path, val);
        }
    }
    
    public static void save() {
        if (MobsConfig.rawFile != null && MobsConfig.configFile != null) {
            try {
                MobsConfig.configFile.save(MobsConfig.rawFile);
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
        MobsConfig.configFile = config;
    }
    
    public static void setRawFile(final File f) {
        MobsConfig.rawFile = f;
    }
    
    public File toFile() {
        return new File(MobsConfig.configFile.getString(this.path));
    }
    
    public int toInt() {
        if (!(this.def instanceof Integer)) {
            return 0;
        }
        return MobsConfig.configFile.getInt(this.path, (int)this.def);
    }
    
    public double toDouble() {
        if (!(this.def instanceof Double)) {
            return 0.0D;
        }
        return MobsConfig.configFile.getDouble(this.path, (double)this.def);
    }
    
    public boolean toBoolean() {
        return this.def instanceof Boolean && MobsConfig.configFile.getBoolean(this.path, (boolean)this.def);
    }
    
    @Override
    public String toString() {
    	if (!(this.def instanceof String)) {
    		return null;
    	}
    	return MobsConfig.configFile.getString(this.path, (String) this.def);
    }
    
	public static void checkConfig(Plugin p) {
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdirs();
		}
		File file = new File(p.getDataFolder(), "MobsConfig.yml");
		boolean exist = true;
		if (!file.exists()) {
			exist = false;
			try {
				file.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
		UTFConfig configFile = new UTFConfig(file);
		if (!exist) {
			for (MobsConfig c : MobsConfig.values()) {
				configFile.set(c.getPath(), c.getDefault());
			}
		}
		try {
			configFile.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MobsConfig.setRawFile(file);
		MobsConfig.setFile(configFile);
	}
	
}
