package me.lafive.apollo.check.impl.killaura;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity.EntityUseAction;
import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.EntityInteractEvent;
import me.lafive.apollo.event.impl.FlyingEvent;

public class KillAuraD extends Check {
	
	private boolean flagged;
	private long lastFlying;
	private int threshold;
	
	public KillAuraD(PlayerData data) {
		super(data, "KillAura", "C", 0, 600);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			if (flagged) {
				if (System.currentTimeMillis() - lastFlying > 45L) {
					
					if (++threshold > 2) {
						
						handleViolation(100, "Threshold:" + threshold + " ClientVersion:" + getData().getClientVersion());
						
					}
					
				}
			}
			flagged = false;
			lastFlying = System.currentTimeMillis();
			
		} else if (event instanceof EntityInteractEvent) {
			
			EntityInteractEvent e = (EntityInteractEvent) event;
			if (e.getAction().equals(EntityUseAction.ATTACK)) {
				
				if (System.currentTimeMillis() - lastFlying < 5L) {
					
					flagged = true;
					
				} else {
					
					threshold = Math.max(0, --threshold);
					decreaseVl(1);
					
				}
				
			}
			
		}
		
	}

}
