package me.lafive.apollo.event;

import me.lafive.apollo.data.PlayerData;

public class Event {
	
	public void callEvent(PlayerData data) {
		
		data.getPlayerChecks().forEach(check -> check.handleEvent(this));
		
	}

}
