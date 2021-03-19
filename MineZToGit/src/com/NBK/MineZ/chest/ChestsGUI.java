package com.NBK.MineZ.chest;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.CustomStack;
import com.NBK.MineZ.util.Util;

public class ChestsGUI implements Listener{

	private Inventory inv;
	
	public ChestsGUI() {
		Collection<? extends MineZChest> c = MineZChest.getChests();
		this.inv = Bukkit.createInventory(null, (int)(9 * Math.ceil(MineZChest.getChests().size() / 9.0)), "Chests");
		for (int i = 0; i < c.size(); i++) {
			MineZChest mzc = (MineZChest)c.toArray()[i];
			CustomStack cs = new CustomStack(Material.CHEST);
			cs.setName(mzc.getName()).
			addStringInLore("§7Total percent§f: §a" + mzc.getItemMap().getTotal() + "§7%").
			addStringInLore(" ").
			addStringInLore("§7Items§f:");
			for (double d : mzc.getItemMap().getMap().keySet()) {
				cs.addStringInLore(ChatColor.DARK_AQUA + Util.getItemName(mzc.getItemMap().getMap().get(d)) + " §a" + mzc.getItemMap().getProcentOfElement(mzc.getItemMap().getMap().get(d)) + "§7%");
			}
			cs.addStringInLore(" ").
			addStringInLore("§7Quantity percentage§f§l(§a" + mzc.getWeightItemsQuanity().getTotal() + "§7%§f§l)§f:");
			for (double d : mzc.getWeightItemsQuanity().getMap().keySet()) {
				cs.addStringInLore("§7Amount§f: §3" + mzc.getWeightItemsQuanity().getMap().get(d) + " §a" + mzc.getWeightItemsQuanity().getProcentOfElement(mzc.getWeightItemsQuanity().getMap().get(d)) + " §7%");
			}
			inv.setItem(i, cs.getItemStack());
		}
		Bukkit.getPluginManager().registerEvents(this, MineZMain.INSTANCE);
	}
	
	public Inventory getInv() {
		return inv;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().equals(inv)) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.CHEST && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
				MineZChest mzc = MineZChest.byName(e.getCurrentItem().getItemMeta().getDisplayName());
				if (mzc != null) {
					e.getWhoClicked().openInventory(mzc.getToolGUI());
				}
			}
		}
	}
	
}
