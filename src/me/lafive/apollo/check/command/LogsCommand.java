package me.lafive.apollo.check.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.lafive.apollo.Apollo;
import net.md_5.bungee.api.ChatColor;

public class LogsCommand implements CommandExecutor {
	
	private Apollo plugin = Apollo.getInstance();
	private int logCount;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender.hasPermission("apollo.logs"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
		} else {
			if (args.length < 1) {
				sender.sendMessage(ChatColor.RED + "Usage: /logs <player>");
			} else if (args.length > 1) {
				sender.sendMessage(ChatColor.RED + "Usage: /logs <player>");
			} else {
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				if (!(target instanceof OfflinePlayer)) {
					sender.sendMessage(ChatColor.RED + "Invalid player!");
				} else {
					UUID UUID = target.getUniqueId();
					String UUIDString = String.valueOf(UUID);
					File targetFile = new File(plugin.getDataFolder() + "/logs/", UUIDString + ".txt");
					if (!targetFile.exists()) {
						logCount = 0;
					} else {
						try {
							BufferedReader reader = new BufferedReader(new FileReader(targetFile));
							logCount = 0;
							while (reader.readLine() != null) logCount++;
							reader.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					
				}
				
				if (logCount == 0) {
					sender.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") " + target.getName() + " has no anticheat logs.");
				} else {
					sender.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") " + target.getName() + " has " + logCount + " anticheat logs.");
				}
				
		  	  }
			}
		}
		return false;
	}	

}







	// sender.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ")");