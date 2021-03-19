package com.NBK.MineZ.chest.custom;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.util.RandomCollection;

public class PotCommon extends MineZChest{

	public PotCommon(String name, int secondsToRespawn) {
		super(name, secondsToRespawn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RandomCollection<ItemStack> chestItems() {
		return new RandomCollection<ItemStack>().
				add(80, new ItemStack(Material.POTION, 1, (short)8261)).
				add(15, new ItemStack(Material.POTION, 1, (short)8229)).
				add(4, new ItemStack(Material.POTION, 1, (short)16453)).
				add(1, new ItemStack(Material.POTION, 1, (short)8261));
	}

	@Override
	public RandomCollection<Integer> amountChestItems() {
		return new RandomCollection<Integer>().add(40, 1).add(40, 2).add(10, 3).add(10, 5);
	}

}
