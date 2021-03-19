package com.NBK.MineZ.Items;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;

import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.CustomStack;
import com.NBK.MineZ.util.UTFConfig;

public enum CustomizedStackItems {

	COOKIE(8, 2, 1.0, Material.COOKIE),
	MELON(3, 2, 1.2, Material.MELON),
	WHEAT(3, 0, 0, Material.WHEAT),
	BROWN_MUSHROOM(1, 0, 0, Material.BROWN_MUSHROOM),
	RED_MUSHROOM(1, 0, 0, Material.RED_MUSHROOM),
	INK_SACK(3, 0, 0, Material.INK_SACK),
	APPLE(2, 4, 2.7, Material.APPLE),
	BAKED_POTATO(1, 6, 5, Material.BAKED_POTATO),
	BREAD(3, 6, 3.5, Material.BREAD),
	GOLDEN_APPLE(1, 4, 2.4, Material.GOLDEN_APPLE),
	GOLDEN_CARROT(1, 4, 14.4, Material.GOLDEN_CARROT),
	PUMPKIN_PIE(1, 8, 4.8, Material.PUMPKIN_PIE),
	ROTTEN_FLESH(1, 1, 0.8, Material.ROTTEN_FLESH),
	RAW_FISH(3, 2, 1.0, Material.RAW_FISH),
	BOWL(1, 0, 0, Material.BOWL),
	SNOW_BALL(1, 0, 0, Material.SNOW_BALL),
	ARROW(15, 0, 0, Material.ARROW),
	PAPER(1, 0, 0, Material.PAPER),
	WEB(1, 0, 0, Material.WEB),
	ENDER_PEARL(1, 0, 0, Material.ENDER_PEARL),
	GLASS_BOTTLE(1, 0, 0, Material.GLASS_BOTTLE),
	SULPHUR(1, 0, 0, Material.SULPHUR),
	STONE_BUTTON(1, 0, 0, Material.STONE_BUTTON),
	SUGAR(1, 0, 0, Material.SUGAR);
	
	private int stackSize;
	private int hunger;
	private double saturation;
	private Material m;
    private static UTFConfig file;
    private static File rawFile;
    
	
	private CustomizedStackItems(final int stackSize, final int hunger, final double saturation, final Material m) {
		this.stackSize = stackSize;
		this.m = m;
		this.hunger = hunger;
		this.saturation = saturation;
	}
	
	public int getStackSize() {
		return CustomizedStackItems.file.getInt(this.name() + ".Stack");
	}
	
	public Material getMaterial() {
		return m;
	}
	
	public int getHunger() {
		return CustomizedStackItems.file.getInt(this.name() + ".Hunger");
	}
	
	public double getSaturation() {
		return CustomizedStackItems.file.getDouble(this.name() + ".Saturation");
	}
	
	public CustomStack getCustomStack() {
		CustomStack cs = new CustomStack(this.m);
		if (this.hunger != 0)cs.addStringInLore("§6Hunger§f: §e" + this.hunger);
		if (this.saturation != 0)cs.addStringInLore("§6Saturation§f: §e" + this.saturation);
		return cs.addStringInLore("§3Stack§f: §b" + this.stackSize);
		
	}
	
    public static void set(final String path, final Object val) {
        if (CustomizedStackItems.file != null) {
        	CustomizedStackItems.file.set(path, val);
        }
    }
    
    public static void save() {
        if (CustomizedStackItems.rawFile != null && CustomizedStackItems.file != null) {
            try {
            	CustomizedStackItems.file.save(CustomizedStackItems.rawFile);
            }
            catch (IOException ex) {}
        }
    }
	
    public static void setFile(final UTFConfig config) {
        CustomizedStackItems.file = config;
    }
    
    public static void setRawFile(final File f) {
        CustomizedStackItems.rawFile = f;
    }
    
    public static void checkConfig() {
    	File file = new File(MineZMain.INSTANCE.getDataFolder(), "CustomizedStackItemsConfig.yml");
    	if (!file.exists()) {
    		try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	UTFConfig configFile = new UTFConfig(file);
    	setRawFile(file);
    	setFile(configFile);
    	for (CustomizedStackItems item : CustomizedStackItems.values()) {
    		set(item.name() + ".Hunger", item.hunger);
    		set(item.name() + ".Saturation", item.saturation);
    		set(item.name() + ".Stack", item.stackSize);
    	}
    	save();
    }
    
    public static CustomizedStackItems nullableValueOf(String s) {
    	CustomizedStackItems csi = null;
    	for (CustomizedStackItems i : values()) {
    		if (i.name().equals(s)) {
    			csi = i;
    			break;
    		}
    	}
    	return csi;
    }
    
}
