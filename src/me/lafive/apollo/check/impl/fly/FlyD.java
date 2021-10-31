package me.lafive.apollo.check.impl.fly;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;

public class FlyD extends Check {
	
	private int threshold;
	private long lastFlying;
	
	public FlyD(PlayerData data) {
		super(data, "Fly", "D", 600, 2000);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			if (!e.isPos()) return;
			
			if (!getData().isServerGround() && getData().getServerGroundStateTicks() > 5) {
				
				if (e.getLocation().isOnGround()) {
					
					if (System.currentTimeMillis() - lastFlying > 45L) {
						if (++threshold > 5) {
							handleViolation(100, "ServerAirTicks:" + getData().getServerGroundStateTicks() + " ClientGroundTicks:" + e.getGroundStateTicks() + " Y-Pos:" + e.getLocation().getPosY());
						} 
					} else {
						threshold = 0;
					}
					
				} else {
					decreaseVl(1);
				}
				
			} else {
				threshold = 0;
			}
			
			lastFlying = System.currentTimeMillis();
			
		}
		
	}

}
