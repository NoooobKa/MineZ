package com.NBK.MineZ.game;

import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.main.Config;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;

public class BleedingController{

	private UUID id;
	private BukkitTask bt;
	
	public BleedingController(final UUID id) {
		this.id = id;
	}
	
	public void startBleading() {
		MZPlayer mzp = MZPlayer.getPlayer(this.id);
		if (this.bt != null || !mzp.isLiving()) {
			return;
		}
		mzp.getStat().setBleading(true);
		Player p = MZPlayer.getPlayer(this.id).getPlayer();
		p.sendMessage(Lang.BLEADING_START_TEXT.toString());
		this.bt = new BukkitRunnable() {
			int i = -10;
			@Override
			public void run() {
				i++;
				if (i == 0) {
					p.sendMessage(Lang.BLEADING_DAMAGE_TEXT.toString());
					p.damage(0);
					p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 2));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
					if (p.getHealth() > Config.BLEADING_EFFECT_DAMAGE.toInt()) {
						p.setHealth(p.getHealth() - Config.BLEADING_EFFECT_DAMAGE.toInt());
					}else {
						p.damage(Integer.MAX_VALUE);
					}
				}
				if (i == Config.BLEADING_EFFECT_PERIOD.toInt() * 5) {
					i = -1;
				}
				p.spigot().playEffect(p.getEyeLocation(), Effect.TILE_BREAK, 152, 0, 0f, 0.3f, 0f, 0.4f, 1, 10);
			}
		}.runTaskTimer(MineZMain.INSTANCE, 10, 4);
	}
	
	public void stopBleading() {
		if (this.bt != null) {
			MZPlayer mzp = MZPlayer.getPlayer(this.id);
			mzp.getStat().setBleading(false);
			if (mzp.isLiving()) {
				mzp.getPlayer().sendMessage(Lang.BLEADING_STOP_TEXT.toString());
			}
			this.bt.cancel();
			this.bt = null;
		}
	}
	
	public boolean isBleading() {
		return this.bt != null ? true : false;
	}
	
}
