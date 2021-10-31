package me.lafive.apollo.check.impl.killaura;

import org.bukkit.entity.Entity;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity.EntityUseAction;
import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.EntityInteractEvent;
import me.lafive.apollo.event.impl.FlyingEvent;

public class KillAuraC extends Check {
	
	private boolean attacked;
	private Entity lastEntity;
	private long lastFlying, lastAttack;
	private boolean flagged;
	
	public KillAuraC(PlayerData data) {
		super(data, "KillAura", "C", 0, 300);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (getData().getClientVersion() > 47) {
			
			if (event instanceof FlyingEvent) {
				
				if (flagged) {
					
					if (System.currentTimeMillis() - lastFlying > 45L) {
						
						handleViolation(100, "FlyingTimeDif:" + (System.currentTimeMillis() - lastFlying));
						
					}
					
				}
				flagged = false;
				
				lastFlying = System.currentTimeMillis();
				
			} else if (event instanceof EntityInteractEvent) {
				
				EntityInteractEvent e = (EntityInteractEvent) event;
				if (e.getAction().equals(EntityUseAction.ATTACK)) {
					
					if (System.currentTimeMillis() - lastAttack < 5L && e.getEntity() != lastEntity) {
						
						flagged = true;
						
					} else if (System.currentTimeMillis() - lastAttack > 45L) {
						
						decreaseVl(1);
						
					}
					
					lastEntity = e.getEntity();
					lastAttack = System.currentTimeMillis();
					
				}
				
			}
			
		} else {
			
			if (event instanceof FlyingEvent) {
				
				attacked = false;
				lastEntity = null;
				
			} else if (event instanceof EntityInteractEvent) {
				
				EntityInteractEvent e = (EntityInteractEvent) event;
				if (e.getAction().equals(EntityUseAction.ATTACK)) {
					
					if (attacked && e.getEntity() != lastEntity) {
						handleViolation(100, "ClientVersion:" + getData().getClientVersion());
					} else if (!attacked) {
						decreaseVl(1);
					}
					attacked = true;
					lastEntity = e.getEntity();
					
				}
				
			}
			
		}
		
	}

}
