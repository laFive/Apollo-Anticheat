package me.lafive.apollo.check.impl.badpackets;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;

public class BadPacketsB extends Check {
	
	public BadPacketsB(PlayerData data) {
		super(data, "BadPackets", "B", 0, 100);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			
			if (e.isLook()) {
				if (Math.abs(e.getLocation().getPitch()) > 90) {
					handleViolation(100, "Pitch:" + e.getLocation().getPitch());
				}
			}
			
		}
		
	}

}
