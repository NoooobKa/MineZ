package com.NBK.MineZ.chest.custom;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.NBK.MineZ.Items.CustomizedStackItems;
import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.util.RandomCollection;

public class FoodUnCommon extends MineZChest{

	public FoodUnCommon(String name, int secondsToRespawn) {
		super(name, secondsToRespawn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RandomCollection<ItemStack> chestItems() {
		return new RandomCollection<ItemStack>().
				add(16.5, CustomizedStackItems.APPLE.getCustomStack().getItemStack()).
				add(1.5, new ItemStack(Material.CAKE)).
				add(30, CustomizedStackItems.MELON.getCustomStack().getItemStack()).
				add(11, CustomizedStackItems.RED_MUSHROOM.getCustomStack().getItemStack()).
				add(11, CustomizedStackItems.BROWN_MUSHROOM.getCustomStack().getItemStack()).
				add(10, CustomizedStackItems.INK_SACK.getCustomStack().setDurablity((short) 3).getItemStack()).
				add(18, CustomizedStackItems.WHEAT.getCustomStack().getItemStack()).
				add(1, CustomizedStackItems.PUMPKIN_PIE.getCustomStack().getItemStack()).
				add(1, CustomizedStackItems.GOLDEN_CARROT.getCustomStack().getItemStack());
	}

	@Override
	public RandomCollection<Integer> amountChestItems() {
		return new RandomCollection<Integer>().add(10, 1).add(60, 2).add(30, 3);
	}

}
