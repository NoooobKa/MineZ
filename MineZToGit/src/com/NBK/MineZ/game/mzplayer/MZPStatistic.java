package com.NBK.MineZ.game.mzplayer;

public abstract class MZPStatistic {
	
	private boolean isLiving;
	private boolean isBleading;
	private boolean isInfection;
	private int XP;
	private int kills;
	private int deaths;
	private int careerKillingMobs;
	private int currentKillingMobs;
	private int careerCured;

	public boolean isLiving() {
		return isLiving;
	}

	public void setLiving(boolean isLiving) {
		this.isLiving = isLiving;
	}

	public boolean isBleading() {
		return isBleading;
	}

	public void setBleading(boolean isBleading) {
		this.isBleading = isBleading;
	}

	public boolean isInfection() {
		return isInfection;
	}

	public void setInfection(boolean isInfection) {
		this.isInfection = isInfection;
	}

	public int getXP() {
		return XP;
	}

	public void setXP(int xP) {
		XP = xP;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getCareerKillingMobs() {
		return careerKillingMobs;
	}

	public void setCareerKillingMobs(int careerKillingMobs) {
		this.careerKillingMobs = careerKillingMobs;
	}

	public int getCurrentKillingMobs() {
		return currentKillingMobs;
	}

	public void setCurrentKillingMobs(int currentKillingMobs) {
		this.currentKillingMobs = currentKillingMobs;
	}

	public int getCareerCured() {
		return careerCured;
	}

	public void setCareerCured(int careerCured) {
		this.careerCured = careerCured;
	}
}
