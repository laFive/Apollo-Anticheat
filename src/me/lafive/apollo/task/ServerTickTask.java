package me.lafive.apollo.task;

import org.bukkit.Bukkit;

import me.lafive.apollo.Apollo;

public class ServerTickTask implements Runnable {

	@Override
	public void run() {
		
		Apollo.getInstance().getDataManager().getAllData().forEach(data -> data.handleServerTick());
		
	}
	
	public void schedule() {
		
		Bukkit.getScheduler().runTaskTimer(Apollo.getInstance(), this, 1L, 1L);
		
	}

}
