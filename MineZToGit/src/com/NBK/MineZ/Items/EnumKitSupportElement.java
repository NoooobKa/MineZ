package com.NBK.MineZ.Items;

import org.bukkit.Material;

import com.NBK.MineZ.util.CustomStack;

public enum EnumKitSupportElement {

	BANDAGE(0, 60, -1, new CustomStack(Material.PAPER)),
	HEALING_OINTMENT(1, 15, -1, new CustomStack(Material.INK_SACK).setDurablity((short)1)),
	ANTIBIOTIC(2, 15, -1, new CustomStack(Material.INK_SACK).setDurablity((short)10)),
	STIMULANT(3, 30, 10, new CustomStack(Material.INK_SACK).setDurablity((short)12)),
	REVITALIZER(4, 30, 30, new CustomStack(Material.INK_SACK).setDurablity((short)11)),
	ALOE(5, 30, 20, new CustomStack(Material.INK_SACK).setDurablity((short)14)),
	TOTAL_HEALS(7, -1, -1, null);
	
	private int index;
	private int secToCD;
	private int abilityDuration;
	private CustomStack cs;
	
	private EnumKitSupportElement(final int index, final int secToCD, final int abilityDuration, final CustomStack cs) {
		this.index = index;
		this.secToCD = secToCD;
		this.abilityDuration = abilityDuration;
		this.cs = cs;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getSecToCD() {
		return secToCD;
	}
	
	public int getAbilityDuration() {
		return abilityDuration;
	}
	
	public CustomStack getCustomStack() {
		return cs;
	}
	
}
