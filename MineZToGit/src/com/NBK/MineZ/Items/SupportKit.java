package com.NBK.MineZ.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.NBK.MineZ.events.MZPlayerUseSupportKitEvent;
import com.NBK.MineZ.events.SupportKitAction;
import com.NBK.MineZ.game.mzplayer.MZPlayer;
import com.NBK.MineZ.main.Config;
import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.util.CustomStack;
import com.NBK.MineZ.util.Util;

/* SupportKit manager class*/
public class SupportKit{

	private ItemStack kit;
	
	public SupportKit(ItemStack kit) {
		this.kit = kit;
	}
	
	public int getBandages() {
		return getElementAmount(EnumKitSupportElement.BANDAGE);
	}
	
	public void setBandages(int amount) {
		setElementAmount(EnumKitSupportElement.BANDAGE, amount);
	}
	
	public int getHealingOintments() {
		return getElementAmount(EnumKitSupportElement.HEALING_OINTMENT);
	}

	public void setHealing(int amount) {
		setElementAmount(EnumKitSupportElement.HEALING_OINTMENT, amount);
	}
	
	public int getAntibiotics() {
		return getElementAmount(EnumKitSupportElement.ANTIBIOTIC);
	}
	
	public void setAntibiotics(int amount) {
		setElementAmount(EnumKitSupportElement.ANTIBIOTIC, amount);
	}
	
	public int getStimulants() {
		return getElementAmount(EnumKitSupportElement.STIMULANT);
	}
	
	public void setStimulants(int amount) {
		setElementAmount(EnumKitSupportElement.STIMULANT, amount);
	}
	
	public int getRevitalizers() {
		return getElementAmount(EnumKitSupportElement.REVITALIZER);
	}
	
	public void setRevitalizers(int amount) {
		setElementAmount(EnumKitSupportElement.REVITALIZER, amount);
	}
	
	public int getAloes() {
		return getElementAmount(EnumKitSupportElement.ALOE);
	}
	
	public void setAloes(int amount) {
		setElementAmount(EnumKitSupportElement.ALOE, amount);
	}
	
	public int getTotalHeals() {
		return Integer.valueOf(ChatColor.stripColor(kit.getItemMeta().getLore().get(EnumKitSupportElement.TOTAL_HEALS.getIndex())).split("\\s")[2]);
	}
	
	public void setEnabled(EnumKitSupportElement e, boolean b) {
		ItemMeta meta = kit.getItemMeta();
		List<String> lore = meta.getLore();
		String newString = ChatColor.WHITE + ChatColor.stripColor(lore.get(e.getIndex())).replaceFirst(isEnabled(e) ? Lang.ENABLED.toString() : Lang.DISABLED.toString(), b ? ChatColor.GREEN + Lang.ENABLED.toString() : ChatColor.RED + Lang.DISABLED.toString());
		lore.set(e.getIndex(), newString);
		meta.setLore(lore);
		kit.setItemMeta(meta);
	}
	
	public boolean isEnabled(EnumKitSupportElement e) {
		String[] s = kit.getItemMeta().getLore().get(e.getIndex()).split("\\s");
		return s[s.length - 1].contains(Lang.ENABLED.toString());
	}
	
	public void setTotalHealse(int heals) {
		ItemMeta meta = kit.getItemMeta();
		List<String> lore = meta.getLore();
		String newString = lore.get(EnumKitSupportElement.TOTAL_HEALS.getIndex()).replace(String.valueOf(getTotalHeals()), String.valueOf(heals));
		lore.set(EnumKitSupportElement.TOTAL_HEALS.getIndex(), newString);
		meta.setLore(lore);
		kit.setItemMeta(meta);
	}
	
	private int getElementAmount(EnumKitSupportElement e) {
		return Integer.valueOf(ChatColor.stripColor(kit.getItemMeta().getLore().get(e.getIndex())).split("\\s")[0]);
	}
	
	private void setElementAmount(EnumKitSupportElement e, int amount) {
		ItemMeta meta = kit.getItemMeta();
		List<String> lore = meta.getLore();
		String newString = lore.get(e.getIndex()).replaceFirst(String.valueOf(getElementAmount(e)), String.valueOf(amount));
		lore.set(e.getIndex(), newString);
		meta.setLore(lore);
		kit.setItemMeta(meta);
	}
	
