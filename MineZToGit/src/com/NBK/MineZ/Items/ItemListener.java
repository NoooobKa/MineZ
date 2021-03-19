package com.NBK.MineZ.Items;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.main.Config;
import com.NBK.MineZ.main.Lang;

public class ItemListener implements Listener{

	private Plugin p;
	
	public ItemListener(Plugin p) {
		this.p = p;
		Bukkit.getPluginManager().registerEvents(this, p);
	}
	
	public void pullEntityToLocation(final Entity e, final Location loc) {
	    final Location entityLoc = e.getLocation();
	    entityLoc.setY(entityLoc.getY() + 0.5);
	    e.teleport(entityLoc);
	    entityLoc.setX(Math.round(entityLoc.getX()));
	    entityLoc.setY(Math.round(entityLoc.getY()));
	    entityLoc.setZ(Math.round(entityLoc.getZ()));
	    final double g = -0.08;
	    final double t = Math.round(loc.distance(entityLoc));
	    final double v_x = (1.5 + 0.07 * t) * (Math.round(loc.getX()) - entityLoc.getX()) / t;
	    final double v_y = (1.3 + 0.03 * t) * (Math.round(loc.getY()) - entityLoc.getY()) / t - 0.5 * g * t;
	    final double v_z = (1.5 + 0.07 * t) * (Math.round(loc.getZ()) - entityLoc.getZ()) / t;
	    final Vector v = e.getVelocity();
	    v.setX(v_x);
	    v.setY(v_y);
	    v.setZ(v_z);
	    e.setVelocity(v);
	 }
	
	@EventHandler
	public void onPlayerFish(final PlayerFishEvent e) {
		Player p = e.getPlayer();
		Location hookLocation = e.getHook().getLocation();
		if (p.getItemInHand() == null || !p.getItemInHand().hasItemMeta() || !p.getItemInHand().getItemMeta().hasDisplayName() && !p.getItemInHand().getItemMeta().getDisplayName().contains(Lang.GRAPPLE.toString()))return;
		if ((e.getState() == State.IN_GROUND || hookLocation.getWorld().getBlockAt(hookLocation.clone().add(0, -0.2, 0)).getType() != Material.AIR || hookLocation.getWorld().getBlockAt(hookLocation.clone().add(0, 0.3, 0)).getType() != Material.AIR) && p.getLocation().distance(hookLocation) > 2){
            pullEntityToLocation(p, hookLocation);
            if (p.getItemInHand().getDurability() < 64) {
                p.getItemInHand().setDurability((short)(p.getItemInHand().getDurability() + 8));
            }
            else {
                p.setItemInHand(null);
                p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
            }
        p.playSound(p.getLocation(), Sound.GHAST_FIREBALL, 1f, 1.5f);  
		}
	}
	 
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		if (e.getItem().getType() == Material.POTION) {
			if (e.getItem().getDurability() != 0) {
				new BukkitRunnable() {
					@Override
					public void run() {
						p.setItemInHand(null);
					}
				}.runTask(this.p);
			}else {
				MZPlayer.getPlayer(e.getPlayer()).getThirstController().refresh();
			}
		}
		if (e.getItem().getType() == Material.MILK_BUCKET) {
			MZPlayer.getPlayer(e.getPlayer()).getInfectionConroller().stopPoisoning();
		}
		if (e.getItem().getType().isEdible()) {
			if (p.getHealth() < 19) {
				p.setHealth(p.getHealth() + 1);
			}else {
				p.setHealth(p.getMaxHealth());
			}
		}
	}
	
	@EventHandler
	public void onLaunchProj(ProjectileLaunchEvent e) {
		if (e.getEntity() instanceof EnderPearl && e.getEntity().getShooter() instanceof Player) {
			MZPlayer p = MZPlayer.getPlayer((Player)e.getEntity().getShooter());
			Long time = System.currentTimeMillis();
			@SuppressWarnings("deprecation")
			Block b = p.getPlayer().getTargetBlock((HashSet<Byte>)null, 10);
			if (b != null && p.getPlayer().getLocation().distance(b.getLocation()) < 7) {
				e.setCancelled(true);
				p.getPlayer().sendMessage(Lang.GRANADE_TOO_CLOSE.toString());
				new BukkitRunnable() {
					@Override
					public void run() {
						p.getPlayer().setItemInHand(CustomItems.GRANADE.getCustomStack().getItemStack());
					}
				}.runTask(this.p);
				return;
			}
			if (p.getLastGranade() + 1000 * Config.GRANDE_COOLDOWN.toInt() > time) {
				e.setCancelled(true);
				p.getPlayer().sendMessage(Lang.GRANADE_COOLDOWN.toString());
				new BukkitRunnable() {
					@Override
					public void run() {
						p.getPlayer().setItemInHand(CustomItems.GRANADE.getCustomStack().getItemStack());
					}
				}.runTask(this.p);
				return;
			}
			p.setLastGranade(time);
		}
	}
	
	@EventHandler
	public void oninteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getItem() != null ) {
				if (e.getItem().getType() == Material.SUGAR) {
					MZPlayer p = MZPlayer.getPlayer(e.getPlayer().getUniqueId());
					Long time = System.currentTimeMillis();
					if (p.getLastSugar() + 40000 > time) {
						e.getPlayer().sendMessage(Lang.SUGAR_COOLDOWN.toString());
						return;
					}
					p.setLastSugar(time);
					if (e.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
						e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
					}
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1));
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BURP, 1f, 1f);
					e.getPlayer().setItemInHand(null);
					new BukkitRunnable() {
						
						@Override
						public void run() {
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 1));
						}
					}.runTaskLater(this.p, 600);
				}
				if (e.getItem().getType() == Material.PAPER) {
					Player p = e.getPlayer();
					p.sendMessage(Lang.BANDAGE_USE.toString());
					p.setItemInHand(null);
					MZPlayer mzp = MZPlayer.getPlayer(e.getPlayer().getUniqueId());
					mzp.getBleadingController().stopBleading();
					if (p.getHealth() < p.getMaxHealth() - 1) {
						p.setHealth(p.getHealth() + 1);
					}else {
						p.setHealth(p.getMaxHealth());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if(e.getCause() == TeleportCause.ENDER_PEARL) {
			e.setCancelled(true);
			Location to = e.getTo();
			to.getWorld().createExplosion(e.getTo().getX(), e.getTo().getY(), e.getTo().getZ(), 3F, false, false);
			e.getPlayer().spigot().playEffect(to, Effect.LAVA_POP, 0, 0, 1.0f, 1.0f, 1.0f, 0.4f, 60, 30);
			e.getPlayer().spigot().playEffect(to, Effect.SMALL_SMOKE, 0, 0, 1.0f, 1.0f, 1.0f, 0.4f, 60, 30);
		}
	}
	
}
