package me.lafive.apollo.check.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lafive.apollo.Apollo;
import me.lafive.apollo.config.ApolloConfig;
import net.md_5.bungee.api.ChatColor;

public class TabCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender.hasPermission("apollo.toggleautobans"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this commannd.");
		} else {
			ApolloConfig config = Apollo.getInstance().getApolloConfig();
			if (!config.isAutobans()) {
				config.setAutobans(true);
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					if (!player.hasPermission("apollo.alerts")) continue;
					
					if (!config.isAlertsDisabled(player.getUniqueId())) {
						
						player.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") Apollo Autobans have been enabled by " + sender.getName() + ".");
						
					}
					
				}
				
			} else {
				config.setAutobans(false);
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					if (!player.hasPermission("apollo.alerts")) continue;
					
					if (!config.isAlertsDisabled(player.getUniqueId())) {
						
						player.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") Apollo Autobans have been disabled by " + sender.getName() + ".");
						
					}
					
				}
			}
		}
		return false;
	}

}
