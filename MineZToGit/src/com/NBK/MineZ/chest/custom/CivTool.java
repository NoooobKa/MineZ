package com.NBK.MineZ.chest.custom;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import com.NBK.MineZ.Items.CustomItems;
import com.NBK.MineZ.Items.CustomizedStackItems;
import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.util.RandomCollection;
import com.NBK.MineZ.util.Util;

public class CivTool extends MineZChest{

	public CivTool(String name, int secondsToRespawn) {
		super(name, secondsToRespawn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RandomCollection<ItemStack> chestItems() {
		return new RandomCollection<ItemStack>().
				add(11, CustomizedStackItems.ARROW.getCustomStack().setAmount(2).getItemStack()).
				add(4.5, CustomizedStackItems.BOWL.getCustomStack().getItemStack()).
				add(11, CustomItems.SHEARS.getCustomStack().getItemStack()).
				add(5.5, CustomizedStackItems.WEB.getCustomStack().getItemStack()).
				add(11, CustomizedStackItems.GLASS_BOTTLE.getCustomStack().getItemStack()).
				add(14.5, CustomItems.SHOVEL.getCustomStack().getItemStack()).
				add(26, new ItemStack(Material.STONE_AXE)).
				add(5.5, CustomItems.BUTTON.getCustomStack().getItemStack()).
				add(11, CustomItems.HOE.getCustomStack().getItemStack());
	}

	@Override
	public RandomCollection<Integer> amountChestItems() {
		return new RandomCollection<Integer>().add(40, 1).add(40, 2).add(20, 3);
	}

	public boolean setItem(Chest chest, ItemStack item) {
		if (Util.isEquipment(item) || Util.isToolOrCombat(item)) {
			item.setDurability(Util.getRandomWeaponDurablityProcent(item.getType().getMaxDurability()));
		}
		return super.setItem(chest, item);
	}
	
}
