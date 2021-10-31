package me.lafive.apollo.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class DataManager {
	
	private HashMap<UUID, PlayerData> data = new HashMap<UUID, PlayerData>();
	
	public void createPlayerData(Player p) {
		data.put(p.getUniqueId(), new PlayerData(p));
	}

	public PlayerData getPlayerData(Player p) {
		if (!data.containsKey(p.getUniqueId())) {
			p.kickPlayer(ChatColor.RED + "Failed to fetch your Apollo data! Please relog");
			return null;
		}
		return data.get(p.getUniqueId());
	}
	
	public Collection<PlayerData> getAllData(){
		
		return data.values();
		
	}
	
}
