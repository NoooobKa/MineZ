package com.NBK.MineZ.mobs;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.NBK.MineZ.Items.CustomizedStackItems;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.util.NMSUtil;
import com.NBK.MineZ.util.Util;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityZombie;
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

public class CustomZombie extends EntityZombie{
	
	private static int two = MobsConfig.ZOMBIE_AXIS_VALUE_TIER_TWO.toInt();
    private static int three = MobsConfig.ZOMBIE_AXIS_VALUE_TIER_THREE.toInt();

	private Random r;
	private ItemStack[] content;
	
	
	public CustomZombie(World world) {
		super(world);
		r = new Random();
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
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(MobsConfig.MOBS_DEFAULT_SPEED.toDouble() * MobsConfig.ZOMBIE_SPEED_MULTIPLY.toDouble());
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(64);
        for (int i = 0; i < this.dropChances.length; i++) {
			this.dropChances[i] = 0F;
		}
        Zombie z = (Zombie) this.getBukkitEntity();
        int tier;
        switch (MobsConfig.MOBS_AXIS.toString()) {
		case "Z":
			tier = locZ <= two ? 0 : (locZ > two && locZ < three) ? 1 : 2;
			break;
		default:
			tier = locX <= two ? 0 : (locX > two && locX < three) ? 1 : 2;
			break;
		}
        z.setMaxHealth(MobsConfig.ZOMBIE_START_HEALTH.toDouble() + MobsConfig.ZOMBIE_BOOST_HEALTH.toInt() * tier);
        z.setHealth(MobsConfig.ZOMBIE_START_HEALTH.toDouble() + MobsConfig.ZOMBIE_BOOST_HEALTH.toInt() * tier);
        z.setNoDamageTicks(10);
        z.setMaximumNoDamageTicks(1);
        z.setRemoveWhenFarAway(false);
	}

	//damageToThis
	public boolean damageEntity(final DamageSource damagesource, final float f) {
		Entity ent = damagesource.getEntity();
		if (ent instanceof EntityPlayer) {
			if (r.nextDouble() < 0.15) {
				hook((Player)ent.getBukkitEntity(), this);
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					Vector v = getBukkitEntity().getVelocity();
					v.setX(v.getX() * 0.7);
					v.setY(v.getY() * 0.4);
					v.setZ(v.getZ() * 0.7);
					getBukkitEntity().setVelocity(v);
				}
			}.runTask(MineZMain.INSTANCE);
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
	
	public void mutation(Player p) {
		if (p != null) {
			setItemInHand(Util.firstSword(p.getInventory()));
			setEquipment(p.getEquipment().getArmorContents());
			setHead(p);
			setContent((ItemStack[]) ArrayUtils.addAll(p.getInventory().getContents(), p.getEquipment().getArmorContents()));
			for (int i = 0; i < content.length; i++) {
				final ItemStack is = content[i];
				if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().contains(Lang.SOULBOUND.toString())) {
					content[i] = null;
				}
			}
			setCustomName(p.getName());
		}
	}
	
	public void setContent(ItemStack[] content) {
		this.content = content;
	}
	
	public void setItemInHand(ItemStack is) {
		((Zombie)this.getBukkitEntity()).getEquipment().setItemInHand(is);
	}
	
	public void setEquipment(ItemStack[] equip) {
		((Zombie)this.getBukkitEntity()).getEquipment().setArmorContents(equip);
	}
	
	public void setHead(Player p) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(p.getUniqueId().toString());
        head.setItemMeta(meta);
        ((Zombie)this.getBukkitEntity()).getEquipment().setHelmet(head);
	}
	
	public void dropContent() {
		if (content != null) {
			for (ItemStack is : content) {
				if (is != null && is.getType() != Material.AIR) {
					if (is.hasItemMeta() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().contains(Lang.SOULBOUND.toString()))continue;
					this.getBukkitEntity().getWorld().dropItemNaturally(this.getBukkitEntity().getLocation(), is);
				}
			}
			
		}else {
			if (random.nextBoolean()) {
				this.getBukkitEntity().getWorld().dropItemNaturally(this.getBukkitEntity().getLocation(), CustomizedStackItems.ROTTEN_FLESH.getCustomStack().getItemStack());

			}
		}
	}
	
	public void hook(Player source, EntityZombie zombie) {
		source.setVelocity(source.getLocation().getDirection().multiply(0.4));
	}
	
}
