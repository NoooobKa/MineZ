package com.NBK.MineZ.game;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.main.Config;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;

public class ThirstController {

	private UUID id;
	private BukkitTask bt;
	private BukkitTask dt;
	
	public ThirstController(UUID id) {
		this.id = id;
	}
	
	public MZPlayer getPlayer() {
		return MZPlayer.getPlayer(id);
	}
	
	private BukkitTask getThirstTask() {
		return bt;
	}
	
	private void setThirstTask(BukkitTask bt) {
		this.bt = bt;
	}
	
	private BukkitTask getDamageTask() {
		return dt;
	}
	
	private void setDamageTask(BukkitTask dt) {
		this.dt = dt;
	}
	
	public void startThirst() {
		MZPlayer mzp = getPlayer();
		Player p = mzp.getPlayer();
		setThirstTask(
			new BukkitRunnable() {
			@Override
			public void run() {
				if (mzp.isLiving()) {
					if (p.getLevel() > 1) {
						p.setLevel(p.getLevel() - 1);
					}else {
						p.setLevel(0);
					}
					
					if (p.getLevel() == 10) {
						p.sendMessage(Lang.THIRST_10LEVEL_TEXT.toString());
					}else {
						if (p.getLevel() == 4) {
							p.sendMessage(Lang.THIRST_4LEVEL_TEXT.toString());
						}
					}
					
					if (p.getLevel() == 0 && getDamageTask() == null) {
						p.sendMessage(Lang.THIRST_0LEVEL_TEXT.toString());
						setDamageTask(new BukkitRunnable() {
							
							@Override
							public void run() {
								if (p.getLevel() > 0) {
									this.cancel();
									return;
								}
								p.sendMessage(Lang.THIRST_DAMAGE_TEXT.toString());
								p.damage(0);
								p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 2));
								if (p.getHealth() > Config.THIRST_DAMAGE.toInt()) {
									p.setHealth(p.getHealth() - Config.THIRST_DAMAGE.toInt());
								}else {
									p.damage(Integer.MAX_VALUE);
								}
							}
						}.runTaskTimer(MineZMain.INSTANCE, 100, Config.THIRST_DAMAGE_PERIOD.toInt() * 20));
					}
					
				}
			}
		}.runTaskTimer(MineZMain.INSTANCE, 100, Config.THIRST_LEVEL_PERIOD.toInt() * 20));
	}
	
	public void stopThirst() {
		if (getDamageTask() != null) {
			getDamageTask().cancel();
			setDamageTask(null);
		}
		if (getThirstTask() != null) {
			getThirstTask().cancel();
			setThirstTask(null);
		}
	}
	
	public void refresh() {
		stopThirst();
		getPlayer().getPlayer().setLevel(20);
		getPlayer().getPlayer().sendMessage(Lang.THIRST_RELOAD_TEXT.toString());
		startThirst();
	}
	
}
