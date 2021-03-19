package com.NBK.MineZ.game;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.main.Config;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;

public class Board {

	private Scoreboard scoreboard;
	private Objective objective;
	private UUID id;
	
	public Board(UUID id) {
		this.id = id;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = scoreboard.registerNewObjective(Bukkit.getPlayer(id).getName(), "Z");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(Lang.SCOREBOARD_NAME.toString());
	}
	
	public void update() {
		MZPlayer p = MZPlayer.getPlayer(id);
		p.getPlayer().setScoreboard(scoreboard);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		new BukkitRunnable() {
			@Override
			public void run() {
				if (p.isOnline()) {
					for (String s : Board.this.scoreboard.getEntries()) {
						Board.this.scoreboard.resetScores(s);
					}
					Objective o = objective;
					o.getScore(formatter.format(new Date(System.currentTimeMillis()))).setScore(0);
					o.getScore("").setScore(1);
					int ping = p.getHandle().ping;
					o.getScore("§6Ping§f: " + (ping < 60 ? "§a" + ping : ping < 100 ? "§e" + ping : "§c" + ping)).setScore(2);
					o.getScore(" ").setScore(3);
					o.getScore(p.getLastArea() == null ? ChatColor.DARK_GREEN + Lang.WILDERNESS.toString() : ChatColor.GREEN + p.getLastArea().getName()).setScore(4);
					boolean granade = p.getLastGranade() + 1000 * Config.GRANDE_COOLDOWN.toInt() > System.currentTimeMillis();
					boolean sugar = p.getLastSugar() + 40000 > System.currentTimeMillis();
					if (granade && sugar) {
						o.getScore("   ").setScore(5);
						o.getScore(ChatColor.GOLD + Lang.GRANADE.toString() + "§f: " + ChatColor.RED + (int)(p.getLastGranade() + 1000 * Config.GRANDE_COOLDOWN.toInt() - System.currentTimeMillis())/1000).setScore(6);
						o.getScore("    ").setScore(7);
						o.getScore("§6Sugar§f: §c" + (int)(p.getLastSugar() + 40000 - System.currentTimeMillis())/1000).setScore(8);
					}else {
						if (granade) {
							o.getScore("   ").setScore(5);
							o.getScore(ChatColor.GOLD + Lang.GRANADE.toString() + "§f: " + ChatColor.RED + (int)(p.getLastGranade() + 1000 * Config.GRANDE_COOLDOWN.toInt() - System.currentTimeMillis())/1000).setScore(6);
						}else {
							if (sugar) {
								o.getScore("    ").setScore(5);
								o.getScore("§6Sugar§f: §c" + (int)(p.getLastSugar() + 40000 - System.currentTimeMillis())/1000).setScore(6);
							}
						}
					}	
				}else {
					this.cancel();
				}
			}
		}.runTaskTimer(MineZMain.INSTANCE, 20, 20);
	}
	
}
