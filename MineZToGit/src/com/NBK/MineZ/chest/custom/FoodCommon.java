package com.NBK.MineZ.chest.custom;

import org.bukkit.inventory.ItemStack;

import com.NBK.MineZ.Items.CustomizedStackItems;
import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.util.RandomCollection;

public class FoodCommon extends MineZChest{

	public FoodCommon(String name, int secondsToRespawn) {
		super(name, secondsToRespawn);
	}

	@Override
	public RandomCollection<ItemStack> chestItems() {
		return new RandomCollection<ItemStack>().
				add(20, CustomizedStackItems.APPLE.getCustomStack().getItemStack()).
				add(30, CustomizedStackItems.RAW_FISH.getCustomStack().getItemStack()).
				add(25, CustomizedStackItems.MELON.getCustomStack().getItemStack()).
				add(5, CustomizedStackItems.RED_MUSHROOM.getCustomStack().getItemStack()).
				add(5, CustomizedStackItems.BROWN_MUSHROOM.getCustomStack().getItemStack()).
				add(8, CustomizedStackItems.INK_SACK.getCustomStack().setDurablity((short) 3).getItemStack()).
				add(7, CustomizedStackItems.WHEAT.getCustomStack().getItemStack());
	}

	@Override
	public RandomCollection<Integer> amountChestItems() {
		return new RandomCollection<Integer>().add(50, 1).add(30, 2).add(20, 3);
	}

}
