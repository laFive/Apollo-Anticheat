package me.lafive.apollo.check.impl.timer;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.event.impl.TeleportEvent;
import me.lafive.apollo.util.FixedSizeDoubleArray;

public class TimerB extends Check {
	
	private FixedSizeDoubleArray array;
	private Long lastSend = null;
	private long lastLag;
	private int threshold;
	
	public TimerB(PlayerData data) {
		super(data, "Timer", "B", 0, 1500);
		array = new FixedSizeDoubleArray(20);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			if (getData().isRiding())
				return;
			
			if (lastSend != null) {
				
				long delay = System.currentTimeMillis() - lastSend;
				if (delay > 150L || getData().isPositionDesynced()) {
					lastLag = System.currentTimeMillis();
					array.clear();
					threshold = 0;
					return;
				}
				
				if (System.currentTimeMillis() - lastLag < 1500L) {
					return;
				}
				
				array.add(delay);
				
				if (array.getSize() >= 20 && array.getAverage() < 45) {
					
					if (++threshold > 3) {
						handleViolation(100, "Threshold:" + threshold + " Average:" + array.getAverage());
						threshold = 0;
					}
					
				} else if (array.getSize() >= 20) {
					
					decreaseVl(1);
					--threshold;
					
				}
				
			}
			
			lastSend = System.currentTimeMillis();
			
		} else if (event instanceof TeleportEvent) {
			
			array.clear();
			threshold = 0;
			
			
		}
		
	}

}
