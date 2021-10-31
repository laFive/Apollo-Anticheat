package me.lafive.apollo.check.impl.inventory;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.InventoryInteractEvent;

public class InventoryA extends Check {
	
	public InventoryA(PlayerData data) {
		super(data, "Inventory", "A", 0, 500);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (getData().getClientVersion() > 47)
			return;
		
		if (event instanceof InventoryInteractEvent) {
			
			if (!getData().isInventoryOpen() && getData().getInventoryTicks() == 0) {
				
				handleViolation(100, "ClientVersion:" + getData().getClientVersion());
				
			} else if (getData().getInventoryTicks() == 0) {
				
				decreaseVl(1);
				
			}
			
		}
		
	}

}
