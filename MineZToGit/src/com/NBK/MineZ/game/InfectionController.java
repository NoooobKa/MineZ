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

public class InfectionController {

	private UUID id;
	private BukkitTask bt;
	
	public InfectionController(UUID id) {
		this.id = id;
	}
	
	public void startPoisoning() {
		MZPlayer mzp = MZPlayer.getPlayer(this.id);
		if (this.bt != null || !mzp.isLiving()) {
			return;
		}
		mzp.getStat().setInfection(true);
		Player p = mzp.getPlayer();
		p.sendMessage(Lang.INFECTION_START_TEXT.toString());
		this.bt = new BukkitRunnable() {
			int i = -10;
			@Override
			public void run() {
				i++;
				if (i == 0) {
					p.sendMessage(Lang.INFECTION_DAMAGE_TEXT.toString());
					if (p.getHealth() > 1) {
						p.setHealth(p.getHealth() - 1);
					}else {
						p.setHealth(0.0f);
					}
					p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 2));
				}
				if (i == Config.INFECTION_EFFECT_PERIOD.toInt() * 5) {
					i = -1;
				}
			}
		}.runTaskTimer(MineZMain.INSTANCE, 10, 4);
	}
	
	public void stopPoisoning() {
		if (this.bt != null) {
			MZPlayer p = MZPlayer.getPlayer(this.id);
			p.getStat().setInfection(false);
			if (p.isLiving()) {
				p.getPlayer().sendMessage(Lang.INFECTION_STOP_TEXT.toString());
			}
			this.bt.cancel();
			this.bt = null;
		}
	}
	
	public boolean isPoisoning() {
		return this.bt != null ? true : false;
	}
	
}
