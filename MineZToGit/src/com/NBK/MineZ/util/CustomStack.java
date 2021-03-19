package com.NBK.MineZ.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.NBK.MineZ.main.Lang;

public class CustomStack {

	private ItemStack is;
	
	public CustomStack(ItemStack is) {
		this.is = is;
	}
	
	public CustomStack(Material m) {
		this.is = new ItemStack(m);
	}
	
	public ItemStack getItemStack() {
		return is;
	}
	
	public CustomStack setType(Material m) {
		getItemStack().setType(m);
		return this;
	}
	
	public CustomStack setName(String name) {
		ItemMeta m = getMeta();
		m.setDisplayName(name);
		setMeta(m);
		return this;
		
	}
	
	public CustomStack setLore(List<String> lore) {
		ItemMeta m = getMeta();
		m.setLore(lore);
		getItemStack().setItemMeta(m);
		return this;
	}
	
	public CustomStack addStringInLoreAbove(String string) {
		List<String> lore = getLore();
		lore.add(0, string);
		setLore(lore);
		return this;
	}
	
	public CustomStack addStringInLore(String string) {
		List<String> lore = getLore();
		lore.add(string);
		setLore(lore);
		return this;
	}
	
	private List<String> getLore() {
		return getMeta().getLore() == null ? new ArrayList<>() : getMeta().getLore();
	}
	
	private ItemMeta getMeta() {
		return getItemStack().getItemMeta();
	}
	
	private void setMeta(ItemMeta meta) {
		getItemStack().setItemMeta(meta);
	}
	
	public CustomStack enchant(Enchantment ench, int level) {
		getItemStack().addEnchantment(ench, level);
		return this;
	}
	
	public CustomStack setAmount(int amount) {
		getItemStack().setAmount(amount);
		return this;
	}
	
	public CustomStack setDurablity(short d) {
		getItemStack().setDurability(d);
		return this;
	}
	
	public CustomStack addItemFlag(ItemFlag flag) {
		ItemMeta m = getMeta();
		m.addItemFlags(flag);
		setMeta(m);
		return this;
	}
	
	public CustomStack setSoulbound() {
		if (!getLore().contains(Lang.SOULBOUND.toString())) {
			addStringInLore(Lang.SOULBOUND.toString());
		}
		return this;
	}
	
	public CustomStack setUndroppable() {
		if (!getLore().contains(Lang.UNDROPPABLE.toString())) {
			addStringInLore(Lang.UNDROPPABLE.toString());
		}
		return this;
	}
}
