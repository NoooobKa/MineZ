package com.NBK.MineZ.chest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.main.Config;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.CustomStack;
import com.NBK.MineZ.util.RandomCollection;
import com.NBK.MineZ.util.Util;

public abstract class MineZChest implements Listener {

	private static final Map<String, MineZChest> minezChests;
	private String name;
	private ChestConfig config;
	private List<Block> blockList;
	private RandomCollection<ItemStack> chestItems;
	private RandomCollection<Integer> weightItemsQuanity;
	private int secondsToRespawn;
	private Inventory toolGUI;
	
	static {
		minezChests = new ConcurrentHashMap<String, MineZChest>();
	}
	
	public MineZChest(String name, int secondsToRespawn) {
		this.name = name;
		this.config = new ChestConfig(name);
		this.blockList = getConfig().getBlocksFromCfg();
		this.chestItems = new RandomCollection<>();
		this.setWeightItemsQuanity(new RandomCollection<>());
		this.secondsToRespawn = secondsToRespawn;
		this.toolGUI = createToolInv(name);
		this.chestItems = chestItems();
		this.weightItemsQuanity = amountChestItems();
		minezChests.put(name, this);
		Bukkit.getPluginManager().registerEvents(this, MineZMain.INSTANCE);
	}
	
	public int getSecondsToRespawn() {
		return secondsToRespawn;
	}

	public String getName() {
		return name;
	}
	
	/*Returns the item map*/
	public RandomCollection<ItemStack> getItemMap(){
		return chestItems;
	}
	
	/*Add an item to the MineZChest*/
	public void addItemToItemsMap(ItemStack item, double weight) {
		getItemMap().add(weight, item);
	}
	
	/*Overrides items in the MineZChest*/
	public void setItemsMap(RandomCollection<ItemStack> colletion) {
		this.chestItems = colletion;
	}
	
	/*For MineZ GUI*/
	public Inventory getToolGUI() {
		return toolGUI;
	}
	/*will return a list of blocks that are MineZChest*/
	public List<Block> getBlocks(){
		return blockList;
	}
	
	/*do not use*/
	public void setBlocks(List<Block> blockList) {
		this.blockList = blockList;
	}
	
	/*Add a chest by block*/
	public void addBlock(Block b, Player whoAdd) {
		getBlocks().add(b);
		getConfig().addBlockLocation(b, whoAdd);
	}
	
	/*Will remove the chest by block*/
	public void removeBlock(Block b, Player whoRemove) {
		if (getBlocks().remove(b)) {
			getConfig().removeBlockLocation(b, whoRemove);
		}
	}
	
	/*Returns all registered chests*/
	public static Collection<? extends MineZChest> getChests(){
		return minezChests.values();
	}
	
	/*Returns the chest config file*/
	public ChestConfig getConfig() {
		return config;
	}
	
