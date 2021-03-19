package com.NBK.MineZ.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.Plugin;

import com.NBK.MineZ.util.UTFConfig;

public enum Lang {
	ANTIDOTE("Items.Antidote-Name", "§9Antidote"),
	BLEADING_START_TEXT("Game.Bleading.Start-Text", "§cbleeding started"),
	BLEADING_DAMAGE_TEXT("Game.Bleading.Damage-Text", "§cbleeding"),
	BLEADING_STOP_TEXT("Game.Bleading.Stop-Text", "§ableeding stopped"),
	CANT_SAPWN_TEXT("Game.Cant-Spawn-Text", "§cU cant spawn now"),
	DISABLED("Lang.Disabled", "Disabled"),
	ENABLED("Lang.Enabled", "Enabled"),
	SOULBOUND("Lang.Soulbound", "Soulbound"),
	UNDROPPABLE("Lang.UnDroppable", "Undroppable"),
	NEXT("Lang.Next", "Next"),
	BACK("Lang.Back", "Back"),
	WILDERNESS("Lang.Wilderness", "Wilderness"),
	GRANADE("Items.Granade.Name", "§9Granade"),
	GRANADE_COOLDOWN("Items.Granade.Cooldown-Text", "Cooldown"),
	GRANADE_TOO_CLOSE("Items.Granade.Too-Close-Text", "Too close"),
	GRAPPLE("Items.Grapple.Name", "§gGrapple"),
	ITS_A_TRAP("Other.Its-A-Trap", "§4ITS A TRAP!!!"),
	SPAWN_TEXT("Game.Spawn-Text", "Spawn: {SPAWN}"),
	SPAWN_SELECTOR_NAME("Items.Spawn-Selector.Name", "Click me to chose spawn"),
	SOUNDS_LIKE_SOMEONE_IS_NEARBY("Game.Other.Sounds-Like-Someone-Is-Nearby", "§cSounds like someone is nearby..."),
	SCOREBOARD_NAME("Scoreboadrd.Name", "§f§lMine§c§lZ §f - §6"),
	SUGAR_COOLDOWN("Items.Sugar.Cooldown", "Cooldown"),
	SUPPORT_KIT_NAME("Support-Kit.Name", "Support Kit"),
	CHECK_PLAYER_HEAL_TIMING_TEXT("Support-Kit.Heal-Timing-Check-Text", "{PLAYER_NAME}: {HEAL_TIME} to next heal"),
	ALOE("Support-Kit.Aloe-Name", "Aloe"),
	HEALING_OINTMENT("Support-Kit.Healing-Ointment-Name", "Healing ointment"),
	ANTIBIOTIC("Support-Kit.Antibiotic-Name", "Antibiotic"),
	STIMULANT("Support-Kit.Stimulant-Name", "Stimulant"),
	REVITALIZER("Support-Kit.Revitalizer-Name", "Revitalizer"),
	BANDAGE("Support-Kit.Bandage-Name", "§9Bandage"),
	BANDAGE_USE("Game.Bandage.Use-Text", "Bandage used"),
	INFECTION_START_TEXT("Game.Infection.Start-Text", "§5Infection started"),
	INFECTION_DAMAGE_TEXT("Game.Infection.Damage-Text", "§5Infection"),
	INFECTION_STOP_TEXT("Game.Infection.Stop-Text", "§aInfection stopped"),
	LOGIN_DENY_TEXT("Game.Login.Deny-Text", "Deny {TIME_TO_NEXT_LOGIN}"),
	//LOGIN_ACCEPT_TEXT("Game.Login.Accept-Text", "Accept"),
	LOGOUT_TIMER_TEXT("Game.Logout.Timer-Text", "To successful logout left {TIME} s"),
	LOGOUT_ALREADY_LOGOUT_TEXT("Game.Logout.Already-Logout-Text", "U already logouting"),
	LOGOUT_PLAYER_NOT_LIVING_TEXT("Game.Logout.Player-Not-Living-Text", "u is not living"),
	LOGOUT_KICK_TEXT("Game.Logout.Kick-Text", "u successful logout"),
	LOGOUT_CANCEL_TEXT("Game.Logout.Cancel-Text", "cancelled"),
	TAG_CHEST_HELPER("Tags.Chests-Helper-Tag", "Chest helper"),
	TAG_AREA_HELPER("Tags.Area-Helper-Tag", "Area helper"),
	TAG_SPAWN_HELPER("Tags.Spawn-Helper-Tag", "Spawn helper"),
	TAG_SPAWNER_HELPER("Tags.Spawner-Helper-Tag", "Spawner helper"),
	TAG_MineZ("Tags.MineZ", "§7§l[§fMine§4Z§7§l]§f "),
	TAG_MineZChest("Tags.MineZChest", "§7§l[§6Chest§7§l]§f "),
	TAG_AREA("Tags.Area", "§7§l[§aArea§7§l]§f "),
	THIRST_RELOAD_TEXT("Game.Thirst.Reload-Text", "mmm Water"),
	THIRST_10LEVEL_TEXT("Game.Thirst.10LEVEL-Text", "?"),
	THIRST_4LEVEL_TEXT("Game.Thirst.4LEVEL-Text", "?"),
	THIRST_0LEVEL_TEXT("Game.Thirst.0LEVEL-Text", "?"),
	THIRST_DAMAGE_TEXT("Game.Thirst.Damage-Text", "?");
	
	private final String path;
    private final String def;
    private static UTFConfig configFile;
    private static File rawFile;
    
    private Lang(final String path, final String def) {
        this.path = path;
        this.def = def;
    }
    
    public static void setFile(final UTFConfig config) {
        Lang.configFile = config;
    }
    
    public static void setRawFile(final File f) {
        Lang.rawFile = f;
    }
    
    public static void save() {
        if (Lang.rawFile != null && Lang.configFile != null) {
            try {
                Lang.configFile.save(Lang.rawFile);
            }
            catch (IOException ex) {}
        }
    }
    
    @Override
    public String toString() {
        return Lang.configFile.getString(this.path, this.def);
    }
    
    public String getPath() {
        return this.path;
    }
	
    public String getDefault() {
        return this.def;
    }
    
	public static void checkConfig(Plugin p) {
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdirs();
		}
		File file = new File(p.getDataFolder(), "LangConfig.yml");
		boolean exist = true;
		if (!file.exists()) {
			exist = false;
			try {
				file.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
		UTFConfig configFile = new UTFConfig(file);
		if (!exist) {
			for (Lang c : Lang.values()) {
				configFile.set(c.getPath(), c.getDefault());
			}
		}
		try {
			configFile.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Lang.setRawFile(file);
		Lang.setFile(configFile);
	}
    
}
