package com.NBK.MineZ.npc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.combat.DeathMessages;
import com.NBK.MineZ.main.MineZMain;
import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.PlayerInventory;
import net.minecraft.server.v1_8_R3.WorldServer;

public class LivingNPC extends EntityPlayer{

	public static LivingNPC create(Player p, int despawnTime) {
		LivingNPC npc = create(p);
		new BukkitRunnable() {
			@Override
			public void run() {
				npc.despawn();
			}
		}.runTaskLater(MineZMain.INSTANCE, 20 * despawnTime);
		return npc;
	}
	
	public static LivingNPC create(Player p) {
		GameProfile gp = ((CraftPlayer)p).getProfile();
		return new LivingNPC(p, ((CraftServer)Bukkit.getServer()).getServer(), ((CraftWorld)p.getWorld()).getHandle(), gp, new PlayerInteractManager(((CraftWorld)p.getWorld()).getHandle()));
	}
	
	public LivingNPC(Player p, MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile,PlayerInteractManager playerinteractmanager) {
		super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
	    PlayerInventory inv = ((CraftPlayer)p).getHandle().inventory;
	    this.inventory.armor = inv.armor;
	    this.inventory.items = inv.items;
	    this.playerConnection = new PlayerConnection(worldserver.getMinecraftServer(), new NetworkManager(EnumProtocolDirection.SERVERBOUND), this);
	    this.bukkitEntity = (CraftEntity)new CraftPlayer((CraftServer)Bukkit.getServer(), this);
	    WorldServer worldServer = (worldserver.getWorld()).getHandle();
	    Location location = p.getLocation();
	    setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	    worldServer.addEntity((Entity)this);
	    checkState();
	    checkFall();
	}

	private void checkState() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (bukkitEntity == null) {
					this.cancel();
					return;
				}
		        final IBlockData data = LivingNPC.this.world.getType(new BlockPosition((net.minecraft.server.v1_8_R3.Entity)LivingNPC.this));
		        final Block block = data.getBlock();
		        if (block.getMaterial() == Material.FIRE) {
		        	LivingNPC.this.setOnFire(15);
		        }
			}
		}.runTaskTimer(MineZMain.INSTANCE, 0, 20);
	}
	
	private void checkFall() {
		new BukkitRunnable() {
			float damage = 0.0f;
			float g = 0.8f;
			@Override
			public void run() {
		        if (!LivingNPC.this.onGround) {
		        	if (locY < 0) {
		        		damageEntity(DamageSource.OUT_OF_WORLD, getMaxHealth());
		        	}
		        	LivingNPC.this.motY = LivingNPC.this.onGround ? Math.max(0.0D, LivingNPC.this.motY) : LivingNPC.this.motY;
		            move(LivingNPC.this.motX, LivingNPC.this.motY, LivingNPC.this.motZ);
		            LivingNPC.this.motX *= 0.800000011920929D;
		            LivingNPC.this.motY *= 0.800000011920929D;
		            LivingNPC.this.motZ *= 0.800000011920929D;
		        	g += 0.1f * Math.ceil((double)g);
		        	if ((int)locY < (int)lastY) {
		        		damage += 1.0f;
		        	}
		        	LivingNPC.this.motY -= 0.1 * Math.ceil(g);
		        }else {
		        	if (damage > 0) {
		        		LivingNPC.this.damageEntity(DamageSource.FALL, damage);
		        		damage = 0;
		        	}
		        	this.cancel();
		        }
			}
		}.runTaskTimer(MineZMain.INSTANCE, 0, 2);
	}
	
    public void die(final DamageSource damageSource) {
        Bukkit.broadcastMessage(DeathMessages.NPC_DEATH.getRandom().replace("{NAME}", this.getName()));
        super.die(damageSource);
    }
	
	public void despawn() {
		this.getBukkitEntity().remove();
	}
	
}
