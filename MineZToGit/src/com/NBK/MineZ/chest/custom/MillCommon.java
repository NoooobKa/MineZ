package com.NBK.MineZ.chest.custom;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import com.NBK.MineZ.Items.CustomizedStackItems;
import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.util.RandomCollection;
import com.NBK.MineZ.util.Util;

public class MillCommon extends MineZChest{

	public MillCommon(String name, int secondsToRespawn) {
		super(name, secondsToRespawn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RandomCollection<ItemStack> chestItems() {
		return new RandomCollection<ItemStack>().
				add(10, CustomizedStackItems.ARROW.getCustomStack().setAmount(2).getItemStack()).
				add(16, new ItemStack(Material.WOOD_SWORD)).
				add(15, new ItemStack(Material.LEATHER_HELMET)).
				add(15, new ItemStack(Material.LEATHER_CHESTPLATE)).
				add(15, new ItemStack(Material.LEATHER_LEGGINGS)).
				add(15, new ItemStack(Material.LEATHER_BOOTS)).
				add(5, new ItemStack(Material.BOW)).
				add(6, new ItemStack(Material.STONE_AXE)).
				add(3, new ItemStack(Material.STONE_SWORD));
	}

	@Override
	public RandomCollection<Integer> amountChestItems() {
		return new RandomCollection<Integer>().add(20, 1).add(60, 2).add(20, 3);
	}

	public boolean setItem(Chest chest, ItemStack item) {
		if (Util.isEquipment(item) || Util.isToolOrCombat(item)) {
			item.setDurability(Util.getRandomWeaponDurablityProcent(item.getType().getMaxDurability()));
		}
		return super.setItem(chest, item);
	}
	
}
