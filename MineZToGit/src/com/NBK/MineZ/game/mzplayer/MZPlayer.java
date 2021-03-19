package com.NBK.MineZ.game.mzplayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.Items.Loadout;
import com.NBK.MineZ.data.IData;
import com.NBK.MineZ.data.PlayerConfig;
import com.NBK.MineZ.data.PlayerDataBase;
import com.NBK.MineZ.events.MZPlayerSpawnEvent;
import com.NBK.MineZ.game.BleedingController;
import com.NBK.MineZ.game.Board;
import com.NBK.MineZ.game.InfectionController;
import com.NBK.MineZ.game.ThirstController;
import com.NBK.MineZ.main.Config;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.main.MineZMain;
import com.NBK.MineZ.npc.LivingNPC;
import com.NBK.MineZ.util.CustomStack;
import com.NBK.MineZ.util.Util;
import com.NBK.MineZ.world.Area;
import com.NBK.MineZ.world.SpawnPoint;
import com.NBK.MineZ.world.SpawnPointGUI;

import net.minecraft.server.v1_8_R3.EntityPlayer;


public class MZPlayer {

	private static final Map<UUID, MZPlayer> players;
	private final UUID id;
	private final String name;
	private IData stat;
	private BleedingController bleedingController;
	private InfectionController infectionConroller;
	private ThirstController thirstController;
	private MZPAchievements achievements;
	private StartLoot startLoot;
	private Long nextHealAccesTime;
	private Area lastArea;
	private SpawnPointGUI spawnSelector;
	private Board board;
	private Long lastGranade;
	private Long lastSugar;
	private Long lastPlayed;
	private boolean isLogouting;
    private Entity lastDamager;
	
	static {
		players = new HashMap<UUID, MZPlayer>();
	}

	public static MZPlayer getPlayer(final UUID id) {
        if (id != null && MZPlayer.players.get(id) == null) {
            PlayerLoader.loadPlayer(id);
        }
        return MZPlayer.players.get(id);
    }
    
    public static MZPlayer getPlayer(final Player p) {
        if (p != null && p.getUniqueId() != null) {
            return getPlayer(p.getUniqueId());
        }
        return null;
    }
    
    public static Collection<MZPlayer> getPlayers() {
        return MZPlayer.players.values();
    }
    
    public static void registerListener(Plugin p) {
        MZPlayer.players.clear();
        final PlayerLoader l = new PlayerLoader();
        Bukkit.getPluginManager().registerEvents(l, p);
    }
	
	public MZPlayer(final UUID id, String name) {
		this.id = id;
		this.name = name;
		this.stat = Config.USE_MySQL.toBoolean() ? new PlayerDataBase(id) : new PlayerConfig(id);
		this.bleedingController = new BleedingController(id);
		this.infectionConroller = new InfectionController(id);
		this.thirstController = new ThirstController(id);
		this.nextHealAccesTime = Long.MIN_VALUE;
		this.spawnSelector = new SpawnPointGUI(id);
		this.board = new Board(id);
		this.lastGranade = Long.MIN_VALUE;
		this.lastSugar = Long.MIN_VALUE;
		this.lastPlayed = Long.MIN_VALUE;
		this.isLogouting = false;
		new BukkitRunnable() {
			@Override
			public void run() {
				onFourth();
			}
		}.runTaskTimer(MineZMain.INSTANCE, 0, 5);
	}
	
    public boolean isLiving() {
		return getStat().isLiving();
	}

	public void setLiving(boolean isLiving) {
		getStat().setLiving(isLiving);
	}

	public MZPAchievements getAchievements() {
		return achievements;
	}
	
	public StartLoot getStartLoot() {
		return startLoot;
	}

	public void setStartLoot(StartLoot startLoot) {
		this.startLoot = startLoot;
	}

	public Long getNextHealAccesTime() {
		return nextHealAccesTime;
	}

	public void setNextHealAccesTime(Long nextHealAccesTime) {
		this.nextHealAccesTime = nextHealAccesTime;
	}

	public Area getLastArea() {
		return lastArea;
	}

