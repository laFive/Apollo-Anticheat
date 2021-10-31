package me.lafive.apollo.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import me.lafive.apollo.Apollo;

public class ApolloConfig {
	
	private boolean autobans;
	private String banCommand;
	private ArrayList<UUID> disabledAlerts = new ArrayList<UUID>();
	private ArrayList<UUID> exemptPlayers = new ArrayList<UUID>();
	private File config;
	
	public ApolloConfig(Apollo plugin) {
		
		plugin.getDataFolder().mkdirs();
		config = new File(plugin.getDataFolder() + "/config.yml");
		if (!config.exists()) {
			try {
				config.createNewFile();
				YamlConfiguration yaml = YamlConfiguration.loadConfiguration(config);
				yaml.set("AutoBans", true);
				yaml.set("Ban-Command", "/ban %player% Cheating");
				yaml.set("data.Disabled-Alert-Players", new ArrayList<String>());
				yaml.set("data.Exempt-Players", new ArrayList<String>());
				yaml.save(config);
			} catch (IOException ex) {
				System.out.println("[Apollo] A fatal error occured while saving the initial config file! (IOException)");
				Bukkit.getPluginManager().disablePlugin(plugin);
				return;
			}
		}
		
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(config);
		autobans = yaml.getBoolean("AutoBans");
		banCommand = yaml.getString("Ban-Command");
		for (String s : (List<String>) (yaml.getList("data.Disabled-Alert-Players"))) {
			disabledAlerts.add(UUID.fromString(s));
		}
		for (String s : (List<String>) (yaml.getList("data.Exempt-Players"))) {
			exemptPlayers.add(UUID.fromString(s));
		}
		
	}
	
	public boolean isAlertsDisabled(UUID u) {
		return disabledAlerts.contains(u);
	}
	
	public String getBanCommand() {
		return banCommand;
	}
	
	public void setAlertsDisabled(UUID u, boolean value) {
		if (value) {
			if (!disabledAlerts.contains(u))
				disabledAlerts.add(u);
		} else {
			disabledAlerts.remove(u);
		}
		saveConfig();
	}
	
	public boolean isExempt(UUID u) {
		return exemptPlayers.contains(u);
	}
	
	public void setExempt(UUID u, boolean value) {
		if (value) {
			if (!exemptPlayers.contains(u))
				exemptPlayers.add(u);
		} else {
			exemptPlayers.remove(u);
		}
		saveConfig();
	}
	
	public boolean isAutobans() {
		return autobans;
	}
	
	public void setAutobans(boolean value) {
		autobans = value;
		saveConfig();
	}
	
	public void saveConfig() {
		
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(config);
		yaml.set("AutoBans", autobans);
		yaml.set("Ban-Command", banCommand);
		ArrayList<String> disabledStringUuids = new ArrayList<String>(), exemptStringUuids = new ArrayList<String>();
		for (UUID u : disabledAlerts) {
			disabledStringUuids.add(u.toString());
		}
		for (UUID u : exemptPlayers) {
			exemptStringUuids.add(u.toString());
		}
		yaml.set("data.Disabled-Alert-Players", disabledStringUuids);
		yaml.set("data.Exempt-Players", exemptStringUuids);
		try {
			yaml.save(config);
		} catch (IOException ex) {
			System.out.println("[Apollo] An error occured while saving the config! (IOException)");
		}
		
	}

}
