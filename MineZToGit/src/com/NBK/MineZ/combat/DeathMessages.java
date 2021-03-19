package com.NBK.MineZ.combat;

import com.NBK.MineZ.util.*;
import java.io.*;
import java.util.*;
import org.bukkit.plugin.*;

public enum DeathMessages
{
    PLAYER_KILL_PLAYER("DeathsMessage.Player.Player-Kill-Player", Arrays.asList("§e{KILLED} §c§lMURDERED §e{KILLER} §cwith a {ITEM_NAME}")), 
    ZOMBEE_KILL_PLAYER("DeathsMessage.Player.Zombee-Kill-Player", Arrays.asList("§e{KILLED} §cwas slain by a H4x0r zombie. Laaaaaiiimmmzzz.",
    		"§e{KILLED} §cattempted to ally with the zombie.", "§e{KILLED} §cshould have run faster.", "§e{KILLED} §ctried petting a zombie.",
    		"§e{KILLED} §cbecame zombie breakfast, a nice bowl of Rasin Brain",
    		"§e{KILLED} §cgot smacked by a zombie hockey player and its zomboni.",
    		"§e{KILLED} §cthought poking a zombie with a stick was a good idea.",
    		"§e{KILLED} §cneeds to learn not to attract zombies.",
    		"§e{KILLED} §ccould really use a hand... the zombies took theirs!",
    		"§e{KILLED} §cwas eaten alive by a zombie!",
    		"§e{KILLED} §czombie zombie zombie §lYEAH!")), 
    NPC_DEATH("DeathsMessage.NPC", Arrays.asList("{NAME} NPC's was killed"));
    
    private final String path;
    private final List<String> def;
    private static UTFConfig configFile;
    private static File rawFile;
    
    private DeathMessages(final String path, final List<String> def) {
        this.path = path;
        this.def = def;
    }
    
    public static void setFile(final UTFConfig config) {
        DeathMessages.configFile = config;
    }
    
    public static void setRawFile(final File f) {
        DeathMessages.rawFile = f;
    }
    
    public static void save() {
        if (DeathMessages.rawFile != null && DeathMessages.configFile != null) {
            try {
                DeathMessages.configFile.save(DeathMessages.rawFile);
            }
            catch (IOException ex) {}
        }
    }
    
    public String getPath() {
        return this.path;
    }
    
    public String getRandom() {
        final List<String> array = this.getArrayList();
        return array.get(new Random().nextInt(array.size()));
    }
    
    public List<String> getArrayList() {
        return (List<String>)DeathMessages.configFile.getStringList(this.path);
    }
    
    public List<String> getDefault() {
        return this.def;
    }
    
    public static void checkConfig(final Plugin p) {
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdirs();
        }
        final File file = new File(p.getDataFolder(), "DeathMessages.yml");
        boolean exist = true;
        if (!file.exists()) {
            exist = false;
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        final UTFConfig configFile = new UTFConfig(file);
        if (!exist) {
            DeathMessages[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final DeathMessages c = values[i];
                configFile.set(c.getPath(), (Object)c.getDefault());
            }
        }
        try {
            configFile.save(file);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        setRawFile(file);
        setFile(configFile);
    }
}
