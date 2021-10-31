package me.lafive.apollo.check.impl.killaura;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity.EntityUseAction;
import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.EntityInteractEvent;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.event.impl.InteractEvent;

public class KillAuraB extends Check {
	
	private boolean streak;
	
	public KillAuraB(PlayerData data) {
		super(data, "KillAura", "B", 0, 300);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (getData().getClientVersion() > 47)
			return;
		
		if (event instanceof FlyingEvent) {
			
			streak = false;
			
		} else if (event instanceof InteractEvent) {
			
			streak = true;
			
		} else if (event instanceof EntityInteractEvent) {
			
			EntityInteractEvent e = (EntityInteractEvent) event;
			if (e.getAction().equals(EntityUseAction.ATTACK)) {
				if (streak) {
					handleViolation(100, "ClientVersion:" + getData().getClientVersion());
				}
			}
			
		}
		
	}

}
