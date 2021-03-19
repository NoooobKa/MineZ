package com.NBK.MineZ.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.NBK.MineZ.main.Lang;

public class AreaHelper {

	private ItemStack is;
	private Location firstLoc;
	private Location secondLoc;
	
	public AreaHelper(ItemStack is) {
		this.is = is;
		this.firstLoc = getFirstLoc(is);
		this.secondLoc = getSecondLoc(is);
		updateInfo();
	}

	public ItemStack getItem() {
		return is;
	}
	
	public Location getFirstLoc() {
		return firstLoc;
	}

	public void setFirstLoc(Location firstLoc) {
		this.firstLoc = firstLoc;
		updateInfo();
	}

	public Location getSecondLoc() {
		return secondLoc;
	}

	public void setSecondLoc(Location secondLoc) {
		this.secondLoc = secondLoc;
		updateInfo();
	}
	
	public void updateInfo() {
		List<String> lore = new ArrayList<>();
		lore.add(Lang.TAG_AREA_HELPER.toString());
		lore.add("§2Pos1§f: §7§l(§aWorld§f, §6X, §6Y, §6Z§7§l)");
		lore.add(firstLoc != null ? "§a" + firstLoc.getWorld().getName() + " " + ChatColor.GOLD + (int)firstLoc.getX() + " " + (int)firstLoc.getY() + " " + (int)firstLoc.getZ() : "§4null");
		lore.add(" ");
		lore.add("§2Pos2§f: §7§l(§aWorld§f, §6X, §6Y, §6Z§7§l)");
		lore.add(secondLoc != null ? "§a" + secondLoc.getWorld().getName() + " " + ChatColor.GOLD + (int)secondLoc.getX() + " " + (int)secondLoc.getY() + " " + (int)secondLoc.getZ() : "§4null");
		ItemMeta m = getItem().getItemMeta();
		m.setLore(lore);
		getItem().setItemMeta(m);
	}
	
	private Location getFirstLoc(ItemStack is) {
		if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
			List<String> lore = is.getItemMeta().getLore();
			for (int i = 0; i < lore.size(); i++) {
				if (lore.get(i).contains("§2Pos1§f: §7§l(§aWorld§f, §6X, §6Y, §6Z§7§l)")) {
					if (!lore.get(i + 1).contains("null")) {
						String[] sl = ChatColor.stripColor(lore.get(i + 1)).split("\\s");
						return new Location(Bukkit.getWorld(sl[0]), Integer.valueOf(sl[1]), Integer.valueOf(sl[2]), Integer.valueOf(sl[3]));
					}
				}
			}
		}
		return null;
	}
	
	private Location getSecondLoc(ItemStack is) {
		if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
			List<String> lore = is.getItemMeta().getLore();
			for (int i = 0; i < lore.size(); i++) {
				if (lore.get(i).contains("§2Pos2§f: §7§l(§aWorld§f, §6X, §6Y, §6Z§7§l)")) {
					if (!lore.get(i + 1).contains("null")) {
						String[] sl = ChatColor.stripColor(lore.get(i + 1)).split("\\s");
						return new Location(Bukkit.getWorld(sl[0]), Integer.valueOf(sl[1]), Integer.valueOf(sl[2]), Integer.valueOf(sl[3]));
					}
				}
			}
		}
		return null;
	}
}
