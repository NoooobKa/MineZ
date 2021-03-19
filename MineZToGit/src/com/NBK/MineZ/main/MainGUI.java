package com.NBK.MineZ.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.NBK.MineZ.chest.ChestsGUI;
import com.NBK.MineZ.mobs.Spawner;
import com.NBK.MineZ.util.CustomStack;
import com.NBK.MineZ.world.AreaGUI;

public class MainGUI implements Listener{

	private Inventory inv;
	
	public MainGUI() {
		this.inv = Bukkit.createInventory(null, 9, "§f§lMine§4§lZ");
		inv.setItem(0, new CustomStack(Material.CHEST).setName("§6Chests").getItemStack());
		inv.setItem(1, new CustomStack(Material.GRASS).setName("§aArea").getItemStack());
		inv.setItem(2, new CustomStack(Material.MOB_SPAWNER).setName("§cSpawners").getItemStack());
		Bukkit.getPluginManager().registerEvents(this, MineZMain.INSTANCE);
	}
	
	public Inventory getInv() {
		return inv;
	}
	
	@EventHandler
	public void ice(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().equals(inv)) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null) {
				switch (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) {
				case "Chests":
					e.getWhoClicked().openInventory(new ChestsGUI().getInv());
					break;
				case "Area":
					e.getWhoClicked().openInventory(new AreaGUI().getInv());
					break;
				case "Spawners":
					Inventory inv = Spawner.getGUI().getMainGUI();
					e.getWhoClicked().openInventory(inv);
					break;
				default:
					break;
				}
			}
		}
	}
	
}
