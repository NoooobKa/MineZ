package com.NBK.MineZ.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.CustomStack;

public class Area implements Listener{

	private final String name;
	private final Location firsPoint;
	private final Location secondPoint;
	private boolean pvp;
	private boolean visible;
	private boolean canMobsSpawn;
	private Inventory toolInv;
	
	public Area(String name, Location firstPoint, Location secondPoint) {
		this.name = name;
		this.firsPoint = firstPoint;
		this.secondPoint = secondPoint;
		this.pvp = true;
		this.visible = true;
		this.canMobsSpawn = true;
		this.toolInv = createInv();
		Bukkit.getPluginManager().registerEvents(this, MineZMain.INSTANCE);
	}
	
	public String getName() {
		return name;
	}
	
	public Location getFirstPoint() {
		return firsPoint;
	}
	
	public Location getSecondPoint() {
		return secondPoint;
	}
	
	public boolean isPvp() {
		return pvp;
	}

	public void setPvp(boolean pvp) {
		this.pvp = pvp;
		CustomStack is = new CustomStack(Material.DIAMOND_SWORD).setName("§6PVP");
		if (isPvp())is.enchant(Enchantment.DURABILITY, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS);
		getToolInv().setItem(3, is.getItemStack());
		getToolInv().setItem(12, new CustomStack(Material.STAINED_GLASS_PANE).setName(isPvp() ? "§aEnabled" : "§cDisabled").setDurablity((short) (isPvp() ? 5 : 14)).getItemStack());
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		getToolInv().setItem(4, new CustomStack(Material.STAINED_GLASS_PANE).setName("§6VISIBLE").setDurablity((short) (isVisible() ? 0 : 15)).getItemStack());
		getToolInv().setItem(13, new CustomStack(Material.STAINED_GLASS_PANE).setName(isVisible() ? "§aEnabled" : "§cDisabled").setDurablity((short) (isVisible() ? 5 : 14)).getItemStack());

	}

	public boolean isCanMobsSpawn() {
		return canMobsSpawn;
	}

	public void setCanMobsSpawn(boolean canMobsSpawn) {
		this.canMobsSpawn = canMobsSpawn;
		getToolInv().setItem(5, new CustomStack(Material.EGG).setType(isCanMobsSpawn() ? Material.MONSTER_EGG : Material.EGG).setDurablity((short) (isCanMobsSpawn() ? 54 : 0)).getItemStack());
		getToolInv().setItem(14, new CustomStack(Material.STAINED_GLASS_PANE).setName(isCanMobsSpawn() ? "§aEnabled" : "§cDisabled").setDurablity((short) (isCanMobsSpawn() ? 5 : 14)).getItemStack());
	}
	
	public boolean enterInToX(double x) {
		double x1 = firsPoint.getX();
		double x2 = secondPoint.getX();
		if (x1 > x2) {
			x1 = secondPoint.getX();
			x2 = firsPoint.getX();
		}
		return x1 < x && x2 > x;
	}
	
	public boolean enterInToY(double y) {
		double x1 = firsPoint.getY();
		double x2 = secondPoint.getY();
		if (x1 > x2) {
			x1 = secondPoint.getY();
			x2 = firsPoint.getY();
		}
		return x1 < y && x2 > y;
	}
	
	public boolean enterInToZ(double z) {
		double x1 = firsPoint.getZ();
		double x2 = secondPoint.getZ();
		if (x1 > x2) {
			x1 = secondPoint.getZ();
			x2 = firsPoint.getZ();
		}
		return x1 < z && x2 > z;
	}
	
	public boolean enterInToLoc(Location l) {
		return enterInToX(l.getX()) && enterInToY(l.getY()) && enterInToZ(l.getZ());
	}
	
	private Inventory createInv() {
		Inventory inv = Bukkit.createInventory(null, 18, "§4§l" + getName());
		CustomStack pvp = new CustomStack(Material.DIAMOND_SWORD).setName("§6PVP");
		if (isPvp())pvp.enchant(Enchantment.DURABILITY, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS);
		CustomStack visible = new CustomStack(Material.STAINED_GLASS_PANE).setName("§6VISIBLE").setDurablity((short) (isVisible() ? 0 : 15));
		CustomStack canMobsSpawn = new CustomStack(Material.EGG).setType(isCanMobsSpawn() ? Material.MONSTER_EGG : Material.EGG).setDurablity((short) (isCanMobsSpawn() ? 54 : 0)).setName("§6CanMobsSpawn");
		inv.setItem(3, pvp.getItemStack());
		inv.setItem(4, visible.getItemStack());
		inv.setItem(5, canMobsSpawn.getItemStack());
		inv.setItem(12, new CustomStack(Material.STAINED_GLASS_PANE).setName(isPvp() ? "§aEnabled" : "§cDisabled").setDurablity((short) (isPvp() ? 5 : 14)).getItemStack());
		inv.setItem(13, new CustomStack(Material.STAINED_GLASS_PANE).setName(isVisible() ? "§aEnabled" : "§cDisabled").setDurablity((short) (isVisible() ? 5 : 14)).getItemStack());
		inv.setItem(14, new CustomStack(Material.STAINED_GLASS_PANE).setName(isCanMobsSpawn() ? "§aEnabled" : "§cDisabled").setDurablity((short) (isCanMobsSpawn() ? 5 : 14)).getItemStack());
		inv.setItem(inv.getSize() - 1, new CustomStack(Material.BARRIER).setName("§cRemove area").getItemStack());
		inv.setItem(9, new CustomStack(Material.COMPASS).setName("§aBack").getItemStack());
		inv.setItem(0, new CustomStack(Material.ENDER_PEARL).setName("§aTeleport").addStringInLore("§bTeleports you to the first point").getItemStack());
		return inv;
	}

	@EventHandler
	public void onGUIInteract(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getInventory().equals(toolInv)) {
			e.setCancelled(true);
			switch (e.getSlot()) {
			case 0:
				e.getWhoClicked().teleport(getFirstPoint());
				e.getWhoClicked().sendMessage("§dYou are teleported to §a§l" + getName());
				break;
			case 9:
				e.getWhoClicked().openInventory(new AreaGUI().getAreasList().get(0));
				break;
			case 12:
				setPvp(isPvp() ? false : true);
				break;
			case 13:
				setVisible(isVisible() ? false : true);
				break;
			case 14:
				setCanMobsSpawn(isCanMobsSpawn() ? false : true);
				break;
			case 17:
				AreasManager.getManager().removeArea(this, (Player) e.getWhoClicked());
				e.getWhoClicked().closeInventory();
				break;
			default:
				break;
			}
		}
	}
	
	public Inventory getToolInv() {
		return toolInv;
	}
	
	
}
