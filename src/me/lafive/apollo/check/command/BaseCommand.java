package me.lafive.apollo.check.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lafive.apollo.Apollo;
import me.lafive.apollo.config.ApolloConfig;


public class BaseCommand implements CommandExecutor {
	
	private Apollo plugin = Apollo.getInstance();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender.hasPermission("Apollo.manage"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
		} else {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------------" + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Apollo V" + plugin.getDescription().getVersion() + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------------");
				sender.sendMessage(ChatColor.GOLD + "/logs " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Show log count for the specified user");
				sender.sendMessage(ChatColor.GOLD + "/alerts " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Toggle receiving of your Apollo alerts");
				sender.sendMessage(ChatColor.GOLD + "/clearlogs " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Clear the specified player's logs");
				sender.sendMessage(ChatColor.GOLD + "/ping " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Check the ping of a player");
				sender.sendMessage(ChatColor.GOLD + "/Apollo " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Show this help menu");
				sender.sendMessage(ChatColor.GOLD + "/Apollo exempt " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Make a player exempt from Apollo checks");
				sender.sendMessage(ChatColor.GOLD + "/Apollo unexempt " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Make an exempt player unexempt from Apollo checks");
				sender.sendMessage(ChatColor.GOLD + "/Apollo restart " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Reload all Apollo checks and data");
			} else if (args.length == 1) {
				if (args[0].toString().equalsIgnoreCase("restart")) {
					long now = System.currentTimeMillis();
					Bukkit.getPluginManager().disablePlugin(plugin);
					Bukkit.getPluginManager().enablePlugin(plugin);
					long now2 = ( System.currentTimeMillis() - now );
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						if (!player.hasPermission("apollo.alerts")) continue;
						if (!Apollo.getInstance().getApolloConfig().isAlertsDisabled(player.getUniqueId())) {
							player.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") Apollo restarted in " + now2 + "ms.");
						}
					}
					
				}  else if (args[0].toString().equalsIgnoreCase("exempt")) {
					sender.sendMessage(ChatColor.RED + "Usage: /Apollo exempt <player>");
				} else if (args[0].toString().equalsIgnoreCase("unexempt")) {
					sender.sendMessage(ChatColor.RED + "Usage: /Apollo unexempt <player>");
				} else {
					sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------------" + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Apollo V" + plugin.getDescription().getVersion() + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------------");
					sender.sendMessage(ChatColor.GOLD + "/logs " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Show log count for the specified user");
					sender.sendMessage(ChatColor.GOLD + "/alerts " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Toggle receiving of your Apollo alerts");
					sender.sendMessage(ChatColor.GOLD + "/clearlogs " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Clear the specified player's logs");
					sender.sendMessage(ChatColor.GOLD + "/ping " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Check the ping of a player");
					sender.sendMessage(ChatColor.GOLD + "/Apollo " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Show this help menu");
					sender.sendMessage(ChatColor.GOLD + "/Apollo exempt " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Make a player exempt from Apollo checks");
					sender.sendMessage(ChatColor.GOLD + "/Apollo unexempt " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Make an exempt player unexempt from Apollo checks");
					sender.sendMessage(ChatColor.GOLD + "/Apollo restart " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Reload all Apollo checks and data");
				} 
					
				} else if (args.length == 2) {
					if (args[0].toString().equalsIgnoreCase("exempt")) {
						@SuppressWarnings("deprecation")
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1].toString());
						if (!(target instanceof OfflinePlayer)) {
							sender.sendMessage(ChatColor.RED + "Invalid player!");
						} else {
							ApolloConfig config = Apollo.getInstance().getApolloConfig();
							if (!config.isExempt(target.getUniqueId())) {
								config.setExempt(target.getUniqueId(), true);
					        	
								for (Player player : Bukkit.getOnlinePlayers()) {
									if (!player.hasPermission("apollo.alerts")) continue;
									if (!Apollo.getInstance().getApolloConfig().isAlertsDisabled(player.getUniqueId())) {
										player.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") " + sender.getName() + " has exempted " + target.getName() + " from Apollo checks.");
									}
								}
					        } else {
								sender.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") That player is already exempt!");
							}
						}
					} else if (args[0].toString().equalsIgnoreCase("unexempt")) {
						@SuppressWarnings("deprecation")
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1].toString());
						if (!(target instanceof OfflinePlayer)) {
							sender.sendMessage(ChatColor.RED + "Invalid player!");
						} else {
							ApolloConfig config = Apollo.getInstance().getApolloConfig();
							if (!config.isExempt(target.getUniqueId())) {
								sender.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") That player is not exempt!");
							} else {
								config.setExempt(target.getUniqueId(), false);
					        	
								for (Player player : Bukkit.getOnlinePlayers()) {
									if (!player.hasPermission("apollo.alerts")) continue;
									if (!Apollo.getInstance().getApolloConfig().isAlertsDisabled(player.getUniqueId())) {
										player.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") " + sender.getName() + " has unexempted " + target.getName() + " from Apollo checks.");
									}
								}
							}
						}
					} else {
						sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------------" + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Apollo V" + plugin.getDescription().getVersion() + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------------");
						sender.sendMessage(ChatColor.GOLD + "/logs " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Show log count for the specified user");
						sender.sendMessage(ChatColor.GOLD + "/alerts " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Toggle receiving of your Apollo alerts");
						sender.sendMessage(ChatColor.GOLD + "/clearlogs " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Clear the specified player's logs");
						sender.sendMessage(ChatColor.GOLD + "/ping " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Check the ping of a player");
						sender.sendMessage(ChatColor.GOLD + "/Apollo " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Show this help menu");
						sender.sendMessage(ChatColor.GOLD + "/Apollo exempt " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Make a player exempt from Apollo checks");
						sender.sendMessage(ChatColor.GOLD + "/Apollo unexempt " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Make an exempt player unexempt from Apollo checks");
						sender.sendMessage(ChatColor.GOLD + "/Apollo restart " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Reload all Apollo checks and data");
					}
					
			} else {
				sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------------" + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Apollo V" + plugin.getDescription().getVersion() + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "----------------");
				sender.sendMessage(ChatColor.GOLD + "/logs " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Show log count for the specified user");
				sender.sendMessage(ChatColor.GOLD + "/alerts " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Toggle receiving of your Apollo alerts");
				sender.sendMessage(ChatColor.GOLD + "/clearlogs " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Clear the specified player's logs");
				sender.sendMessage(ChatColor.GOLD + "/ping " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Check the ping of a player");
				sender.sendMessage(ChatColor.GOLD + "/Apollo " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Show this help menu");
				sender.sendMessage(ChatColor.GOLD + "/Apollo exempt " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Make a player exempt from Apollo checks");
				sender.sendMessage(ChatColor.GOLD + "/Apollo unexempt " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Make an exempt player unexempt from Apollo checks");
				sender.sendMessage(ChatColor.GOLD + "/Apollo restart " + ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Reload all Apollo checks and data");
			}
		}
		return true;
	}

}
