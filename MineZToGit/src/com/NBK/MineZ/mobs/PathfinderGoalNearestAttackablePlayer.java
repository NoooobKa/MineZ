package com.NBK.MineZ.mobs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IEntitySelector;
import net.minecraft.server.v1_8_R3.PathfinderGoalTarget;

public class PathfinderGoalNearestAttackablePlayer extends PathfinderGoalTarget{

	protected EntityPlayer attackablePlayer;
	protected Predicate<EntityPlayer> predicate;
	protected final DistanceComparator distanceComporator;
	
	public PathfinderGoalNearestAttackablePlayer(EntityCreature entity, boolean flag) {
		this(entity, flag, null);
	}
	
	public PathfinderGoalNearestAttackablePlayer(EntityCreature entity, boolean flag, final Predicate<EntityPlayer> predicate) {
		super(entity, flag);
		this.distanceComporator = new DistanceComparator(entity);
		a(1);
		this.predicate = new Predicate<EntityPlayer>() {
	        public boolean a(EntityPlayer player) {
	            if (predicate != null && !predicate.apply(player))return false;
	            double followRange = PathfinderGoalNearestAttackablePlayer.this.f();
	            if (player.isSneaking()) {
	            	followRange *= 0.1D; 
	            }else {
	            	if (player.isSprinting()) {
	            		followRange *= 10.0D;
	            	}else {
	            		followRange *= 0.2D;
	            	}
	            }
	            if (player.g(PathfinderGoalNearestAttackablePlayer.this.e) > followRange)return false; 
	            return PathfinderGoalNearestAttackablePlayer.this.a(player, false);
	        }

			@Override	
			public boolean apply(EntityPlayer player) {
				return a(player);
			}
		};
	}
	
	@Override
	public boolean a() {
	    double followRange = f();
	    List<EntityPlayer> list = this.e.world.a(EntityPlayer.class, this.e.getBoundingBox().grow(followRange, 4.0D, followRange), Predicates.and(predicate, IEntitySelector.d));
	    Collections.sort(list, distanceComporator);
	    if (list.isEmpty())return false;
	    this.attackablePlayer = (EntityPlayer)list.get(0);
	    if (attackablePlayer.isSneaking()){
	    	followRange *= 0.13D;
	    	float distance = attackablePlayer.g(this.e);
	    	if (distance > followRange/2 && distance < followRange) {
	    		this.e.getNavigation().a(attackablePlayer, 0.63);
	    		return false;
	    	}
	    }
	    return true;
	}
	
	public void c() {
	  this.e.setGoalTarget(attackablePlayer, TargetReason.CLOSEST_PLAYER, true);
	  super.c();
	}
	
	  public static class DistanceComparator implements Comparator<Entity> {
		    private final Entity a;
		    
		    public DistanceComparator(Entity entity) {
		      this.a = entity;
		    }
		    
		    public int a(Entity entity, Entity entity1) {
		      double d0 = this.a.h(entity);
		      double d1 = this.a.h(entity1);
		      return (d0 < d1) ? -1 : ((d0 > d1) ? 1 : 0);
		    }
		    
		    public int compare(Entity object, Entity object1) {
		      return a(object, object1);
		    }
	 }
	
}
