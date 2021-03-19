package com.NBK.MineZ.chest.custom;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.NBK.MineZ.Items.CustomItems;
import com.NBK.MineZ.Items.CustomizedStackItems;
import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.util.CustomStack;
import com.NBK.MineZ.util.RandomCollection;
import com.NBK.MineZ.util.Util;

public class MillRare extends MineZChest{

	public MillRare(String name, int secondsToRespawn) {
		super(name, secondsToRespawn);
	}

	@Override
	public RandomCollection<ItemStack> chestItems() {
		return new RandomCollection<ItemStack>().
				add(10, CustomizedStackItems.ARROW.getCustomStack().setAmount(10).getItemStack()).
				add(12, new ItemStack(Material.CHAINMAIL_HELMET)).
				add(12, new ItemStack(Material.CHAINMAIL_CHESTPLATE)).
				add(12, new ItemStack(Material.CHAINMAIL_LEGGINGS)).
				add(12, new ItemStack(Material.CHAINMAIL_BOOTS)).
				add(2, new ItemStack(Material.IRON_HELMET)).
				add(2, new ItemStack(Material.IRON_CHESTPLATE)).
				add(2, new ItemStack(Material.IRON_LEGGINGS)).
				add(2, new ItemStack(Material.IRON_BOOTS)).
				add(8, new ItemStack(Material.BOW)).
				add(4, new CustomStack(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 1).getItemStack()).
				add(4, new CustomStack(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 1).enchant(Enchantment.ARROW_KNOCKBACK, 1).getItemStack()).
				add(10, new ItemStack(Material.IRON_SWORD)).
				add(3, new CustomStack(Material.IRON_SWORD).enchant(Enchantment.DAMAGE_UNDEAD, 1).getItemStack()).
				add(3, CustomItems.SUGAR.getCustomStack().getItemStack()).
				add(2, CustomItems.GRANADE.getCustomStack().getItemStack());
	}

	@Override
	public RandomCollection<Integer> amountChestItems() {
		return new RandomCollection<Integer>().add(50, 1).add(30, 2).add(20, 1);
	}

	public boolean setItem(Chest chest, ItemStack item) {
		if (Util.isEquipment(item) || Util.isToolOrCombat(item)) {
			item.setDurability(Util.getRandomWeaponDurablityProcent(item.getType().getMaxDurability()));
		}
		return super.setItem(chest, item);
	}
	
}
