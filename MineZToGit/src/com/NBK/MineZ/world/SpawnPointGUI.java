package com.NBK.MineZ.world;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.util.CustomStack;

public class SpawnPointGUI implements Listener{

	private UUID id;
	private List<Inventory> invs;
	
	public SpawnPointGUI(UUID id) {
		this.id = id;
		this.invs = loadGUI();
	}
	
	private List<Inventory> loadGUI() {
		List<Inventory> invs = new ArrayList<>();
		int size = (int) Math.ceil(SpawnPoint.getSpawnsAmount() / 45.0);
		for (int i = 1; i <= size; i++) {
			Inventory inv = Bukkit.createInventory(null, 54, "Spawns §7§l[§2" + i + "§7/§2" + size + "§7§l]");
			if (size > 1) {
				if (i > 1) {
					inv.setItem(45, new CustomStack(Material.ARROW).setName(Lang.BACK.toString()).getItemStack());
				}
				if (i < size) {
					inv.setItem(53, new CustomStack(Material.ARROW).setName(Lang.NEXT.toString()).getItemStack());
				}
			}
			inv.setItem(49, new CustomStack(Material.PAPER).setName("Random spawn").getItemStack());
			int to = SpawnPoint.getSpawnsAmount() - ((i - 1) * 45) > 45 ? 45 : i == 1 ? SpawnPoint.getSpawnsAmount() : SpawnPoint.getSpawnsAmount() - ((i-1) * 45);
			for (int j = 0; j < to ; j++) {
				Location loc = SpawnPoint.getSpawn(((i - 1) * 45) + j);
				if (loc == null) {
					loc = SpawnPoint.getSpawns().get(SpawnPoint.getSpawns().navigableKeySet().toArray()[((i - 1) * 45) + j]);
				}
				inv.setItem(j, new CustomStack(Material.GRASS).setName("Spawn " + (((i - 1) * 45) + j)).
						addStringInLore("§aX§f: §e" + (int)loc.getX()).
						addStringInLore("§aY§f: §e" + (int)loc.getY()).
						addStringInLore("§aZ§f: §e" + (int)loc.getZ()).getItemStack());
			}
			invs.add(inv);
		}
		return invs;
	}
	
	public List<Inventory> getInvs() {
		return invs;
	}
	
	public void open() {
		if (invs.size() > 0) {
			Bukkit.getPlayer(id).openInventory(invs.get(0));
		}
	}
	
	public Inventory next(Inventory inv) {
		if (invs.contains(inv)) {
			for (int i = 0; i < invs.size(); i++) {
				if (invs.get(i).getTitle().equals(inv.getTitle())) {
					return invs.get(i + 1);
				}
			}
		}
		return null;
	}
	
	public Inventory back(Inventory inv) {
		if (invs.contains(inv)) {
			for (int i = 0; i < invs.size(); i++) {
				if (invs.get(i).getTitle().equals(inv.getTitle())) {
					return invs.get(i - 1);
				}
			}
		}
		return null;
	}
	
	public static void registerListener(Plugin p) {
		new GUIListener(p);
	}
	
	private static class GUIListener implements Listener{
		
		public GUIListener(Plugin p) {
			Bukkit.getPluginManager().registerEvents(this, p);
		}
		
		@EventHandler
		public void onInventory(InventoryClickEvent e) {
			if (e.getInventory() != null && e.getInventory().getTitle() != null && e.getInventory().getTitle().contains("Spawns")) {
				e.setCancelled(true);
				if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
					ItemStack is = e.getCurrentItem();
					if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
						String name = is.getItemMeta().getDisplayName();
						MZPlayer p = MZPlayer.getPlayer((Player)e.getWhoClicked());
						if (name.equals(Lang.BACK.toString())) {
							e.getWhoClicked().openInventory(p.getSpawnGUI().back(e.getInventory()));
						}
						if (name.equals(Lang.NEXT.toString())) {
							e.getWhoClicked().openInventory(p.getSpawnGUI().next(e.getInventory()));
						}
						if (is.getType() == Material.GRASS && name.contains("Spawn")) {
							p.spawn(Integer.valueOf(name.split("\\s")[1]));
						}
						if (is.getType() == Material.PAPER && name.equals("Random spawn")) {
							p.spawn(SpawnPoint.getRandomSpawn());
						}
					}
				}
			}
		}
		
	}
}
