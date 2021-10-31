package me.lafive.apollo.check.impl.speed;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;

public class SpeedA extends Check {
	
	private double lastMotion;
	private int threshold;
	
	public SpeedA(PlayerData data) {
		super(data, "Speed", "A", 0, 800);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			
			if (e.isPos()) {
				
				double motion = Math.hypot(e.getLocation().getPosX() - e.getFromLocation().getPosX(), e.getLocation().getPosZ() - e.getFromLocation().getPosZ());
				double lastMotion = this.lastMotion;
				this.lastMotion = motion;
				
				if (getData().isFlying() || getData().isOnClimbable() || getData().getLiquidTicks() > 0 || getData().getTeleportTicks() > 0 || getData().isRiding() || getData().getVelocityTicks() > 0)
					return;
				
				if (!e.getLocation().isOnGround() && e.getGroundStateTicks() > 2 && Math.abs(lastMotion) > 1E-7) {
					
					double expectedMotion = lastMotion * 0.91D;
					double difference = Math.abs(expectedMotion - motion);
					
					if (motion > expectedMotion && difference > 0.03) {
						
						if ((threshold += 6) > 24) {
							handleViolation(100, "Motion:" + motion + " ExpectedMotion:" + expectedMotion + " ClientAirTicks:" + e.getGroundStateTicks() + " LastMotion:" + lastMotion);
						}
						
					} else {
						
						threshold = Math.max(0, --threshold);
						decreaseVl(1);
						
					}
					
				}
				
			}
			
		}
		
	}

}
