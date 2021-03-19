package com.NBK.MineZ.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.main.MineZMain;

public class PlayerDataBase implements IData{

	private UUID id;
	private boolean isLiving;
	private boolean isBleading;
	private boolean isInfection;
	private int XP;
	private int kills;
	private int totalKills;
	private int deaths;
	private int careerKilledMobs;
	private int currentKilledMobs;
	private int cured;
	private int careerCured;
	private String serializedStartLoot;
	private String serializedAchievements;
	private MySQL sql;
	
	public PlayerDataBase(UUID id) {
		this.id = id;
		this.sql = MineZMain.INSTANCE.getMySQL();
		checkPlayer(id);
		try {
			PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM minez_players WHERE uuid = ?");
			ps.setString(1, id.toString());
			ResultSet rs = ps.executeQuery();
			rs.next();
			this.isLiving = rs.getBoolean("IsLiving");
			this.isBleading = rs.getBoolean("IsBleading");
			this.isInfection = rs.getBoolean("IsInfection");
			this.XP = rs.getInt("XP");
			this.kills = rs.getInt("Kills");
			this.totalKills = rs.getInt("TotalKills");
			this.deaths = rs.getInt("Deaths");
			this.careerKilledMobs = rs.getInt("TotalKilledMobs");
			this.currentKilledMobs = rs.getInt("KilledMobs");
			this.cured = rs.getInt("Cured");
			this.careerCured = rs.getInt("TotalCured");
			this.serializedAchievements = rs.getString("Achievements");
			this.serializedStartLoot = rs.getString("StartLoot");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public UUID getId() {
		return id;
	}
	
	@Override
	public boolean isLiving() {
		return isLiving;
	}

	@Override
	public void setLiving(boolean b) {
		this.isLiving = b;
		setAsyncronously("IsLiving", b);
	}

	@Override
	public boolean isBleading() {
		return isBleading;
	}

	@Override
	public void setBleading(boolean b) {
		this.isBleading = b;
		setAsyncronously("IsBleading", b);
	}

	@Override
	public boolean isInfection() {
		return isInfection;
	}

	@Override
	public void setInfection(boolean b) {
		this.isInfection = b;
		setAsyncronously("IsInfection", b);
	}

	@Override
	public int getXP() {
		return XP;
	}

	@Override
	public void setXP(int x) {
		this.XP = x;
		setAsyncronously("XP", x);
	}

	@Override
	public int getTotalKills() {
		return totalKills;
	}

	@Override
	public void setTotalKills(int x) {
		this.totalKills = x;
		setAsyncronously("TotalKills", x);
	}
	
	@Override
	public int getKills() {
		return kills;
	}

	@Override
	public void setKills(int x) {
		this.kills = x;
		setAsyncronously("Kills", x);
	}

	@Override
	public int getDeaths() {
		return deaths;
	}

	@Override
	public void setDeaths(int x) {
		this.deaths = x;
		setAsyncronously("Deaths", x);
	}

	@Override
	public int getCareerKilledMobs() {
		return careerKilledMobs;
	}

	@Override
	public void setCareerKilledMobs(int x) {
		this.careerKilledMobs = x;
		setAsyncronously("TotalKilledMobs", x);
	}

	@Override
	public int getCurrentKilledMobs() {
		return currentKilledMobs;
	}

	@Override
	public void setCurrentKilledMobs(int x) {
		this.currentKilledMobs = x;
		setAsyncronously("KilledMobs", x);
	}

	@Override
	public int getCured() {
		return cured;
	}

	@Override
	public void setCured(int x) {
		this.cured = x;
		setAsyncronously("Cured", x);
	}
	
	@Override
	public int getCareerCured() {
		return careerCured;
	}

	@Override
	public void setCarrerCured(int x) {
		this.cured = x;
		setAsyncronously("TotalCured", x);
	}

	@Override
	public String getSerializedStartLoot() {
		return serializedStartLoot;
	}

	@Override
	public void setSerializedStartLoot(String loot) {
		this.serializedStartLoot = loot;
		setAsyncronously("StartLoot", loot);
	}

	@Override
	public String getSerializedAchievements() {
		return serializedAchievements;
	}

	@Override
	public void setSerializedAchievements(String a) {
		this.serializedAchievements = a;
		setAsyncronously("Achievements", a);
	}

	private void setAsyncronously(String field, Object o) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE minez_players SET " + field + " = ? WHERE uuid = ?");
					ps.setObject(1, o);
					ps.setString(2, getId().toString());
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(MineZMain.INSTANCE);
	}
	
	private void checkPlayer(UUID id) {
		try {
			boolean exist = true;
			PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM minez_players WHERE UUID = ?");
			ps.setString(1, id.toString());
			ResultSet rs = ps.executeQuery();
			if (!rs.next())exist = false;
			
			if (!exist) {
				PreparedStatement insert = sql.getConnection().prepareStatement("INSERT INTO minez_players(UUID,IsLiving,IsBleading,IsInfection,XP,TotalKills,Kills,Deaths,KilledMobs,TotalKilledMobs,Cured,TotalCured,Achievements,StartLoot) VALUE(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				insert.setString(1, id.toString());
				insert.setBoolean(2, false);
				insert.setBoolean(3, false);
				insert.setBoolean(4, false);
				insert.setInt(5, 0);
				insert.setInt(6, 0);
				insert.setInt(7, 0);
				insert.setInt(8, 0);
				insert.setInt(9, 0);
				insert.setInt(10, 0);
				insert.setInt(11, 0);
				insert.setInt(12, 0);
				insert.setString(13, "");
				insert.setString(14, "");
				insert.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
