package com.NBK.MineZ.chest.custom;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.util.RandomCollection;

public class PotUnCommon extends MineZChest{

	public PotUnCommon(String name, int secondsToRespawn) {
		super(name, secondsToRespawn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RandomCollection<ItemStack> chestItems() {
		return new RandomCollection<ItemStack>().
				add(45, new ItemStack(Material.POTION, 1, (short)8261)).
				add(42, new ItemStack(Material.POTION, 1, (short)8229)).
				add(7, new ItemStack(Material.POTION, 1, (short)16453)).
				add(3, new ItemStack(Material.POTION, 1, (short)16421)).
				add(3, new ItemStack(Material.GOLDEN_APPLE));
	}

	@Override
	public RandomCollection<Integer> amountChestItems() {
		return new RandomCollection<Integer>().add(40, 1).add(40, 2).add(10, 3).add(10, 4);
	}

}
