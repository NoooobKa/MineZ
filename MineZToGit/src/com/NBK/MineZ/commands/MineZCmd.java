package com.NBK.MineZ.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.NBK.MineZ.Items.AreaHelper;
import com.NBK.MineZ.main.MainGUI;
import com.NBK.MineZ.util.Util;
import com.NBK.MineZ.world.Area;
import com.NBK.MineZ.world.AreasManager;

public class MineZCmd implements CommandExecutor{

	public MineZCmd(JavaPlugin p) {
		p.getCommand("MineZ").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			if (p.isOp()) {
				if (args.length == 0) {
					p.openInventory(new MainGUI().getInv());
					return true;
				}
//				if (args.length == 1) {
//					MineZMain.otc.executeSpigotTimings(s, args);
//				}
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("CreateArea")) {
						if (Util.isAreaHelper(p.getItemInHand())) {
							AreaHelper ah = new AreaHelper(p.getItemInHand());
							Area a = new Area(args[1], ah.getFirstLoc(), ah.getSecondLoc());
							AreasManager.getManager().addArea(a, p);
							return true;
						}else {
							p.sendMessage("§4You must hold the AreaHelper in your hand");
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	
	
}
