package me.lafive.apollo.check.impl.timer;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.event.impl.TeleportEvent;

public class TimerA extends Check {
	
	private Long lastSend = null;
	private int balance;
	
	public TimerA(PlayerData data) {
		super(data, "Timer", "A", 0, 1200);
		lastSend = System.currentTimeMillis();
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			
			if (getData().getClientVersion() >= 755) {
				if (!e.isNewLocation()) return;
			}
			
			if (lastSend != null) {
				
				balance -= (System.currentTimeMillis() - lastSend);
				
			}
			
			balance += 50;
			
			if (balance > 110L) {
				
				handleViolation(100, "Balance:" + balance + " LastDif:" + (System.currentTimeMillis() - lastSend));
				while (balance >= 50) {
					balance -= 50;
				}
				
			} else if (balance == 0) {
				
				decreaseVl(1);
				
			}
			
			lastSend = System.currentTimeMillis();
			
		} else if (event instanceof TeleportEvent) {
			
			balance -= 50;
			
		}
		
	}
	

}
