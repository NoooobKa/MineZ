package com.NBK.MineZ.Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.util.CustomStack;

public class Loadout {

	public static final Loadout LOBBY;
	public static final Loadout SPAWN_DEFAULT;
	private ItemStack[] invenotry;
	private ItemStack[] armor;
	
	static {
		LOBBY = new Loadout().setItemToInvenotyInSlot(new CustomStack(Material.GRASS).setName(Lang.SPAWN_SELECTOR_NAME.toString()).getItemStack(), 4);
		SPAWN_DEFAULT = new Loadout().setSoulboundItemToInvenotyInSlot(new ItemStack(Material.WOOD_SWORD), 0).setSoulboundItemToInvenotyInSlot(CustomItems.BANDAGE.getCustomStack().getItemStack(), 1).setSoulboundItemToInvenotyInSlot(new ItemStack(Material.POTION), 2);
	}
	
	public Loadout() {
		this.invenotry = new ItemStack[36];
		this.armor = new ItemStack[4];
	}
	
	public ItemStack[] getInventory() {
		return invenotry;
	}
	
	public ItemStack[] getArmor() {
		return armor;
	}
	
	public Loadout addItemToInventory(ItemStack item) {
		if (item != null) {
			for (int i = 0; i < invenotry.length; i++) {
				if (invenotry[i] == null) {
					invenotry[i] = item.clone();
				}
			}
		}
		return this;
	}
	
	public Loadout setItemToInvenotyInSlot(ItemStack item, int slot) {
		if (item != null && slot >= 0 && slot <= 35) {
			invenotry[slot] = item.clone();
		}
		return this;
	}
	
	public Loadout setSoulboundItemToInvenotyInSlot(ItemStack item, int slot) {
		if (item != null && slot >= 0 && slot <= 35) {
			invenotry[slot] = new CustomStack(item).addStringInLore(Lang.SOULBOUND.toString()).getItemStack();
		}
		return this;
	}
	
	public Loadout setHelmet(ItemStack helmet) {
		if (helmet != null) {
			armor[3] = helmet.clone();
		}
		return this;
	}
	
	public Loadout setChestplate(ItemStack chestplate) {
		if (chestplate != null) {
			armor[2] = chestplate.clone();
		}
		return this;
	}
	
	public Loadout setLeggins(ItemStack leggins) {
		if (leggins != null) {
			armor[1] = leggins.clone();
		}
		return this;
	}
	
	public Loadout setBoots(ItemStack boots) {
		if (boots != null) {
			armor[0] = boots.clone();
		}
		return this;
	}
	
}
