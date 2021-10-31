package me.lafive.apollo.check.impl.speed;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;

public class SpeedB extends Check {
	
	private int speedAmplifier;
	private int speedTicks;
	
	public SpeedB(PlayerData data) {
		super(data, "Speed", "B", 0, 800);
	}
	
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			speedTicks = Math.max(0, --speedTicks);
			
			FlyingEvent e = (FlyingEvent) event;
			
			if (!e.isPos() || getData().isRiding() || getData().isFlying() || getData().getUnderBlockTicks() > 0 || getData().getIceTicks() > 0 || getData().getLiquidTicks() > 0 || getData().isPositionDesynced() || getData().getVelocityTicks() > 0)
				return;
			
			double motion = Math.hypot(e.getLocation().getPosX() - e.getFromLocation().getPosX(), e.getLocation().getPosZ() - e.getFromLocation().getPosZ());
			double motionThreshold;
			
			if (e.getLocation().isOnGround()) {
				
				if (e.getGroundStateTicks() > 7) {
					motionThreshold = 0.3;
				} else if (e.getGroundStateTicks() == 1) {
					if (getData().getStairTicks() > 0) {
						motionThreshold = 0.5;
					} else {
						motionThreshold = 0.34;
					}
				} else if (e.getGroundStateTicks() == 2) {
					motionThreshold = 0.46;
					if (getData().getStairTicks() > 0) {
						motionThreshold = 0.5;
					}
				} else if (e.getGroundStateTicks() == 3) {
					motionThreshold = 0.43;
					if (getData().getStairTicks() > 0) {
						motionThreshold = 0.5;
					}
				} else {
					motionThreshold = 0.355;
					if (getData().getStairTicks() > 0) {
						motionThreshold = 0.5;
					}
				}
				
			} else {
				if (e.getGroundStateTicks() > 6) {
					motionThreshold = 0.35;
					if (getData().getStairTicks() > 0) {
						motionThreshold = 0.5;
					}
				} else if (e.getGroundStateTicks() <= 2) {
					if (getData().getStairTicks() > 0) {
						motionThreshold = 0.7;
					} else {
						motionThreshold = 0.62;
					}
				} else {
					if (getData().getStairTicks() > 0) {
						motionThreshold = 0.7;
					} else {
						motionThreshold = 0.38;
					}
				}
			}
			
			for (PotionEffect pe : getData().getPlayer().getActivePotionEffects()) {
				if (pe.getType().equals(PotionEffectType.SPEED)) {
					speedTicks = 15;
					speedAmplifier = pe.getAmplifier();
					break;
				}
			}
			
			if (speedTicks > 0) {
				motionThreshold += (speedAmplifier + 1) * 0.17;
			}
			
			if (motion > motionThreshold) {
				handleViolation(100, "Motion:" + motion + " MotionThreshold:" + motionThreshold + " ClientGround:" + e.getLocation().isOnGround() + " ClientGroundStateTicks:" + e.getGroundStateTicks() + " SpeedTicks:" + speedTicks + " SpeedAmplifier:" + speedAmplifier);
			} else {
				decreaseVl(1);
			}
			
		}
		
	}

}
