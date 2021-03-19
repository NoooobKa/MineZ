package com.NBK.MineZ.world;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.NBK.MineZ.Items.AreaHelper;
import com.NBK.MineZ.main.MainGUI;
import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.CustomStack;
import com.NBK.MineZ.util.Util;

public class AreaGUI implements Listener{

	private Inventory mainInv;
	private List<Inventory> areas;
	
	public AreaGUI() {
		this.mainInv = Bukkit.createInventory(null, 9, "§aAreas manager");
		this.areas = loadGUIAreas();
		mainInv.setItem(0, new CustomStack(Material.LEATHER).setName("§bGet AreaHelper").getItemStack());
		mainInv.setItem(1, new CustomStack(Material.GRASS).setName("§bClick me to customize areas").getItemStack());
		mainInv.setItem(8, new CustomStack(Material.COMPASS).setName("§aBack").addStringInLore("Will take you back to MainGUI").getItemStack());
		Bukkit.getPluginManager().registerEvents(this, MineZMain.INSTANCE);
	}
	
	public Inventory getInv() {
		return mainInv;
	}
	
	public List<Inventory> getAreasList() {
		return areas;
	}
	
	@EventHandler
	public void onGUIClick(InventoryClickEvent e) {
		if (e.getClickedInventory() != null) {
			if (e.getClickedInventory().equals(mainInv)) {
				e.setCancelled(true);
				switch (e.getSlot()) {
				case 0:
					e.getWhoClicked().setItemInHand(new AreaHelper(new ItemStack(Material.LEATHER)).getItem());
					e.getWhoClicked().closeInventory();
					break;
				case 1:	
					if (areas.size() > 0)e.getWhoClicked().openInventory(areas.get(0));
					break;
				case 8:
					e.getWhoClicked().openInventory(new MainGUI().getInv());
					break;
				default:
					break;
				}
			}
			if (areas.contains(e.getClickedInventory())) {
				e.setCancelled(true);
				if (e.getCurrentItem().getType() != Material.AIR) {
					if (e.getCurrentItem().getType() == Material.GRASS && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
						String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
						if (AreasManager.getManager().containsName(name)) {
							Area a = AreasManager.getManager().getAreaByName(name);
							e.getWhoClicked().openInventory(a.getToolInv());
						}
					}
					switch (e.getSlot()) {
					case 18:
						e.getWhoClicked().openInventory(back(e.getClickedInventory()));
						break;
					case 26:
						e.getWhoClicked().openInventory(next(e.getClickedInventory()));
						break;
					default:
						break;
					}
				}
			}
		}
	}
	
	private Inventory back(Inventory inv) {
		Inventory back = null;
		boolean found = false;
		for (Inventory i : areas) {
			if (i.equals(inv)) {
				found = true;
				break;
			}
			back = i;
		}
		return found ? back : inv;
	}
	
	private Inventory next(Inventory inv) {
		Inventory next = null;
		boolean isThis = false;
		for (Inventory i : areas) {
			if (isThis) {
				next = i;
				break;
			}
			if (i.equals(inv)) {
				isThis = true;
			}
		}
		return next != null ? next : inv;
	}
	
	private List<Inventory> loadGUIAreas() {
		List<Inventory> invs = new ArrayList<>();
		int size = AreasManager.getManager().getAreas().size();
		int areaIndex = 0;
		int invsSize = (int) Math.ceil(size/35.0);
		for (int i = 0; i < invsSize; i++) {
			Inventory inv = Bukkit.createInventory(null, 45, "§2Areas " + (i + 1) + "§7/§2" + invsSize);
			if (i != 0)inv.setItem(18, new CustomStack(Material.ARROW).setName("§aBack").getItemStack());
			if (i != invsSize-1)inv.setItem(26, new CustomStack(Material.ARROW).setName("§aNext").getItemStack());
			for (int j = 0; j < inv.getSize(); j++) {
				if (areaIndex == size)break;
				if (j == 0 || j%9 == 0 || (j + 1)%9 == 0)continue;
				Area a = (Area)AreasManager.getManager().getAreas().toArray()[areaIndex];
				inv.setItem(j, new CustomStack(Material.GRASS).setName("§a§l"+ a.getName()).
						addStringInLore("§6PVP§f: " + (a.isPvp() ? Util.getCheckMarkImage() : Util.getCrossImage())).
						addStringInLore("§6CanMobsSpawn§f: " + (a.isCanMobsSpawn() ? Util.getCheckMarkImage() : Util.getCrossImage())).
						addStringInLore("§6Visible§f: " + (a.isVisible() ? Util.getCheckMarkImage() : Util.getCrossImage())).
						addStringInLore("§ePos1§f: §2" + a.getFirstPoint().getWorld().getName() + " " + ChatColor.GREEN + (int)a.getFirstPoint().getX() + " " + (int)a.getFirstPoint().getY() + " " + (int)a.getFirstPoint().getZ()).
						addStringInLore("§ePos2§f: §2" + a.getSecondPoint().getWorld().getName() + " " + ChatColor.GREEN + (int)a.getSecondPoint().getX() + " " + (int)a.getSecondPoint().getY() + " " + (int)a.getSecondPoint().getZ()).getItemStack());
				areaIndex++;
			}
			invs.add(inv);
		}
		return invs;
	}
	
}
