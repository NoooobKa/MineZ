package com.NBK.MineZ.combat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class PvpListener implements Listener{
	
	public PvpListener(Plugin p) {
		Bukkit.getPluginManager().registerEvents(this, p);
	}
	
    public double getDamageReduced(Player player) {
        PlayerInventory inv = player.getInventory();
        ItemStack boots = inv.getBoots();
        ItemStack helmet = inv.getHelmet();
        ItemStack chest = inv.getChestplate();
        ItemStack pants = inv.getLeggings();
        int red = 0;
        if (helmet == null)red = red + 0;
        else if(helmet.getType() == Material.LEATHER_HELMET)red += 4; 
        else if(helmet.getType() == Material.GOLD_HELMET)red += 8;
        else if(helmet.getType() == Material.CHAINMAIL_HELMET)red += 8;
        else if(helmet.getType() == Material.IRON_HELMET)red = red += 16; 
        //
        if (boots == null)red = red + 0;
        else if(boots.getType() == Material.LEATHER_BOOTS)red += 4;
        else if(boots.getType() == Material.GOLD_BOOTS)red += 4;
        else if(boots.getType() == Material.CHAINMAIL_BOOTS)red += 8; 
        else if(boots.getType() == Material.IRON_BOOTS)red += 16;
        //
        if (pants == null)red = red + 0;
        else if(pants.getType() == Material.LEATHER_LEGGINGS)red += 8;
        else if(pants.getType() == Material.GOLD_LEGGINGS)red += 12;
        else if(pants.getType() == Material.CHAINMAIL_LEGGINGS)red += 12;
        else if(pants.getType() == Material.IRON_LEGGINGS)red += 16;
        //
        if (chest == null)red = red + 0;
        else if(chest.getType() == Material.LEATHER_CHESTPLATE)red += 12;
        else if(chest.getType() == Material.GOLD_CHESTPLATE)red += 20;
        else if(chest.getType() == Material.CHAINMAIL_CHESTPLATE)red += 16;
        else if(chest.getType() == Material.IRON_CHESTPLATE)red += 20;
        return 1 - red/100.0;
    }
	
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e) {
    	if (e.getEntityType() == EntityType.PLAYER) {
    		e.setDamage(e.getDamage() * getDamageReduced((Player) e.getEntity()));
    	}
    	if (e.getEntity() instanceof Zombie && e.getDamager().getType() == EntityType.PLAYER) {
    		Player p = (Player) e.getDamager();
    		if (p.getItemInHand() != null && p.getItemInHand().getType().name().contains("_AXE")) {
    			for (Entity ent : e.getEntity().getNearbyEntities(2, 2, 2)) {
    				if (ent instanceof Zombie) {
    					((Zombie) ent).damage(e.getDamage(), p);
    				}
    			}
    		}
    	}
    }
    
}
