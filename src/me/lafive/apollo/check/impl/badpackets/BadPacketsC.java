package me.lafive.apollo.check.impl.badpackets;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.event.impl.InteractEvent;

public class BadPacketsC extends Check {
	
	private boolean flagged;
	private long lastFlying;
	private int threshold;
	
	public BadPacketsC(PlayerData data) {
		super(data, "BadPackets", "C", 0, 600);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			if (flagged) {
				if (System.currentTimeMillis() - lastFlying > 45L) {
					if (++threshold > 2) {
						handleViolation(100, "FlyingTimeDif:" + (System.currentTimeMillis() - lastFlying));
					}
				} else {
					threshold = 0;
				}
			}
			
			flagged = false;
			lastFlying = System.currentTimeMillis();
			
		} else if (event instanceof InteractEvent) {
			
			if (System.currentTimeMillis() - lastFlying < 5L) {
				
				flagged = true;
				
			} else {
				
				threshold = 0;
				decreaseVl(1);
				
			}
			
		}
		
	}

}
