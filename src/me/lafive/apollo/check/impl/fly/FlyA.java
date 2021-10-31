package me.lafive.apollo.check.impl.fly;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;

public class FlyA extends Check {
	
	private double lastDeltaY;
	private int resetVl;
	private int threshold;
	
	public FlyA(PlayerData data) {
		super(data, "Fly", "A", 0, 1500);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			
			if (getData().getClientVersion() >= 755) {
				if (!e.isNewLocation()) return;
			}
			
			if (e.getLocation().isOnGround())
				return;
			
			double deltaY = e.getLocation().getPosY() - e.getFromLocation().getPosY();
			double lastDeltaY = this.lastDeltaY;
			this.lastDeltaY = deltaY;
			
			if (getData().getTeleportTicks() > 0 || getData().isPositionDesynced() || getData().isOnClimbable() || getData().isRiding() || getData().getLiquidTicks() > 0)
				return;
			
			if (e.getGroundStateTicks() < 2 || getData().getWebTicks() > 0 || getData().getVelocityTicks() > 0 || getData().isFlying() || getData().getUnderBlockTicks() > 0)
				return;
			
			double predictedDeltaY = (lastDeltaY - 0.08D) * 0.98F;
			double difference = Math.abs(predictedDeltaY - deltaY);
			if (difference < 0.005) return;
			
			if (predictedDeltaY > 0 && deltaY < 0 && Math.abs(deltaY) < 0.1)
				return;
			
			double maxDifference = 0.001;
			if (e.getGroundStateTicks() <= 2)
				maxDifference += 0.03;
			
			if (difference > maxDifference) {
				
				if ((threshold += 6) > 18) {
					
					handleViolation(100, "DeltaY:" + deltaY + " LastDeltaY:" + lastDeltaY + " Difference:" + difference + " Version:" + getData().getClientVersion());
					
				}
				
			} else {
				threshold = Math.max(0, --threshold);
				if (++resetVl > 40) {
					resetVl = 0;
					decreaseVl(1);
				}
			}
			
		}
		
	}

}
