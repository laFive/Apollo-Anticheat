package me.lafive.apollo.check.impl.fly;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;

public class FlyE extends Check {
	
	public FlyE(PlayerData data) {
		super(data, "Fly", "E", 0, 800);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			
			if (getData().isPositionDesynced() || getData().getVelocityTicks() > 0 || getData().isFlying() || getData().getJumpBoostTicks() > 0)
				return;
			
			double motionY = e.getLocation().getPosY() - e.getFromLocation().getPosY();
			
			if (motionY > 2.5) {
				handleViolation(1000, "MotionY:" + motionY + " ClientAirTicks:" + e.getGroundStateTicks());
				return;
			}
			
			if (Math.abs(motionY) > 8) {
				handleViolation(100, "MotionY:" + motionY + " ClientAirTicks:" + e.getGroundStateTicks());
				return;
			}
			
			if (!getData().isServerGround() && getData().getServerGroundStateTicks() > 2) {
				if (motionY > 0.42) {
					handleViolation(100, "MotionY:" + motionY + " ServerAirTicks:" + getData().getServerGroundStateTicks());
					return;
				}
			}
			
			if (!e.getLocation().isOnGround() && e.getGroundStateTicks() > 4) {
				if (motionY > 0.42) {
					handleViolation(100, "MotionY:" + motionY + " ClientAirTicks:" + e.getGroundStateTicks());
					return;
				}
			}
			
			if (motionY > 0.85) {
				handleViolation(100, "MotionY:" + motionY + " ClientAirTicks:" + e.getGroundStateTicks());
				return;
			}
			
			decreaseVl(1);
			
		}
		
	}

}
