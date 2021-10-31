package me.lafive.apollo.check.impl.fly;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;

public class FlyB extends Check {
	
	private int resetVl;
	
	public FlyB(PlayerData data) {
		super(data, "Fly", "B", 0, 500);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			FlyingEvent e = (FlyingEvent) event;
			
			if (e.isPos() && e.getLocation().isOnGround()) {
				
				if (e.getLocation().getPosY() % 0.015625 != 0) {
					
					handleViolation(100, "Y-Pos:" + e.getLocation().getPosY() + " Modu:" + e.getLocation().getPosY() % 0.015625);
					
				} else {
					
					resetVl++;
					if (resetVl > 40) {
						decreaseVl(1);
						resetVl = 0;
					}
					
				}
				
			}
			
		}
		
	}

}
