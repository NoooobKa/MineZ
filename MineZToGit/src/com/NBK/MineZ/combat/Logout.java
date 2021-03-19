package com.NBK.MineZ.combat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.main.Lang;

public class Logout implements CommandExecutor, Listener{

	private Plugin plugin;
	
	public Logout(JavaPlugin plugin) {
		this.plugin = plugin;
		plugin.getCommand("Logout").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (s instanceof Player) {
			MZPlayer mzp = MZPlayer.getPlayer((Player)s);
			if (mzp.isLiving()) {
				if (!mzp.isLogouting()) {
					newLogout(mzp.getPlayer());
					return true;
				}else {
					mzp.getPlayer().sendMessage(Lang.LOGOUT_ALREADY_LOGOUT_TEXT.toString());
					return true;
				}
			}else {
				mzp.getPlayer().sendMessage(Lang.LOGOUT_PLAYER_NOT_LIVING_TEXT.toString());
				return true;
			}
		}
		return false;
	}

	public void newLogout(Player p) {
		MZPlayer mzp = MZPlayer.getPlayer(p);
		mzp.setLogouting(true);
		new BukkitRunnable() {
			int timer = 15;
			final Location loc = p.getLocation().clone();
			final EntityDamageEvent event = p.getLastDamageCause();
			@Override
			public void run() {
				p.sendMessage(Lang.LOGOUT_TIMER_TEXT.toString().replace("{TIME}", String.valueOf(timer)));
				if (!p.isOnline() || !equalsLocations(loc, p.getLocation()) || !equalsDamageEvent(p, event)) {
					this.cancel();
					p.sendMessage(Lang.LOGOUT_CANCEL_TEXT.toString());
					mzp.setLogouting(false);
				}
				if (timer-- == 0) {
					p.kickPlayer(Lang.LOGOUT_KICK_TEXT.toString());
					this.cancel();
					p.sendMessage(Lang.LOGOUT_CANCEL_TEXT.toString());
					mzp.setLogouting(false);
				}
			}
		}.runTaskTimer(plugin, 0, 20);
	}
	
	private boolean equalsLocations(Location l1, Location l2) {
		return (int)l1.getX() == (int)l2.getX() && (int)l1.getY() == (int)l2.getY() && (int)l1.getZ() == (int)l2.getZ();
	}
	
	private boolean equalsDamageEvent(Player p , EntityDamageEvent event) {
		return event == null ? p.getLastDamageCause() == null : event.equals(p.getLastDamageCause()); 
	}
	
}
