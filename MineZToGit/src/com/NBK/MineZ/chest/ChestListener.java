package com.NBK.MineZ.chest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.mobs.CustomZombie;
import com.NBK.MineZ.mobs.EntityMap;
import com.NBK.MineZ.util.Util;

public class ChestListener implements Listener{
	
	public ChestListener(Plugin p) {
		Bukkit.getPluginManager().registerEvents(this, p);
	}
	
	@EventHandler
	public void onChestOpen(PlayerInteractEvent e) {
		if (e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST)) {
			Block b = e.getClickedBlock();
			if (b.getType() == Material.TRAPPED_CHEST) {
				Chest chest = (Chest)b.getState();
				if (b.hasMetadata(EnumChestMetadata.Name.getKey()) && b.hasMetadata(EnumChestMetadata.IsOpen.getKey()) && !b.getMetadata(EnumChestMetadata.IsOpen.getKey()).get(0).asBoolean()) {
					MineZChest mzc = MineZChest.byName(b.getMetadata(EnumChestMetadata.Name.getKey()).get(0).value().toString());
					mzc.setLoot(chest);
				}
				CustomZombie cz = (CustomZombie) EntityMap.ZOMBIE.spawn(b.getLocation().add(0.5, 0, 0.5));
				cz.setContent(chest.getInventory().getContents());
				chest.getInventory().clear();
				b.setType(Material.AIR);
				e.getPlayer().getWorld().playSound(e.getClickedBlock().getLocation(), Sound.DIG_WOOD, 0.5F, 1.5F);
				e.getPlayer().sendMessage(Lang.ITS_A_TRAP.toString());
			}
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (b.hasMetadata(EnumChestMetadata.Name.getKey()) && b.hasMetadata(EnumChestMetadata.IsOpen.getKey()) && !b.getMetadata(EnumChestMetadata.IsOpen.getKey()).get(0).asBoolean()) {
					MineZChest mzc = MineZChest.byName(b.getMetadata(EnumChestMetadata.Name.getKey()).get(0).value().toString());
					mzc.setLoot((Chest)b.getState());
				}
			}else {
				if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					if (b.hasMetadata(EnumChestMetadata.Name.getKey()) && b.hasMetadata(EnumChestMetadata.IsOpen.getKey()) && !b.getMetadata(EnumChestMetadata.IsOpen.getKey()).get(0).asBoolean()) {
						MineZChest mzc = MineZChest.byName(b.getMetadata(EnumChestMetadata.Name.getKey()).get(0).value().toString());
						mzc.setLoot((Chest)b.getState());
					}
					b.setType(Material.AIR);
					e.getPlayer().getWorld().playSound(e.getClickedBlock().getLocation(), Sound.DIG_WOOD, 0.5F, 1.5F);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMZChestEdit(PlayerInteractEvent e) {
		if (Util.isChestHelper(e.getItem())) {
			e.setCancelled(true);
			MineZChest mzc = MineZChest.byName(e.getItem().getItemMeta().getDisplayName());
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				mzc.addBlock(e.getClickedBlock(), e.getPlayer());
				e.getClickedBlock().setMetadata(EnumChestMetadata.FaceDirection.getKey(), e.getClickedBlock().getType() == Material.CHEST ? new FixedMetadataValue(MineZMain.INSTANCE, e.getClickedBlock().getState().getData().getData()) : new FixedMetadataValue(MineZMain.INSTANCE, Util.getDirectionByPlayerLocation(e.getPlayer().getLocation())));
			}else {
				if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					mzc.removeBlock(e.getClickedBlock(), e.getPlayer());
				}
			}
		} 
	}
	
}
