package me.lafive.apollo.check.impl.killaura;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity.EntityUseAction;
import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.EntityInteractEvent;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.event.impl.SwingEvent;

public class KillAuraA extends Check {
	
	private boolean swung;
	private boolean expect;
	
	public KillAuraA(PlayerData data) {
		super(data, "KillAura", "A", 0, 500);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (getData().getClientVersion() <= 47) {
			
			if (event instanceof SwingEvent) {
				
				swung = true;
				decreaseVl(1);
				
			} else if (event instanceof FlyingEvent) {
				
				swung = false;
				
			} else if (event instanceof EntityInteractEvent) {
				
				EntityInteractEvent e = (EntityInteractEvent) event;
				if (e.getAction().equals(EntityUseAction.ATTACK) && !swung) {
					handleViolation(100, "ClientVersion:" + getData().getClientVersion());
				}
				
			}
			
		} else {
			
			if (event instanceof EntityInteractEvent) {
				
				EntityInteractEvent e = (EntityInteractEvent) event;
				if (e.getAction().equals(EntityUseAction.ATTACK)) {
					expect = true;
				}
				
			} else if (event instanceof SwingEvent) {
				
				if (expect)
					decreaseVl(1);
				
				expect = false;
				
			} else if (event instanceof FlyingEvent) {
				
				if (expect) {
					handleViolation(100, "ClientVersion:" + getData().getClientVersion());
				}
				
			}
			
		}
		
	}

}
