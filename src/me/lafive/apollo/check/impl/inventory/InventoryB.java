package me.lafive.apollo.check.impl.inventory;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity.EntityUseAction;
import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.EntityInteractEvent;

public class InventoryB extends Check {
	
	public InventoryB(PlayerData data) {
		super(data, "Inventory", "B", 0, 500);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof EntityInteractEvent) {
			
			EntityInteractEvent e = (EntityInteractEvent) event;
			if (e.getAction().equals(EntityUseAction.ATTACK)) {
				
				if (getData().isServerInventoryOpen()) {
					
					handleViolation(100, "ClientVersion:" + getData().getClientVersion());
					
				} else {
					
					decreaseVl(1);
					
				}
				
			}
			
		}
		
	}

}
