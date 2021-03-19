package com.NBK.MineZ.chest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.UTFConfig;
import com.NBK.MineZ.util.Util;

public class ChestConfig {
	
	private String name;
	private File file;
	private UTFConfig configFile;
	
	public ChestConfig(String name) {
		this.name = name;
		loadConfig(name);
	}
	
	private void loadConfig(String name) {
		File file = new File(DataFolder.dataFolder, name + ".yml");
		UTFConfig configFile;
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			configFile = new UTFConfig(file);
			configFile.set("BlockLocations", new ArrayList<>());
		}else {
			configFile = new UTFConfig(file);
		}
		this.file = file;
		this.configFile = configFile;
	}
	
	public String getName() {
		return name;
	}
	
	public File getFile() {
		return file;
	}
	
	public UTFConfig getConfig() {
		return configFile;
	}
	
	public List<Block> getBlocksFromCfg(){
		List<Block> blocksList = new ArrayList<>();
		for (String s : getConfig().getStringList("BlockLocations")) {
			String[] args = s.split(",");
			World w = Bukkit.getWorld(args[0]);
			int x = Integer.valueOf(args[1]);
			int y = Integer.valueOf(args[2]);
			int z = Integer.valueOf(args[3]);
			byte data = Byte.valueOf(args[4]);
			Location l = new Location(w, x, y, z);
			l.getBlock().setMetadata(EnumChestMetadata.FaceDirection.getKey(), new FixedMetadataValue(MineZMain.INSTANCE, data));
			blocksList.add(l.getBlock());
		}
		return blocksList;
	}
	
	@SuppressWarnings("deprecation")
	public void addBlockLocation(Block b, Player whoAdd) {
		String s = b.getWorld().getName() + "," + b.getX() + "," + b.getY() + "," + b.getZ() + ",";
		if (b.getType() == Material.CHEST) {
			s = s + b.getState().getData().getData();
		}else {
			s = s + ((whoAdd != null) ? Util.getDirectionByPlayerLocation(whoAdd.getLocation()) : 0); 
		}
		List<String> locationsList = getConfig().getStringList("BlockLocations");
		if (!locationsList.contains(s)) {
			locationsList.add(s);
			getConfig().set("BlockLocations", locationsList);
			if (whoAdd != null) {
				whoAdd.sendMessage(Lang.TAG_MineZChest.toString() + ChatColor.LIGHT_PURPLE + getName() + " §7is set at§7 (§aWorld§f, §6X§f, §6Y§f, §6Z§7)§f: " + ChatColor.GREEN + b.getWorld().getName() + " " + ChatColor.GOLD + b.getX() + " " + b.getY() + " " + b.getZ());
			}
			save();
		}else {
			whoAdd.sendMessage("Chest exist");
		}
	}
	
	public void removeBlockLocation(Block b, Player whoAdd) {
		List<String> locationsList = getConfig().getStringList("BlockLocations");
		String s = b.getWorld().getName() + "," + b.getX() + "," + b.getY() + "," + b.getZ() + ",";
		boolean sucs = false;
		for (int i = 0; i < 7; i++) {
			if (locationsList.contains(s + i)) {
				locationsList.remove(s + i);
				if (whoAdd != null) {
					whoAdd.sendMessage(Lang.TAG_MineZChest.toString() + ChatColor.LIGHT_PURPLE + getName() + " §7is removed at§7 (§aWorld§f, §6X§f, §6Y§f, §6Z§7)§f: " + ChatColor.GREEN + b.getWorld().getName() + " " + ChatColor.GOLD + b.getX() + " " + b.getY() + " " + b.getZ());
					sucs = true;
				}
			}
		}
		if (sucs) {
			getConfig().set("BlockLocations", locationsList);
			save();
		}else {
			whoAdd.sendMessage("Chest not founded");
		}
	}
	
	public void save() {
		try {
			getConfig().save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static class DataFolder{
		private static File dataFolder;
		
		static {
			dataFolder = loadDataFolder();
		}
		
		private static File loadDataFolder() {
			File data = new File(MineZMain.INSTANCE.getDataFolder().getPath() + "\\Chests");
			if (!data.exists()) {
				data.mkdirs();
			}
			return data;
		}
		
	}
	
}
