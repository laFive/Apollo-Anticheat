package me.lafive.apollo;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.retrooper.packetevents.PacketEvents;
import me.lafive.apollo.check.CheckManager;
import me.lafive.apollo.check.command.AlertsCommand;
import me.lafive.apollo.check.command.BaseCommand;
import me.lafive.apollo.check.command.ClearLogsCommand;
import me.lafive.apollo.check.command.LogsCommand;
import me.lafive.apollo.check.command.PingCommand;
import me.lafive.apollo.check.command.TabCommand;
import me.lafive.apollo.config.ApolloConfig;
import me.lafive.apollo.data.DataManager;
import me.lafive.apollo.listener.PlayerListener;
import me.lafive.apollo.task.ServerTickTask;

public class Apollo extends JavaPlugin {
	
	private static Apollo instance;
	
	private PacketEvents packetEvents;
	private DataManager dataManager;
	private CheckManager checkManager;
	private ApolloConfig config;
	
	@Override
	public void onEnable() {
		
		instance = this;
		packetEvents = PacketEvents.create(this);
		packetEvents.init();
		packetEvents.registerListener(new PlayerListener());
		dataManager = new DataManager();
		checkManager = new CheckManager();
		new ServerTickTask().schedule();
		config = new ApolloConfig(this);
		registerCommands();
		System.out.println("[Apollo] Apollo v" + getDescription().getVersion() + " has been loaded!");
		
	}
	
	@Override
	public void onDisable() {
		
		System.out.println("[Apollo] Apollo has been successfully disabled.");
		
	}
	
	public void registerCommands() {
		
		getCommand("apollo").setExecutor(new BaseCommand());
		getCommand("ping").setExecutor(new PingCommand());
		getCommand("toggleautobans").setExecutor(new TabCommand());
		getCommand("logs").setExecutor(new LogsCommand());
		getCommand("clearlogs").setExecutor(new ClearLogsCommand());
		getCommand("alerts").setExecutor(new AlertsCommand());
		
	}
	
	public static Apollo getInstance() {
		return instance;
	}
	
	public PacketEvents getPacketEvents() {
		return packetEvents;
	}
	
	public ApolloConfig getApolloConfig() {
		return config;
	}
	
	public DataManager getDataManager() {
		return dataManager;
	}
	
	public CheckManager getCheckManager() {
		return checkManager;
	}

}
