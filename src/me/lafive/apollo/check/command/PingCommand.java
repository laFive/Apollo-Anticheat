package me.lafive.apollo.check.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lafive.apollo.Apollo;
import me.lafive.apollo.data.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class PingCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length < 1) {
			if (!(sender instanceof Player)) {
				return true;
			}
			Player player = (Player) sender;
			PlayerData data = Apollo.getInstance().getDataManager().getPlayerData(player);
			
			player.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") Your ping is " + data.getKeepAlivePing() + "ms.");
			return true;
		}
		
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(ChatColor.RED + "Player not found!");
			return true;
		}
		
		PlayerData data = Apollo.getInstance().getDataManager().getPlayerData(target);
		sender.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") " + target.getName() + "'s ping is " + data.getKeepAlivePing() + "ms.");
		return true;
	}

}
