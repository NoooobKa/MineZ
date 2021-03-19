package com.NBK.MineZ.game;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.NBK.MineZ.Items.Loadout;
import com.NBK.MineZ.combat.DeathMessages;
import com.NBK.MineZ.events.MZPlayerSpawnEvent;
import com.NBK.MineZ.events.MZPlayerUseSupportKitEvent;
import com.NBK.MineZ.events.SupportKitAction;
import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.main.Config;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.mobs.CustomZombie;
import com.NBK.MineZ.mobs.EntityMap;
import com.NBK.MineZ.npc.LivingNPC;
import com.NBK.MineZ.util.Util;
import com.NBK.MineZ.world.Area;
import com.NBK.MineZ.world.AreasManager;

public class GameListener implements Listener{

    private Plugin p;
    private Random r;
    
    public GameListener(final Plugin p) {
        this.p = p;
        this.r = new Random();
        Bukkit.getPluginManager().registerEvents((Listener)this, p);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onHeal(final MZPlayerUseSupportKitEvent e) {
        if (!e.isCancelled() && e.getAction() == SupportKitAction.SHIFT_LEFT_CLICK) {
            e.getKit().setTotalHealse(e.getKit().getTotalHeals() + 1);
        }
    }
    
    @EventHandler
    public void onSpawn(final MZPlayerSpawnEvent e) {
        final Player p = e.getPlayer().getPlayer();
        p.getInventory().clear();
        p.getEquipment().clear();
        e.getPlayer().getThirstController().startThirst();
        e.getPlayer().setLoadout(Loadout.SPAWN_DEFAULT);
    }
    
    @EventHandler
    public void onHeal(final EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player && e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onFoodChange(final FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            final MZPlayer mzp = MZPlayer.getPlayer((Player)e.getEntity());
            if (!mzp.isLiving()) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals(Lang.SPAWN_SELECTOR_NAME.toString())) {
            e.getPlayer().openInventory((Inventory)MZPlayer.getPlayer(e.getPlayer().getUniqueId()).getSpawnGUI().getInvs().get(0));
        }
    }
    
    @EventHandler
    public void onCreatureSpawn(final CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.JOCKEY || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.MOUNT) {
            e.setCancelled(true);
            return;
        }
        final Collection<Area> areas = AreasManager.getManager().getAreasByLocation(e.getLocation());
        if (areas.size() > 0) {
            for (final Area area : areas) {
                if (!area.isCanMobsSpawn()) {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityDeath(final EntityDeathEvent e) {
        e.setDroppedExp(0);
        e.getDrops().clear();
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        final CustomZombie cz = (CustomZombie)EntityMap.ZOMBIE.spawn(e.getEntity().getLocation());
        cz.mutation(e.getEntity());
        e.setDroppedExp(0);
        e.setDeathMessage((String)null);
        e.getDrops().clear();
        if (!(e.getEntity() instanceof LivingNPC)) {
            e.getEntity().spigot().respawn();
        }
        final MZPlayer p = MZPlayer.getPlayer(e.getEntity());
        p.setLiving(false);
        p.getThirstController().stopThirst();
        p.getBleadingController().stopBleading();
        p.getInfectionConroller().stopPoisoning();
    }
    
    @EventHandler
    public void onDamage(final EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.PLAYER && !MZPlayer.getPlayer((Player)e.getEntity()).isLiving()) {
            e.setCancelled(true);
            if (e.getEntity().getLocation().getY() < 0.0) {
                e.getEntity().teleport(e.getEntity().getWorld().getSpawnLocation());
            }
        }
    }
    
    @EventHandler
    public void onEntityDamageEntity(final EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() == EntityType.PLAYER && !MZPlayer.getPlayer((Player)e.getDamager()).isLiving()) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntityType() == EntityType.PLAYER) {
            final MZPlayer p = MZPlayer.getPlayer((Player)e.getEntity());
            final Collection<Area> areas = AreasManager.getManager().getAreasByLocation(e.getEntity().getLocation());
            if (areas.size() > 0) {
                for (final Area area : areas) {
                    if (!area.isPvp()) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            if (p.isLiving() && (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE || e.getCause() == EntityDamageEvent.DamageCause.FALL)) {
                if (this.r.nextDouble() < Config.BLEEDING_CHANCE.toDouble()) {
                    p.getBleadingController().startBleading();
                }
                if (e.getDamager().getType() == EntityType.ZOMBIE && this.r.nextDouble() < Config.INFECTION_CHANCE.toDouble()) {
                    p.getInfectionConroller().startPoisoning();
                }
            }
        }
    }
    
    @EventHandler
    public void onDrop(final PlayerDropItemEvent e) {
        if (!MZPlayer.getPlayer(e.getPlayer()).isLiving()) {
            e.setCancelled(true);
            return;
        }
        final ItemStack is = e.getItemDrop().getItemStack();
        if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
            if (is.getItemMeta().getLore().contains(Lang.SOULBOUND.toString())) {
                e.getItemDrop().remove();
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_BREAK, 1.0f, 1.4f);
            }
            if (is.getItemMeta().getLore().contains(Lang.UNDROPPABLE.toString())) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPickUp(final PlayerPickupItemEvent e) {
        if (!MZPlayer.getPlayer(e.getPlayer()).isLiving()) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onCombust(final EntityCombustEvent e) {
        if (e.getEntityType() == EntityType.ZOMBIE) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onInvClose(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof Chest) {
            final ItemStack[] content = e.getView().getTopInventory().getContents();
            for (int i = 0; i < content.length; ++i) {
                if (content[i] != null) {
                    return;
                }
            }
            ((Chest)e.getInventory().getHolder()).getBlock().setType(Material.AIR);
        }
    }
    
    @EventHandler
    public void entityDeathMessage(final EntityDeathEvent e) {
        if (e.getEntityType() == EntityType.PLAYER && !(e.getEntity() instanceof LivingNPC)) {
            final Player killed = (Player)e.getEntity();
            final EntityDamageEvent ede = killed.getLastDamageCause();
            if (ede.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                final Entity killer = MZPlayer.getPlayer(killed).getLastDamager();
                if (killer instanceof Player) {
                    Util.broadcastMessage(DeathMessages.PLAYER_KILL_PLAYER.getRandom().replace("{KILLED}", killed.getName()).replace("{KILLER}", killer.getName()).replace("{ITEM_NAME}", Util.getItemName(((Player)killer).getItemInHand())), killed.getLocation(), 100, false);
                    return;
                }
                if (killer instanceof Zombie) {
                    Util.broadcastMessage(DeathMessages.ZOMBEE_KILL_PLAYER.getRandom().replace("{KILLED}", killed.getName()), killed.getLocation(), 100, false);
                }
            }
        }
    }
	
}
