package com.NBK.MineZ.chest.custom;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import com.NBK.MineZ.Items.CustomItems;
import com.NBK.MineZ.Items.CustomizedStackItems;
import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.util.CustomStack;
import com.NBK.MineZ.util.RandomCollection;
import com.NBK.MineZ.util.Util;

public class CivCommon extends MineZChest{

	public CivCommon(String name, int secondsToRespawn) {
		super(name, secondsToRespawn);
	}

	@Override
	public RandomCollection<ItemStack> chestItems() {
		return new RandomCollection<ItemStack>().
				add(10, CustomizedStackItems.APPLE.getCustomStack().setAmount(1).getItemStack()).
				add(10, CustomizedStackItems.BOWL.getCustomStack().setAmount(1).getItemStack()).
				add(10, CustomItems.SHEARS.getCustomStack().getItemStack()).
				add(25, new CustomStack(Material.INK_SACK).setName("Element").getItemStack()).
				add(8, CustomItems.BANDAGE.getCustomStack().getItemStack()).
				add(14, CustomItems.SNOWBALL.getCustomStack().getItemStack()).
				add(8, CustomItems.ANTIDOTE.getCustomStack().getItemStack()).
				add(15, CustomizedStackItems.GLASS_BOTTLE.getCustomStack().getItemStack());
	}

	@Override
	public RandomCollection<Integer> amountChestItems() {
		return new RandomCollection<Integer>().add(30, 1).add(60, 2).add(10, 3);
	}

	public boolean setItem(Chest chest, ItemStack item) {
		if (item.getType() == Material.INK_SACK && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			item = Util.getRandomSupportKitElement();
		}
		return super.setItem(chest, item);
	}
	
}
