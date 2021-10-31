package me.lafive.apollo.check.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lafive.apollo.Apollo;

public class AlertsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!sender.hasPermission("apollo.alerts")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
		} else {
			if (!(sender instanceof Player)) {
				sender.sendMessage("You must be a PLAYER to use this command!");
			} else {
				Player player = (Player) sender;
				if (Apollo.getInstance().getApolloConfig().isAlertsDisabled(player.getUniqueId())) {
					Apollo.getInstance().getApolloConfig().setAlertsDisabled(player.getUniqueId(), false);
					player.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") Your anticheat alerts are now enabled.");
				} else {
					Apollo.getInstance().getApolloConfig().setAlertsDisabled(player.getUniqueId(), true);
					player.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") Your anticheat alerts are now disabled.");
				}
			}
		}
		
		return false;
	}
	
	

}
