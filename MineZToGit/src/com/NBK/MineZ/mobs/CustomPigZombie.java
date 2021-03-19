package com.NBK.MineZ.mobs;

import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;

import com.NBK.MineZ.util.NMSUtil;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
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
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class CustomPigZombie extends EntityPigZombie{

	private Random r;
	
	public CustomPigZombie(World world) {
		super(world);
		this.r = new Random();
		((List<?>)NMSUtil.getPrivateField("b", PathfinderGoalSelector.class, goalSelector)).clear();
		((List<?>)NMSUtil.getPrivateField("c", PathfinderGoalSelector.class, goalSelector)).clear();
		((List<?>)NMSUtil.getPrivateField("b", PathfinderGoalSelector.class, targetSelector)).clear();
		((List<?>)NMSUtil.getPrivateField("c", PathfinderGoalSelector.class, targetSelector)).clear();
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, EntityPlayer.class, 1.0D, false));
        this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 1.0D, 8.0F));
        this.goalSelector.a(3, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(5, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 16.0F));
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackablePlayer(this, true));
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(MobsConfig.MOBS_DEFAULT_SPEED.toDouble() * MobsConfig.PIG_ZOMBIE_SPEED_MULTYPLY.toDouble());
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(64);
        this.setHealth(1f);
	}
	
	//damageToThis
	public boolean damageEntity(final DamageSource damagesource, final float f) {
		if (damagesource == DamageSource.STUCK || damagesource == DamageSource.FALL || damagesource.isExplosion()) return false;
		return super.damageEntity(damagesource, f);
	}
	
	@Override
	public void die(DamageSource damageSource) {
		if (!this.isBaby()) {
			this.world.createExplosion(this, locX, locY, locZ, 3, false, false);
			for (int i = 0; i < r.nextInt(MobsConfig.PIG_ZOMBIE_MAX_SPAWN_BABY.toInt()) + 1; i++) {
				CustomPigZombie cpz =  (CustomPigZombie) EntityMap.PIG_ZOMBIE.spawn(new Location(world.getWorld(), r.nextBoolean() ? locX + r.nextDouble() : locX - r.nextDouble(), locY, r.nextBoolean() ? locZ + r.nextDouble() : locZ - r.nextDouble()));
				cpz.setBaby(true);
			}
		}else {
			this.world.createExplosion(this, locX, locY, locZ, 1, false, false);
		}
		this.world.getWorld().spigot().playEffect(new Location(this.world.getWorld(), locX, locY, locZ), Effect.LAVA_POP, 0, 0, 1.0f, 1.0f, 1.0f, 0.4f, 60, 30);
		this.world.getWorld().spigot().playEffect(new Location(this.world.getWorld(), locX, locY, locZ), Effect.SMALL_SMOKE, 0, 0, 1.0f, 1.0f, 1.0f, 0.4f, 60, 30);
		super.die(damageSource);
	}
	
	
    public boolean r(final Entity entity) {
    	if (entity instanceof EntityPlayer) {
    		this.damageEntity(DamageSource.playerAttack((EntityHuman) entity), this.getMaxHealth());
    	}
    	return super.r(entity);
    }
	
}
