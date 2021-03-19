package com.NBK.MineZ.mobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.NBK.MineZ.Items.CustomizedStackItems;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.CustomStack;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityGiantZombie;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.World;

public class CustomGiantZombie extends EntityGiantZombie{

	private Random r;
	private ItemStack[] content;
	private Location spawnLocation;
	
	public CustomGiantZombie(World world) {
		super(world);
		this.r = new Random();
		this.content = new ItemStack[13];
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, EntityPlayer.class, 1.0D, false));
        this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 1.0D, 8.0F));
        this.goalSelector.a(3, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(5, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 16.0F));
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackablePlayer(this, true));
        for (int i = 0; i < r.nextInt(5) + 8; i++) {
			if (r.nextDouble() < 0.1) {
				content[i] = getRare();
			}else {
				content[i] = CustomizedStackItems.ROTTEN_FLESH.getCustomStack().getItemStack();
			}
		}
		this.setHealth((float) MobsConfig.GIANT_HEALTH.toDouble());
		distanceCheck();
	}

	protected void initAttributes() {
		super.initAttributes();
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(MobsConfig.GIANT_HEALTH.toDouble());
		this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(MobsConfig.MOBS_DEFAULT_SPEED.toDouble() * MobsConfig.GIANT_SPEED_MULTYPLY.toDouble());
		this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(8D);
	}
	
	//damageToThis
	public boolean damageEntity(final DamageSource damagesource, final float f) {
		Entity ent = damagesource.getEntity();
		if (ent instanceof EntityPlayer) {
			if (r.nextBoolean()){
				double rand = r.nextDouble();
				if (rand < 0.5) {
					if (rand < 0.05) {
						EntityMap.PIG_ZOMBIE.spawn(getLocation());
					}else {
						EntityMap.ZOMBIE.spawn(getLocation());
					}
				}else {
					if (rand >= 0.5 && rand < 0.8) {
						epicentr();
					}
				}
			}
		}
		return super.damageEntity(damagesource, f);
	}
	
	@Override//onTick
	public void m() {
		super.m();
	}
	
	@Override
	public void die(DamageSource damageSource) {
		dropContent();
		super.die(damageSource);
	}
	
    public boolean r(final Entity entity) {
    	if (entity instanceof EntityPlayer) {
    		double distance = entity.getBukkitEntity().getLocation().distance(getBukkitEntity().getLocation());
    		if (distance > 4.2) {
    			return false;
    		}else {
    			if (distance < 3.2) {
    				Player p = (Player)entity.getBukkitEntity();
					Vector v = getLocation().getDirection().normalize().multiply(5);
					v.setY(0.3);
					p.setVelocity(v);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2));
					entity.damageEntity(DamageSource.mobAttack(this), 4);
    			}
    		}
    	}
    	return super.r(entity);
    }
	
	public void dropContent() {
		if (content != null) {
			for (ItemStack is : content) {
				if (is != null && is.getType() != Material.AIR) {
					if (is.hasItemMeta() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().contains(Lang.SOULBOUND.toString()))continue;
					this.getBukkitEntity().getWorld().dropItemNaturally(((Giant)this.getBukkitEntity()).getEyeLocation(), is);
				}
			}
			
		}
	}
	
	public Location getLocation() {
		return new Location(world.getWorld(), locX, locY, locZ);
	}
	
	public ItemStack getRare() {
		switch (r.nextInt(4)) {
		case 0:
			return new ItemStack(Material.DIAMOND_SWORD);
		case 1:
			return new CustomStack(Material.BOW).enchant(Enchantment.ARROW_INFINITE, 1).getItemStack();
		case 2:
			return CustomizedStackItems.GOLDEN_CARROT.getCustomStack().getItemStack();
		default:
			return CustomizedStackItems.GOLDEN_APPLE.getCustomStack().getItemStack();
		}
	}
	
	public void epicentr() {
		Giant g = (Giant) this.getBukkitEntity();
		g.getWorld().playSound(g.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
		new BukkitRunnable() {
			boolean isJump = false;
			@Override
			public void run() {
				if (!isJump) {
					isJump = true;
					g.setVelocity(new Vector(0.0, 1.5, 0.5));
					return;
				}
				if (!g.isDead() && g.isOnGround()) {
					this.cancel();
					new BukkitRunnable() {
						final Location l = g.getLocation().clone();
						int amount = 8;
						int radius = 1;
				        double increment = (2 * Math.PI) / amount;
				        List<Player> pl = new ArrayList<Player>();
						@Override
						public void run() {
							radius += 5;
							for (int i = 0; i < amount; i++) {
					            double angle = i * increment;
					            double x = l.getX() + (radius * Math.cos(angle));
					            double z = l.getZ() + (radius * Math.sin(angle));
					            g.getWorld().playEffect(new Location(l.getWorld(), x, l.getY(), z), Effect.EXPLOSION_HUGE, 20);
							}
							g.getWorld().playSound(g.getLocation(), Sound.EXPLODE, 1F, 1F);
							for (org.bukkit.entity.Entity entity : g.getNearbyEntities(radius, radius, radius)) {
								if (entity.getType() == EntityType.PLAYER) {
									Player p = (Player) entity;
									if (!pl.contains(p)) {
										p.damage(8);
										p.setVelocity(new Vector(p.getLocation().toVector().subtract(l.toVector()).normalize().getX(), 2/p.getLocation().distance(l), p.getLocation().toVector().subtract(l.toVector()).normalize().getZ()).multiply(3));
										p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 0));
										pl.add(p);
									}
								}
							}
							if (radius == 31)this.cancel();
						}
					}.runTaskTimer(MineZMain.INSTANCE, 0, 8);
				}
			}
		}.runTaskTimer(MineZMain.INSTANCE, 0, 5);
	}
	
	public void distanceCheck() {
		new BukkitRunnable() {
			@Override
			public void run() {
				CustomGiantZombie.this.spawnLocation = new Location(getWorld().getWorld(), locX, locY, locZ);
			}
		}.runTask(MineZMain.INSTANCE);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!CustomGiantZombie.this.isAlive()) {
					this.cancel();
					return;
				}
				if (getGoalTarget() != null) {
					if ((int)locX == (int)lastX && (int)locY == (int)lastY && (int)locZ == (int)lastZ) {
						bF();
					}
				}
				if (CustomGiantZombie.this.getBukkitEntity().getLocation().distance(spawnLocation) > 75) {
					CustomGiantZombie.this.getBukkitEntity().teleport(spawnLocation);
					
				}
			}
		}.runTaskTimer(MineZMain.INSTANCE, 0, 20);
	}
	
}
