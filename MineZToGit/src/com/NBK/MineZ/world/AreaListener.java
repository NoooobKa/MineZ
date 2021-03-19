package com.NBK.MineZ.world;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.Items.AreaHelper;
import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.util.Title;

public class AreaListener implements Listener{
	
	public AreaListener(Plugin p) {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (MZPlayer mzp : MZPlayer.getPlayers()) {
					if (mzp.isOnline()) {
						Collection<Area> currentAreas = AreasManager.getManager().getAreasByLocation(mzp.getPlayer().getLocation());
						if (currentAreas.size() > 0) {
							Area currentArea = (Area) currentAreas.toArray()[0];
							if (mzp.getLastArea() == null || !mzp.getLastArea().getName().equals(currentArea.getName())) {
								mzp.setLastArea(currentArea);
								Title.showTitle(mzp.getPlayer(), ChatColor.GREEN + currentArea.getName(), 2, 2, 2);
							}
						}else {
							mzp.setLastArea(null);
						}
					}
				}
			}
		}.runTaskTimer(p, 20, 50);
		Bukkit.getPluginManager().registerEvents(this, p);
	}
	
	@EventHandler
	public void onAreaInteract(PlayerInteractEvent e) {
		if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().contains(Lang.TAG_AREA_HELPER.toString())) {
			e.setCancelled(true);
			AreaHelper ah = new AreaHelper(e.getItem());
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
				ah.setFirstLoc(e.getClickedBlock().getLocation());
			}
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				ah.setSecondLoc(e.getClickedBlock().getLocation());
			}
		}
	}
	
}
