package com.NBK.MineZ.data;

public interface IData {

	boolean isLiving();
	
	void setLiving(boolean b);
	
	boolean isBleading();
	
	void setBleading(boolean b);
	
	boolean isInfection();
	
	void setInfection(boolean b);
	
	int getXP();
	
	void setXP(int x);
	
	int getTotalKills();
	
	void setTotalKills(int x);
	
	int getKills();
	
	void setKills(int x);
	
	int getDeaths();
	
	void setDeaths(int x);
	
	int getCareerKilledMobs();
	
	void setCareerKilledMobs(int x);
	
	int getCurrentKilledMobs();
	
	void setCurrentKilledMobs(int x);
	
	int getCareerCured();
	
	void setCarrerCured(int x);
	
	int getCured();
	
	void setCured(int x);
	
	String getSerializedStartLoot();
	
	void setSerializedStartLoot(String loot);
	
	String getSerializedAchievements();
	
	void setSerializedAchievements(String a);
}
