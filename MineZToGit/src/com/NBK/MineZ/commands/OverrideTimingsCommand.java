package com.NBK.MineZ.commands;

import org.bukkit.plugin.*;
import org.spigotmc.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.command.defaults.TimingsCommand;

import java.net.*;
import com.google.gson.*;
import java.io.*;
import java.util.logging.*;

import javax.annotation.Nonnull;

public class OverrideTimingsCommand extends TimingsCommand
{

	public OverrideTimingsCommand(String name) {
		super(name);
		
	}
    
	@Override
	public void executeSpigotTimings(@Nonnull final CommandSender sender, @Nonnull final String[] args) {
		if ("on".equals(args[0])) {
            ((SimplePluginManager)Bukkit.getPluginManager()).useTimings(true);
            CustomTimingsHandler.reload();
            sender.sendMessage("Enabled Timings & Reset");
            return;
        }
        if ("off".equals(args[0])) {
            ((SimplePluginManager)Bukkit.getPluginManager()).useTimings(false);
            sender.sendMessage("Disabled Timings");
            return;
        }
        if (!Bukkit.getPluginManager().useTimings()) {
            sender.sendMessage("Please enable timings by typing /timings on");
            return;
        }
        final boolean paste = "paste".equals(args[0]);
        if ("reset".equals(args[0])) {
            CustomTimingsHandler.reload();
            sender.sendMessage("Timings reset");
        }
        else if ("merged".equals(args[0]) || "report".equals(args[0]) || paste) {
            final long sampleTime = System.nanoTime() - TimingsCommand.timingStart;
            int index = 0;
            final File timingFolder = new File("timings");
            timingFolder.mkdirs();
            File timings = new File(timingFolder, "timings.txt");
            final ByteArrayOutputStream bout = paste ? new ByteArrayOutputStream() : null;
            while (timings.exists()) {
                timings = new File(timingFolder, "timings" + ++index + ".txt");
            }
            PrintStream fileTimings = null;
            try {
                fileTimings = (paste ? new PrintStream(bout) : new PrintStream(timings));
                CustomTimingsHandler.printTimings(fileTimings);
                fileTimings.println("Sample time " + sampleTime + " (" + sampleTime / 1.0E9 + "s)");
                fileTimings.println("<spigotConfig>");
                fileTimings.println(Bukkit.spigot().getConfig().saveToString());
                fileTimings.println("</spigotConfig>");
                if (paste) {
                    new PasteThread(sender, bout).start();
                    return;
                }
                sender.sendMessage("Timings written to " + timings.getPath());
                sender.sendMessage("Paste contents of file into form at http://www.spigotmc.org/go/timings to read results.");
            }
            catch (IOException ex) {
                return;
            }
            finally {
                if (fileTimings != null) {
                    fileTimings.close();
                }
            }
            if (fileTimings != null) {
                fileTimings.close();
            }
        }
	}
	
    private static class PasteThread extends Thread
    {
        private final CommandSender sender;
        private final ByteArrayOutputStream bout;
        
        public PasteThread(final CommandSender sender, final ByteArrayOutputStream bout) {
            super("Timings paste thread");
            this.sender = sender;
            this.bout = bout;
        }
        
        @Override
        public synchronized void start() {
            if (this.sender instanceof RemoteConsoleCommandSender) {
                this.run();
            }
            else {
                super.start();
            }
        }
        
        @Override
        public void run() {
            try {
                final HttpURLConnection con = (HttpURLConnection)new URL("https://timings.spigotmc.org/paste").openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setInstanceFollowRedirects(false);
                final OutputStream out = con.getOutputStream();
                out.write(this.bout.toByteArray());
                out.close();
                final JsonObject location = new Gson().fromJson(new InputStreamReader(con.getInputStream()), JsonObject.class);
                con.getInputStream().close();
                final String pasteID = location.get("key").getAsString();
                this.sender.sendMessage(ChatColor.GREEN + "Timings results can be viewed at https://www.spigotmc.org/go/timings?url=" + pasteID);
            }
            catch (IOException ex) {
                this.sender.sendMessage(ChatColor.RED + "Error pasting timings, check your console for more information");
                Bukkit.getServer().getLogger().log(Level.WARNING, "Could not paste timings", ex);
            }
        }
    }
	
}