	public void setLastArea(Area lastArea) {
		this.lastArea = lastArea;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public Board getBoard() {
		return board;
	}
	
    public Long getLastGranade() {
		return lastGranade;
	}

	public void setLastGranade(Long lastGranade) {
		this.lastGranade = lastGranade;
	}

	public Long getLastSugar() {
		return lastSugar;
	}

	public void setLastSugar(Long lastSugar) {
		this.lastSugar = lastSugar;
	}

	public Player getPlayer() {
        return Bukkit.getPlayer(this.id);
    }
    
    public boolean isOnline() {
        return this.getPlayer() != null && this.getPlayer().isOnline();
    }
	
    @SuppressWarnings("deprecation")
	public void onFourth() {
    	final Player p = getPlayer();
    	if (p != null) {
    		if (p.isSneaking()) {
    			p.setExp(0.222f);
    		}else {
    			if (p.isSprinting()) {
    				if (p.isOnGround()) {
    					p.setExp(0.888f);
    				}else {
    					p.setExp(0.999f);
    				}
    			}else {
    				p.setExp(0.444f);
    			}
    		}
    	}
    }
    
	public BleedingController getBleadingController() {
		return bleedingController;
	}

	public InfectionController getInfectionConroller() {
		return infectionConroller;
	}

	public IData getStat() {
		return stat;
	}

	public EntityPlayer getHandle() {
		return ((CraftPlayer)getPlayer()).getHandle();
	}
	
	public ThirstController getThirstController() {
		return thirstController;
	}

	public SpawnPointGUI getSpawnGUI() {
		return spawnSelector;
	}
	
	public void spawn(int spawn) {
		if (!isLiving() && SpawnPoint.exists(spawn)) {
			MZPlayerSpawnEvent event = new MZPlayerSpawnEvent(this, -1);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				getPlayer().teleport(SpawnPoint.getSpawn(spawn));
				getPlayer().sendMessage(Lang.SPAWN_TEXT.toString().replace("{SPAWN}", String.valueOf(spawn)));
				setLiving(true);
				getPlayer().playSound(getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
			}
		}
	}
	
	public void spawn(Location l, String message) {
		if (!isLiving()) {
			MZPlayerSpawnEvent event = new MZPlayerSpawnEvent(this, -1);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				getPlayer().teleport(l);
				setLiving(true);
				getPlayer().playSound(getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
				if (message != null) {
					getPlayer().sendMessage(message);
				}
			}
		}
	}
	
	public void setLoadout(Loadout loadout) {
		Player p = getPlayer();
		p.getInventory().setContents(loadout.getInventory());
		p.getInventory().setArmorContents(loadout.getArmor());
	}
	
	public Long getLastPlayed() {
		return lastPlayed;
	}

	public void setLastPlayed(Long lastPlayed) {
		this.lastPlayed = lastPlayed;
	}

	public boolean isLogouting() {
		return isLogouting;
	}

	public void setLogouting(boolean isLogouting) {
		this.isLogouting = isLogouting;
	}

	public Entity getLastDamager() {
		return lastDamager;
	}

	public void setLastDamager(Entity lastDamager) {
		this.lastDamager = lastDamager;
	}

	private static class PlayerLoader implements Listener {
		public PlayerLoader() {
			for (final Player p : Bukkit.getOnlinePlayers()) {
				loadPlayer(p);
			}
		}
		
		@EventHandler
		public void loginCheck(final PlayerLoginEvent e) {
			MZPlayer mzp = MZPlayer.getPlayer(e.getPlayer());
			Long time = System.currentTimeMillis();
			if (mzp != null && mzp.getLastPlayed() + 20000 > time) {
				e.disallow(Result.KICK_OTHER, Lang.LOGIN_DENY_TEXT.toString().replace("{TIME_TO_NEXT_LOGIN}", String.valueOf((mzp.getLastPlayed() + 20000L - time)/1000L)));
			}
		}
		
		@EventHandler(priority = EventPriority.LOWEST)
		public void joinCheck(final PlayerJoinEvent e) {
			final Player p = e.getPlayer();
			if (!this.exists(p)) {
				loadPlayer(p);
			}
			MZPlayer mzp = MZPlayer.getPlayer(p);
			if (mzp.isLiving()) {
				mzp.getThirstController().startThirst();
				if (mzp.getBleadingController().isBleading()) {
					mzp.getBleadingController().startBleading();
				}
				if (mzp.getInfectionConroller().isPoisoning()) {
					mzp.getInfectionConroller().startPoisoning();
				}
				Collection<Player> nearbyPlayers = Util.getNearbyPlayers(p.getLocation(), 48);
				if (nearbyPlayers.size() > 1) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 1));
					for (Player np : nearbyPlayers) {
						if (!p.getName().equals(np.getName()) && MZPlayer.getPlayer(np).isLiving()) {
							np.sendMessage(Lang.SOUNDS_LIKE_SOMEONE_IS_NEARBY.toString());
						}
					}
				}
				Util.removeChests(p.getLocation(), 48);
			}else {
				p.setLevel(20);
				p.setHealth(p.getMaxHealth());
				p.setFoodLevel(20);
				p.teleport(p.getWorld().getSpawnLocation());
				p.getInventory().clear();
				p.getEquipment().clear();
				p.getInventory().setItem(4, new CustomStack(Material.GRASS).setName(Lang.SPAWN_SELECTOR_NAME.toString()).setUndroppable().getItemStack());
			}
			mzp.getBoard().update();
		}
		
		@EventHandler
		public void respawnCheck(final PlayerRespawnEvent e) {
			new BukkitRunnable() {
				@Override
				public void run() {
					e.getPlayer().setLevel(20);
					e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
					MZPlayer.getPlayer(e.getPlayer()).setLoadout(Loadout.LOBBY);
				}
			}.runTask(MineZMain.INSTANCE);
		}
		
		@EventHandler
		public void quitCheck(final PlayerQuitEvent e) {
			final MZPlayer p = getPlayer(e.getPlayer());
			p.getBleadingController().stopBleading();
			p.getInfectionConroller().stopPoisoning();
			p.getThirstController().stopThirst();
			if (p.isLiving()) {
				LivingNPC.create(e.getPlayer(), 15);
			}
			p.setLastPlayed(System.currentTimeMillis());
		}
		
		@EventHandler
		public void kickCheck(final PlayerKickEvent e) {
			final MZPlayer p = getPlayer(e.getPlayer());
			p.getBleadingController().stopBleading();
			p.getInfectionConroller().stopPoisoning();
			p.getThirstController().stopThirst();
			p.setLastPlayed(System.currentTimeMillis());
		}
		
        private static void loadPlayer(final Player p) {
            final MZPlayer player = new MZPlayer(p.getUniqueId(), p.getName());
            MZPlayer.players.put(p.getUniqueId(), player);
        }
		
        private static void loadPlayer(final UUID id) {
            if (id != null) {
                final Player p = Bukkit.getPlayer(id);
                if (p != null) {
                    final MZPlayer player = new MZPlayer(p.getUniqueId(), p.getName());
                    MZPlayer.players.put(p.getUniqueId(), player);
                }
            }
        }
        
        private boolean exists(final Player p) {
            return MZPlayer.players.containsKey(p.getUniqueId());
        }
	}


	
}