	/*Respawns all chests on the map*/
	@SuppressWarnings("deprecation")
	public void respawnAllChests() {
		for (Block b : getBlocks()) {
			if (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST) {
				Chest chest = (Chest) b.getState();
				for (HumanEntity ent : new ArrayList<HumanEntity>(chest.getInventory().getViewers())) {
					ent.closeInventory();
				}
				chest.getInventory().clear();
				
			}
			b.setType((new Random().nextInt(100) + 1) > Config.TRAPCHESTP_PROBABILITY.toInt() ? Material.CHEST : Material.TRAPPED_CHEST);
			b.setData(b.getMetadata(EnumChestMetadata.FaceDirection.getKey()).get(0).asByte());
			b.setMetadata(EnumChestMetadata.IsOpen.getKey(), new FixedMetadataValue(MineZMain.INSTANCE, false));
			b.setMetadata(EnumChestMetadata.Name.getKey(), new FixedMetadataValue(MineZMain.INSTANCE, getName()));
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	/*So you can get items Quanity map*/
	public RandomCollection<Integer> getWeightItemsQuanity() {
		return weightItemsQuanity;
	}
	
	/*This way you can override the probability of the amount of loot in your chest.*/
	public void setWeightItemsQuanity(RandomCollection<Integer> weightItemsQuanity) {
		this.weightItemsQuanity = weightItemsQuanity;
	}
	
	/*For MineZ GUI*/
	private Inventory createToolInv(String name) {
		Inventory inv = Bukkit.createInventory(null, 9, name);
		inv.setItem(0, new CustomStack(Material.STICK).setName("Get chest helper").getItemStack());
		inv.setItem(1, new CustomStack(Material.NETHER_STAR).setName("Respawn").getItemStack());
		return inv;
	}
	
	/*For MineZ GUI*/
	@EventHandler
	public void onToolInv(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().equals(toolGUI)) {
			switch (e.getSlot()) {
			case 0:
				e.getWhoClicked().getInventory().setItem(e.getWhoClicked().getInventory().firstEmpty(), new CustomStack(Material.STICK).setName(getName()).addStringInLore(Lang.TAG_CHEST_HELPER.toString()).getItemStack());
				e.getWhoClicked().closeInventory();
				break;
			case 1:
				respawnAllChests();
				e.getWhoClicked().closeInventory();
				break;
			default:
				break;
			}
		}
	}
	
	/*Determines the random loot in your chests (when opening a chest)*/
	public void setLoot(Chest chest) {
		chest.getBlock().setMetadata(EnumChestMetadata.IsOpen.getKey(), new FixedMetadataValue(MineZMain.INSTANCE, true));
		int x = getWeightItemsQuanity().next();
		for (int i = 0; i < x; i++) {
			setItem(chest, getItemMap().next());
//			boolean itemIsSet = false;
//			ItemStack item = getItemMap().next();
//			if (Util.isEquipment(item) || Util.isToolOrCombat(item)) {
//				item.setDurability(Util.getRandomWeaponDurablityProcent(item.getType().getMaxDurability()));
//			}
//			if (item.getType() == Material.INK_SACK && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
//				item = Util.getRandomSupportKitElement();
//			}
//			if (item.getType() == Material.FISHING_ROD) {
//				item.setDurability((short) (new Random().nextDouble() > 0.66 ? item.getType().getMaxDurability() - 8 : item.getType().getMaxDurability() - 16));
//			}
//			do {
//				if (chest.getInventory().firstEmpty() == -1)break;
//				int slot = new Random().nextInt(chest.getInventory().getSize());
//				if (chest.getInventory().getItem(slot) == null) {
//					itemIsSet = true;
//					chest.getInventory().setItem(slot, item);
//				}
//			} while (itemIsSet == false);
		}
	}
	
	public boolean setItem(Chest chest, ItemStack item) {
		boolean itemIsSet = false;
		do {
			if (chest.getInventory().firstEmpty() == -1)return false;
			int slot = new Random().nextInt(chest.getInventory().getSize());
			if (chest.getInventory().getItem(slot) == null) {
				itemIsSet = true;
				chest.getInventory().setItem(slot, item);
			}
		} while (itemIsSet == false);
		return true;
	}
	
	
	/*To determine the loot in your chests*/
	public abstract RandomCollection<ItemStack> chestItems();
	
	/*To determine the likelihood / amount of loot in your chests*/
	public abstract RandomCollection<Integer> amountChestItems();
	
	/*Launches repetitive chest respawn tasks, you don't need to change this*/
	public static void runAll() {
		for (String name : minezChests.keySet()) {
			MineZChest mzc = minezChests.get(name);
			new BukkitRunnable() {
				@Override
				public void run() {
					mzc.respawnAllChests();
				}
			}.runTaskTimer(MineZMain.INSTANCE, 0, mzc.getSecondsToRespawn() * 20);
		}
	}
	/*Static method that takes the name of the chest and returns an object @nullable*/
	public static MineZChest byName(String name) {
		return minezChests.get(name);
	}
}
