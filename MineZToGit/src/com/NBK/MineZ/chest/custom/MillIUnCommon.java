package com.NBK.MineZ.chest.custom;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import com.NBK.MineZ.Items.CustomItems;
import com.NBK.MineZ.Items.CustomizedStackItems;
import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.util.RandomCollection;
import com.NBK.MineZ.util.Util;

public class MillIUnCommon extends MineZChest{

	public MillIUnCommon(String name, int secondsToRespawn) {
		super(name, secondsToRespawn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RandomCollection<ItemStack> chestItems() {
		return new RandomCollection<ItemStack>().
				add(10, CustomizedStackItems.ARROW.getCustomStack().setAmount(5).getItemStack()).
				add(9, new ItemStack(Material.LEATHER_HELMET)).
				add(9, new ItemStack(Material.LEATHER_CHESTPLATE)).
				add(9, new ItemStack(Material.LEATHER_LEGGINGS)).
				add(9, new ItemStack(Material.LEATHER_BOOTS)).
				add(4, new ItemStack(Material.CHAINMAIL_HELMET)).
				add(4, new ItemStack(Material.CHAINMAIL_CHESTPLATE)).
				add(4, new ItemStack(Material.CHAINMAIL_LEGGINGS)).
				add(4, new ItemStack(Material.CHAINMAIL_BOOTS)).
				add(10, CustomItems.SUGAR.getCustomStack().getItemStack()).
				add(5, new ItemStack(Material.STONE_AXE)).
				add(8.5, new ItemStack(Material.WOOD_SWORD)).
				add(11, new ItemStack(Material.STONE_SWORD)).
				add(3.5, new ItemStack(Material.IRON_SWORD));
	}

	@Override
	public RandomCollection<Integer> amountChestItems() {
		return new RandomCollection<Integer>().add(50, 1).add(30, 2).add(20, 3);
	}

	public boolean setItem(Chest chest, ItemStack item) {
		if (Util.isEquipment(item) || Util.isToolOrCombat(item)) {
			item.setDurability(Util.getRandomWeaponDurablityProcent(item.getType().getMaxDurability()));
		}
		return super.setItem(chest, item);
	}
	
}
