package com.NBK.MineZ.util;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;

public class Title {

    public static void showSubTitle(final Player player, final String subTitle, final double fadeIn, final double stay, final double fadeOut) {
        final IChatBaseComponent subTitleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subTitle + "\"}");
        final PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subTitleComponent);
        final PacketPlayOutTitle timesPacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, (IChatBaseComponent)null, (int)Math.round(fadeIn * 20.0), (int)Math.round(stay * 20.0), (int)Math.round(fadeOut * 20.0));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(timesPacket);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(subTitlePacket);
    }
    
    public static void showTitle(final Player player, final String title, final double fadeIn, final double stay, final double fadeOut) {
        final IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        final PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleComponent);
        final PacketPlayOutTitle timesPacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, (IChatBaseComponent)null, (int)Math.round(fadeIn * 20.0), (int)Math.round(stay * 20.0), (int)Math.round(fadeOut * 20.0));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(timesPacket);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(titlePacket);
    }
	
}
