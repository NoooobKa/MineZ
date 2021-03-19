package com.NBK.MineZ.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.NBK.MineZ.game.mzplayer.MZPlayer;

public class MZPlayerSpawnEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private MZPlayer p;
	private int spawn;
	private boolean isCancelled;
	
	public MZPlayerSpawnEvent(MZPlayer p, int spawn) {
		this.p = p;
		this.spawn = spawn;
	}
	
	public MZPlayer getPlayer() {
		return p;
	}
	
	public int getSpawn() {
		return spawn;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.isCancelled = b;
	}

}
