package com.NBK.MineZ.Items;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.NBK.MineZ.main.Lang;
import com.NBK.MineZ.util.CustomStack;

public enum CustomItems {

	GRAPPLE {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.FISHING_ROD).setName(Lang.GRANADE.toString());
		}
	},
	SHEARS {
		@Override
		public CustomStack getCustomStack() {
			CustomStack cs = new CustomStack(Material.SHEARS);
			cs.setName(Lang.SUPPORT_KIT_NAME.toString());
			cs.addStringInLore("§f0 / 10 " + Lang.BANDAGE.toString()).
			addStringInLore("§f0 / 10 " + Lang.HEALING_OINTMENT.toString() + ": " + ChatColor.RED + Lang.DISABLED.toString()).
			addStringInLore("§f0 / 10 " + Lang.ANTIBIOTIC.toString() + ": " + ChatColor.RED + Lang.DISABLED.toString()).
			addStringInLore("§f0 / 10 " + Lang.STIMULANT.toString() + ": " + ChatColor.RED + Lang.DISABLED.toString()).
			addStringInLore("§f0 / 10 " + Lang.REVITALIZER.toString() + ": " + ChatColor.RED + Lang.DISABLED.toString()).
			addStringInLore("§f0 / 10 " + Lang.ALOE.toString() + ": " + ChatColor.RED + Lang.DISABLED.toString()).
			addStringInLore(" ").
			addStringInLore("§7Total heals§f: 0").
			addStringInLore(" ").
			addStringInLore("§3Left click§f: §bCheck player's heal timing").
			addStringInLore("§3Shift + Left click§f: §bUse bandage on the player").
			addStringInLore("§3Right click§f: §bUse bandage").
			addStringInLore("§3Shift + Right Click§: §bOpen support kit menu");
			return cs;
		}
	},
	BANDAGE {
		@Override
		public CustomStack getCustomStack() {
			CustomStack cs = new CustomStack(Material.PAPER);
			cs.setName(Lang.BANDAGE.toString()).
			addStringInLore("§3Left click§f: §bUse on the player").
			addStringInLore("§3Right click§f: §bWill heal one heart and stop bleeding");
			return cs;
			
		}
	},
	HEALING {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.INK_SACK).setName(Lang.HEALING_OINTMENT.toString()).setDurablity((short) 1).addStringInLore("§9Healing Kit item that furhter increases the healing effectiveness of bandages.");
		}
	},
	ANTIBIOTIC {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.INK_SACK).setName(Lang.ANTIBIOTIC.toString()).setDurablity((short) 10).addStringInLore("§9Healing Kit item that cures infection when used.");
		}
	},
	STIMULANT {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.INK_SACK).setName(Lang.STIMULANT.toString()).setDurablity((short) 12).addStringInLore("§9Healing Kit item that provides temporary speed.");
		}
	},
	REVITALIZER {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.INK_SACK).setName(Lang.REVITALIZER.toString()).setDurablity((short) 11).addStringInLore("§9Healing Kit item that provides temporary absorption.");
		}
	},
	ALOE {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.INK_SACK).setName(Lang.ALOE.toString()).setDurablity((short) 14).addStringInLore("§9Healing Kit item that provides temporary fire resistance");
		}
	},
	SUGAR {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.SUGAR);
		}
	},
	GRANADE {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.ENDER_PEARL).setName(Lang.GRANADE.toString()).addStringInLore("§9Right Click: Throw, explode on impact.");
		}
	},
	HOE {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.WOOD_HOE);
		}
	},
	SHOVEL {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.IRON_SPADE);
		}
	},
	BUTTON {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.STONE_BUTTON).addStringInLore("§9Place it	near an iron door to open it");
		}
	},
	SNOWBALL {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.SNOW_BALL);
		}
	},
	ANTIDOTE {
		@Override
		public CustomStack getCustomStack() {
			return new CustomStack(Material.MILK_BUCKET).addStringInLore("§9Drink to cure you of disease");
		}
	};
	
	
	
	public abstract CustomStack getCustomStack();
	
}
