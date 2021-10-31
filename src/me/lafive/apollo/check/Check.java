package me.lafive.apollo.check;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.lafive.apollo.Apollo;
import me.lafive.apollo.config.ApolloConfig;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.util.LogUtil;
import me.lafive.core.Core;
import net.md_5.bungee.api.ChatColor;

public class Check {
	
	private String name, subName;
	private int vl, alertVl, banVl;
	private PlayerData data;
	
	public Check(PlayerData data, String name, String subName, int alertVl, int banVl) {
		this.vl = 0;
		this.name = name;
		this.subName = subName;
		this.alertVl = alertVl;
		this.banVl = banVl;
		this.data = data;
	}
	
	public void handleViolation(int amount, String verbose) {
		
		if (!getData().hasRepliedToKeepAlive())
			return;
		
		ApolloConfig config = Apollo.getInstance().getApolloConfig();
		if (config.isExempt(data.getPlayer().getUniqueId()))
			return;
		vl += amount;
		LogUtil.addLog(data.getPlayer(), name, subName, verbose, vl, data.getKeepAlivePing());
		if (vl >= alertVl) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				
				if (!player.hasPermission("Apollo.alerts"))
					continue;
				
				if (config.isAlertsDisabled(player.getUniqueId()))
					continue;
				
				//DecimalFormat df = new DecimalFormat("##.##");
				//double tps = MinecraftServer.getServer().recentTps[0];
				//if (tps > 20) tps = 20.00;
				player.sendMessage(ChatColor.GRAY + "(" + ChatColor.GOLD + "!" + ChatColor.GRAY + ") " + ChatColor.GOLD + data.getPlayer().getName() + ChatColor.GRAY + " failed " + ChatColor.GOLD + name + " " + subName + ChatColor.GRAY + " VL[" + ChatColor.GOLD + vl + ChatColor.GRAY + "]");
				
			}
		}
		if (vl >= banVl) {
			
			if (config.isAutobans()) {
				
				Bukkit.getScheduler().runTask(Apollo.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), config.getBanCommand().replaceAll("%player%", data.getPlayer().getName()));
						if (Bukkit.getPluginManager().getPlugin("FiveCore") != null) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sc (Apollo) I banned " + data.getPlayer().getName() + " on " + Core.INSTANCE.getPluginConfig().getServerName() + " for " + name + " " + subName);
						}
					}
				});
				
				vl = 0;
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------------------------------");
				Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "✗ "  + ChatColor.GOLD + "Apollo " + ChatColor.GRAY + "has removed " + ChatColor.GOLD + data.getPlayer().getName() + ChatColor.GRAY + " from the server.");
				Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "✗ "  + ChatColor.GRAY + "Reason " + ChatColor.DARK_GRAY + "»" + ChatColor.GOLD + " Cheating");
				Bukkit.broadcastMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "-------------------------------------------");
				
			}
			
		}
		
	}
	
	public void decreaseVl(int amount) {
		
		vl = Math.max(0, vl - amount);
		
	}
	
	public PlayerData getData() {
		return data;
	}
	
	public void handleEvent(Event e) {}

}
