package com.NBK.MineZ.chest.custom;

import java.util.Random;

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

public class MillEpic extends MineZChest{

	public MillEpic(String name, int secondsToRespawn) {
		super(name, secondsToRespawn);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public RandomCollection<ItemStack> chestItems() {
		return new RandomCollection<ItemStack>().
				add(8 , CustomizedStackItems.ARROW.getCustomStack().setAmount(15).getItemStack()).
				add(6, new ItemStack(Material.IRON_HELMET)).
				add(6, new ItemStack(Material.IRON_CHESTPLATE)).
				add(6, new ItemStack(Material.IRON_LEGGINGS)).
				add(6, new ItemStack(Material.IRON_BOOTS)).
				add(6, new CustomStack(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 1).getItemStack()).
				add(5, new CustomStack(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 1).enchant(Enchantment.ARROW_KNOCKBACK, 1).getItemStack()).
				add(3, new CustomStack(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 2).getItemStack()).
				add(2, new CustomStack(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 2).enchant(Enchantment.ARROW_KNOCKBACK, 1).getItemStack()).
				add(7, CustomItems.GRAPPLE.getCustomStack().getItemStack()).
				add(12, new CustomStack(Material.IRON_SWORD).enchant(Enchantment.DAMAGE_UNDEAD, 1).getItemStack()).
				add(12, new CustomStack(Material.IRON_SWORD).enchant(Enchantment.DAMAGE_UNDEAD, 1).enchant(Enchantment.KNOCKBACK, 1).getItemStack()).
				add(5, new CustomStack(Material.IRON_SWORD).enchant(Enchantment.DAMAGE_UNDEAD, 2).getItemStack()).
				add(5, new CustomStack(Material.IRON_SWORD).enchant(Enchantment.DAMAGE_UNDEAD, 2).enchant(Enchantment.KNOCKBACK, 1).getItemStack()).
				add(3, new CustomStack(Material.IRON_SWORD).enchant(Enchantment.DAMAGE_UNDEAD, 3).getItemStack()).
				add(4, CustomItems.GRANADE.getCustomStack().getItemStack()).
				add(3, CustomItems.SUGAR.getCustomStack().getItemStack()).
				add(1, new ItemStack(289));
	}

	@Override
	public RandomCollection<Integer> amountChestItems() {
		return new RandomCollection<Integer>().add(40, 1).add(40, 2).add(20, 3);
	}

	public boolean setItem(Chest chest, ItemStack item) {
		if (Util.isEquipment(item) || Util.isToolOrCombat(item)) {
			item.setDurability(Util.getRandomWeaponDurablityProcent(item.getType().getMaxDurability()));
		}
		if (item.getType() == Material.FISHING_ROD) {
			item.setDurability((short) (new Random().nextDouble() > 0.66 ? item.getType().getMaxDurability() - 8 : item.getType().getMaxDurability() - 16));
		}
		return super.setItem(chest, item);
	}
	
}
