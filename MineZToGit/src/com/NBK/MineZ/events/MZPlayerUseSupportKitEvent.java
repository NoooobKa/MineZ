package com.NBK.MineZ.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.NBK.MineZ.Items.SupportKit;

public class MZPlayerUseSupportKitEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private Player who;
	private Player whom;
	private SupportKitAction a;
	private SupportKit kit;
	private boolean isCancelled;
	
	public MZPlayerUseSupportKitEvent(Player who, Player whom, SupportKitAction a, SupportKit kit) {
		this.who = who;
		this.whom = whom;
		this.a = a;
		this.kit = kit;
	}
	
	public Player getDoctor() {
		return who;
	}
	
	public Player getPacient() {
		return whom;
	}
	
	public SupportKitAction getAction() {
		return a;
	}
	
	public SupportKit getKit() {
		return kit;
	}
	
	@Override
	public boolean isCancelled() {
		return isCancelled;
	}
	
	@Override
	public void setCancelled(boolean b) {
		this.isCancelled = b;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
}
