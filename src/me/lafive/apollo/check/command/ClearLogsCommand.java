package me.lafive.apollo.check.command;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.lafive.apollo.Apollo;
import net.md_5.bungee.api.ChatColor;

public class ClearLogsCommand implements CommandExecutor {
	
	private Apollo plugin = Apollo.getInstance();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender.hasPermission("apollo.clearlogs"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
		} else {
			if (args.length < 1) {
				sender.sendMessage(ChatColor.RED + "Usage: /clearlogs <player>");
			} else if (args.length > 1) {
				sender.sendMessage(ChatColor.RED + "Usage: /clearlogs <player>");
			} else {
				@SuppressWarnings("deprecation")
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				if (!(target instanceof OfflinePlayer)) {
					sender.sendMessage(ChatColor.RED + "Invalid player!");
				} else {
					String uuid = String.valueOf(target.getUniqueId());
					File targetFile = new File(plugin.getDataFolder() + "/logs/", uuid + ".txt");
					if (!targetFile.exists()) {
						sender.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") " + target.getName() + " has no anticheat logs!");
					} else {
						targetFile.delete();
						sender.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") Anticheat logs cleared for player " + target.getName() + ".");
					}
				}
				
			}
		}
		return false;
	}

}