	public Inventory getGUI() {
		Inventory inv = Bukkit.createInventory(null, 18, Lang.SUPPORT_KIT_NAME.toString());
		String s = "§fClick to add more to the kit";
		inv.setItem(0, new CustomStack(Material.PAPER).setName(Lang.BANDAGE.toString()).setAmount(getBandages() > 0 ? getBandages() : 1).
				addStringInLore("§fAmount: §6" + getBandages() + " §f/ 10").
				addStringInLore("§7Bandaging a player will").
				addStringInLore("§7give a §6" + EnumKitSupportElement.BANDAGE.getSecToCD() + " §7seconds cooldown").
				addStringInLore(s).getItemStack());
		inv.setItem(2, new CustomStack(Material.INK_SACK).setDurablity((short) 1).setName(Lang.HEALING_OINTMENT.toString()).setAmount(getHealingOintments() > 0 ? getHealingOintments() : 1).
				addStringInLore("§7Boost healing recovery and stop bleeding").
				addStringInLore("§fAmount: §6" + getHealingOintments() + " §f/ 10").
				addStringInLore("§7This will add §6" + EnumKitSupportElement.HEALING_OINTMENT.getSecToCD() + " §7seconds to").
				addStringInLore("§7the player's healing cooldown").
				addStringInLore(s).getItemStack());
		inv.setItem(3, new CustomStack(Material.INK_SACK).setDurablity((short) 10).setName(Lang.ANTIBIOTIC.toString()).setAmount(getAntibiotics() > 0 ? getAntibiotics() : 1).
				addStringInLore("§7Cure infection").
				addStringInLore("§fAmount: §6" + getAntibiotics() + " §f/ 10").
				addStringInLore("§7This will add §6" + EnumKitSupportElement.ANTIBIOTIC.getSecToCD() + " §7seconds to").
				addStringInLore("§7the player's healing cooldown").
				addStringInLore(s).getItemStack());
		inv.setItem(4, new CustomStack(Material.INK_SACK).setDurablity((short) 12).setName(Lang.STIMULANT.toString()).setAmount(getStimulants() > 0 ? getStimulants() : 1).
				addStringInLore("§7Give a speed boost for §6" + EnumKitSupportElement.STIMULANT.getAbilityDuration() + " §7seconds").
				addStringInLore("§fAmount: §6" + getStimulants() + " §f/ 10").
				addStringInLore("§7This will add §6" + EnumKitSupportElement.STIMULANT.getSecToCD() + " §7seconds to").
				addStringInLore("§7the player's healing cooldown").
				addStringInLore(s).getItemStack());
		inv.setItem(5, new CustomStack(Material.INK_SACK).setDurablity((short) 11).setName(Lang.REVITALIZER.toString()).setAmount(getRevitalizers() > 0 ? getRevitalizers() : 1).
				addStringInLore("§7Give an extra 2 absorbation hearts for §6" + EnumKitSupportElement.REVITALIZER.getAbilityDuration() + " §7seconds").
				addStringInLore("§fAmount: §6" + getRevitalizers() + " §f/ 10").
				addStringInLore("§7This will add §6" + EnumKitSupportElement.REVITALIZER.getSecToCD() + " §7seconds to").
				addStringInLore("§7the player's healing cooldown").
				addStringInLore(s).getItemStack());
		inv.setItem(6, new CustomStack(Material.INK_SACK).setDurablity((short) 14).setName(Lang.ALOE.toString()).setAmount(getAloes() > 0 ? getAloes() : 1).
				addStringInLore("§7Give fire resistance for §6" + EnumKitSupportElement.ALOE.getAbilityDuration() + " §7seconds").
				addStringInLore("§fAmount: §6" + getAloes() + " §f/ 10").
				addStringInLore("§7This will add §6" + EnumKitSupportElement.ALOE.getSecToCD() + " §7seconds to").
				addStringInLore("§7the player's healing cooldown").
				addStringInLore(s).getItemStack());
		inv.setItem(11, isEnabled(EnumKitSupportElement.HEALING_OINTMENT) ? new CustomStack(Material.WOOL).setDurablity((short) 5).setName(ChatColor.GREEN + Lang.ENABLED.toString()).getItemStack() : new CustomStack(Material.WOOL).setDurablity((short) 14).setName(ChatColor.RED + Lang.DISABLED.toString()).getItemStack());
		inv.setItem(12, isEnabled(EnumKitSupportElement.ANTIBIOTIC) ? new CustomStack(Material.WOOL).setDurablity((short) 5).setName(ChatColor.GREEN + Lang.ENABLED.toString()).getItemStack() : new CustomStack(Material.WOOL).setDurablity((short) 14).setName(ChatColor.RED + Lang.DISABLED.toString()).getItemStack());
		inv.setItem(13, isEnabled(EnumKitSupportElement.STIMULANT) ? new CustomStack(Material.WOOL).setDurablity((short) 5).setName(ChatColor.GREEN + Lang.ENABLED.toString()).getItemStack() : new CustomStack(Material.WOOL).setDurablity((short) 14).setName(ChatColor.RED + Lang.DISABLED.toString()).getItemStack());
		inv.setItem(14, isEnabled(EnumKitSupportElement.REVITALIZER) ? new CustomStack(Material.WOOL).setDurablity((short) 5).setName(ChatColor.GREEN + Lang.ENABLED.toString()).getItemStack() : new CustomStack(Material.WOOL).setDurablity((short) 14).setName(ChatColor.RED + Lang.DISABLED.toString()).getItemStack());
		inv.setItem(15, isEnabled(EnumKitSupportElement.ALOE) ? new CustomStack(Material.WOOL).setDurablity((short) 5).setName(ChatColor.GREEN + Lang.ENABLED.toString()).getItemStack() : new CustomStack(Material.WOOL).setDurablity((short) 14).setName(ChatColor.RED + Lang.DISABLED.toString()).getItemStack());
		return inv;
	}
	
