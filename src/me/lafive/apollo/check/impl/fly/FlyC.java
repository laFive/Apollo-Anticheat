package me.lafive.apollo.check.impl.fly;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;

public class FlyC extends Check {
	
	public FlyC(PlayerData data) {
		super(data, "Fly", "C", 0, 800);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			if (!getData().isServerGround() && getData().getServerGroundStateTicks() > 4) {
				
				if (e.getLocation().isOnGround() && e.getLocation().getPosY() % 1/64. > 0.001) {
					
					handleViolation(100, "Y-Pos:" + e.getLocation().getPosY() + " ServerAirTicks:" + getData().getServerGroundStateTicks() + " ClientGroundTicks:" + e.getGroundStateTicks());
					
				} else if (!e.getLocation().isOnGround()) {
					
					decreaseVl(1);
					
				}
				
			}
			
		}
		
	}

}
