package com.NBK.MineZ.chest.custom;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.NBK.MineZ.chest.MineZChest;
import com.NBK.MineZ.util.RandomCollection;

public class PotRare extends MineZChest{

	public PotRare(String name, int secondsToRespawn) {
		super(name, secondsToRespawn);
	}

	@Override
	public RandomCollection<ItemStack> chestItems() {
		return new RandomCollection<ItemStack>().
				add(50, new ItemStack(Material.POTION, 1, (short)8229)).
				add(15, new ItemStack(Material.POTION, 1, (short)16453)).
				add(15, new ItemStack(Material.POTION, 1, (short)16421)).
				add(10, getIIIHealPot(false)).
				add(5, getIIIHealPot(true)).
				add(5, new ItemStack(Material.GOLDEN_APPLE));
	}

	@Override
	public RandomCollection<Integer> amountChestItems() {
		return new RandomCollection<Integer>().add(15, 1).add(45, 2).add(23, 3).add(17, 4);
	}
	
	private ItemStack getIIIHealPot(boolean splash) {
		ItemStack SPoH1_3 = new ItemStack(Material.POTION, 1, (short) (splash ? 8261 : 16453));
		PotionMeta meta = (PotionMeta) SPoH1_3.getItemMeta();
		meta.setDisplayName("§6Splash Potion of Healing III");
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		ArrayList<String> list = new ArrayList<String>();
		list.add("§6§lInstant Health III");
		meta.setLore(list);
		meta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 2), true);
		SPoH1_3.setItemMeta(meta);
		return SPoH1_3;
	}
}