	public void use(Player who, Player whom) {
		if (getBandages() > 0) {
			int time = EnumKitSupportElement.BANDAGE.getSecToCD();
			List<PotionEffect> el = new ArrayList<>();
			MZPlayer mzwho = MZPlayer.getPlayer(who);
			if (mzwho.getBleadingController().isBleading()) {
				mzwho.getBleadingController().stopBleading();
			}
			
			if (isEnabled(EnumKitSupportElement.HEALING_OINTMENT)) {
				if (getHealingOintments() > 0) {
					time += EnumKitSupportElement.HEALING_OINTMENT.getSecToCD();
					setHealing(getHealingOintments() - 1);
					if (!whom.hasPotionEffect(PotionEffectType.REGENERATION)) {
						whom.removePotionEffect(PotionEffectType.REGENERATION);
					}
					el.add(new PotionEffect(PotionEffectType.REGENERATION, 120, 1));
					setHealing(getHealingOintments() - 1);
				}else {
					el.add(new PotionEffect(PotionEffectType.REGENERATION, 120, 0));
				}
			}else {
				el.add(new PotionEffect(PotionEffectType.REGENERATION, 120, 0));
			}
			
			if (isEnabled(EnumKitSupportElement.ANTIBIOTIC)) {
				if (getAntibiotics() > 0) {
					time += EnumKitSupportElement.ANTIBIOTIC.getSecToCD();
					setAntibiotics(getAntibiotics() - 1);
					MZPlayer mzp = MZPlayer.getPlayer(whom);
					if (mzp.getInfectionConroller().isPoisoning()) {
						mzp.getInfectionConroller().stopPoisoning();
					}
				}
			}
			
			if (isEnabled(EnumKitSupportElement.STIMULANT)) {
				if (getStimulants() > 0) {
					time += EnumKitSupportElement.STIMULANT.getSecToCD();
					setStimulants(getStimulants() - 1);
					if (whom.hasPotionEffect(PotionEffectType.SPEED)) {
						whom.removePotionEffect(PotionEffectType.SPEED);
					}
					el.add(new PotionEffect(PotionEffectType.SPEED, EnumKitSupportElement.STIMULANT.getAbilityDuration() * 20, 0));
				}
			}
			
			if (isEnabled(EnumKitSupportElement.REVITALIZER)) {
				if (getRevitalizers() > 0) {
					time += EnumKitSupportElement.REVITALIZER.getSecToCD();
					setRevitalizers(getRevitalizers() - 1);
					if (whom.hasPotionEffect(PotionEffectType.ABSORPTION)) {
						whom.removePotionEffect(PotionEffectType.ABSORPTION);
					}
					el.add(new PotionEffect(PotionEffectType.ABSORPTION, EnumKitSupportElement.REVITALIZER.getAbilityDuration() * 20, 0));
				}
			}
			
			if (isEnabled(EnumKitSupportElement.ALOE)) {
				if (getAloes() > 0) {
					time += EnumKitSupportElement.ALOE.getSecToCD();
					setAloes(getAloes() - 1);
					if (whom.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
						whom.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
					}
					el.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, EnumKitSupportElement.ALOE.getAbilityDuration() * 20, 0));
				}
			}
			whom.addPotionEffects(el);
			mzwho.setNextHealAccesTime(System.currentTimeMillis() + time * 1000);
		}
	}
	
	public void useBandage(Player p) {
		if (getBandages() > 0) {
			MZPlayer mzp = MZPlayer.getPlayer(p);
			if (mzp.getBleadingController().isBleading()) {
				mzp.getBleadingController().stopBleading();
			}
			if (p.getHealth() < p.getMaxHealth() - 1) {
				p.setHealth(p.getHealth() + 1);
			}else {
				p.setHealth(p.getMaxHealth());
			}
			setBandages(getBandages() - 1);
		}
	}
	
	public static boolean isSupportKit(ItemStack kit) {
		return kit != null && kit.hasItemMeta() && kit.getItemMeta().hasDisplayName() && kit.getItemMeta().getDisplayName().equals(Lang.SUPPORT_KIT_NAME.toString());
	}
	
	public static void registerListener(Plugin p) {
		Bukkit.getPluginManager().registerEvents(new SupportKitListener(), p);
	}
	
	private static class SupportKitListener implements Listener{
		
		@EventHandler
		public void sad(PlayerInteractEvent e) {
			if (e.getItem() != null && e.getItem().getType() == Material.SHEARS && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals(Lang.SUPPORT_KIT_NAME.toString())) {
				if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					SupportKit kit = new SupportKit(e.getItem());
					if (!e.getPlayer().isSneaking()) {
						MZPlayerUseSupportKitEvent event = new MZPlayerUseSupportKitEvent(e.getPlayer(), null, SupportKitAction.RIGHT_CLICK, kit);
						Bukkit.getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							kit.useBandage(e.getPlayer());
						}
					}else {
						MZPlayerUseSupportKitEvent event = new MZPlayerUseSupportKitEvent(e.getPlayer(), null, SupportKitAction.SHIFT_RIGHT_CLICK, kit);
						Bukkit.getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							e.getPlayer().openInventory(kit.getGUI());
						}
					}
				}
			}
		}
		
		@EventHandler(ignoreCancelled = true)
		public void onDamage(EntityDamageByEntityEvent e) {
			if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
				Player d = (Player) e.getDamager();
				if (isSupportKit(d.getItemInHand())) {
					e.setCancelled(true);
					SupportKit kit = new SupportKit(d.getItemInHand());
					Player p = (Player) e.getEntity();
					if (!d.isSneaking()) {
						MZPlayerUseSupportKitEvent event = new MZPlayerUseSupportKitEvent(d, p, SupportKitAction.LEFT_CLICK, kit);
						Bukkit.getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							int time = (int) (MZPlayer.getPlayer(p).getNextHealAccesTime() - Long.valueOf(System.currentTimeMillis())/1000);
							int timeToNextHeal = time > 0 ? time : 0;
							String text = Lang.CHECK_PLAYER_HEAL_TIMING_TEXT.toString().replace("{PLAYER_NAME}", ChatColor.GOLD + p.getName() + ChatColor.WHITE).replace("{HEAL_TIME}", ChatColor.GOLD + String.valueOf(timeToNextHeal) + ChatColor.WHITE);
							d.sendMessage(text);
						}
					}else {
						MZPlayerUseSupportKitEvent event = new MZPlayerUseSupportKitEvent(d, p, SupportKitAction.SHIFT_LEFT_CLICK, kit);
						Bukkit.getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							if (MZPlayer.getPlayer(p).getNextHealAccesTime() < System.currentTimeMillis()) {
								kit.use(d, p);
							}else {
								int time = (int) (MZPlayer.getPlayer(p).getNextHealAccesTime() - Long.valueOf(System.currentTimeMillis())/1000);
								String text = Lang.CHECK_PLAYER_HEAL_TIMING_TEXT.toString().replace("{PLAYER_NAME}", ChatColor.GOLD + p.getName() + ChatColor.WHITE).replace("{HEAL_TIME}", ChatColor.GOLD + String.valueOf(time) + ChatColor.WHITE);
								d.sendMessage(text);
							}
						}
					}
				}
			}
		}
		
		@EventHandler
		public void onInventory(InventoryClickEvent e) {
			if (e.getInventory() != null && e.getInventory().getName() != null && e.getInventory().getName().equals(Lang.SUPPORT_KIT_NAME.toString())) {
				e.setCancelled(true);
				if (e.getWhoClicked().getItemInHand() == null || !e.getWhoClicked().getItemInHand().hasItemMeta() || !e.getWhoClicked().getItemInHand().getItemMeta().hasDisplayName() || !e.getWhoClicked().getItemInHand().getItemMeta().getDisplayName().equals(Lang.SUPPORT_KIT_NAME.toString())) {
					e.getWhoClicked().closeInventory();
					return;
				}
				if (e.getClickedInventory().getName() != null && e.getClickedInventory().getName().equals(Lang.SUPPORT_KIT_NAME.toString())){
					if (e.getCurrentItem().getType() == Material.WOOL) {
						SupportKit kit = new SupportKit(e.getWhoClicked().getItemInHand());
						ItemStack current = e.getCurrentItem();
						switch (e.getSlot()) {
						case 11:
							kit.setEnabled(EnumKitSupportElement.HEALING_OINTMENT, current.getDurability() == 5 ? false : true);
							e.setCurrentItem(current.getDurability() == 5 ? getDisabledWool() : getEnabledWool());
							break;
						case 12:
							kit.setEnabled(EnumKitSupportElement.ANTIBIOTIC, current.getDurability() == 5 ? false : true);
							e.setCurrentItem(current.getDurability() == 5 ? getDisabledWool() : getEnabledWool());
							break;
						case 13:
							kit.setEnabled(EnumKitSupportElement.STIMULANT, current.getDurability() == 5 ? false : true);
							e.setCurrentItem(current.getDurability() == 5 ? getDisabledWool() : getEnabledWool());
							break;
						case 14:
							kit.setEnabled(EnumKitSupportElement.REVITALIZER, current.getDurability() == 5 ? false : true);
							e.setCurrentItem(current.getDurability() == 5 ? getDisabledWool() : getEnabledWool());
							break;
						case 15:
							kit.setEnabled(EnumKitSupportElement.ALOE, current.getDurability() == 5 ? false : true);
							e.setCurrentItem(current.getDurability() == 5 ? getDisabledWool() : getEnabledWool());
							break;
						default:
							break;
						}
					}else {
						if (e.getCurrentItem().getType() == Material.INK_SACK) {
							SupportKit kit = new SupportKit(e.getWhoClicked().getItemInHand());
							short d = e.getCurrentItem().getDurability();
							switch (d) {
							case 1:
								suckInElements((Player) e.getWhoClicked(), kit, EnumKitSupportElement.HEALING_OINTMENT);
								break;
							case 10:
								suckInElements((Player) e.getWhoClicked(), kit, EnumKitSupportElement.ANTIBIOTIC);
								break;
							case 11:
								suckInElements((Player) e.getWhoClicked(), kit, EnumKitSupportElement.REVITALIZER);
								break;
							case 12:
								suckInElements((Player) e.getWhoClicked(), kit, EnumKitSupportElement.STIMULANT);
								break;
							case 14:
								suckInElements((Player) e.getWhoClicked(), kit, EnumKitSupportElement.ALOE);
								break;
							default:
								break;
							}
						}else {
							if (e.getCurrentItem().getType() == Material.PAPER) {
								SupportKit kit = new SupportKit(e.getWhoClicked().getItemInHand());
								suckInElements((Player) e.getWhoClicked(), kit, EnumKitSupportElement.BANDAGE);
							}
						}
					}
				}
			}
		}
		
		private void suckInElements(Player p, SupportKit kit, EnumKitSupportElement e) {
			if (e.getCustomStack() != null && kit.getElementAmount(e) < Config.SUPPORT_KIT_MAX_ELEMENTS.toInt()) {
				Map<Integer, ItemStack> map = Util.allSimilarWithoutMeta(e.getCustomStack().getItemStack(), p.getInventory());
				if (map.size() > 0) {
					int slot = (int) map.keySet().toArray()[0];
					ItemStack item = map.get(slot);
					if (item.getAmount() > 0) {
						if (item.getAmount() == 1) {
							kit.setElementAmount(e, kit.getElementAmount(e) + 1);
							p.getInventory().setItem(slot, null);
						}else {
							kit.setElementAmount(e, kit.getElementAmount(e) + 1);
							item.setAmount(item.getAmount() - 1);
							p.getInventory().setItem(slot, item);
						}
					}
				}
				p.openInventory(kit.getGUI());
			}
		}
		
		private ItemStack getEnabledWool() {
			return new CustomStack(Material.WOOL).setDurablity((short) 5).setName(ChatColor.GREEN + Lang.ENABLED.toString()).getItemStack();
		}
		
		private ItemStack getDisabledWool() {
			return new CustomStack(Material.WOOL).setDurablity((short) 14).setName(ChatColor.RED + Lang.DISABLED.toString()).getItemStack();
		}
	}
	
}
