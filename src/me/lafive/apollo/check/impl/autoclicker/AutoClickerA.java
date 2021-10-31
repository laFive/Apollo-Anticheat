package me.lafive.apollo.check.impl.autoclicker;

import me.lafive.apollo.check.Check;
import me.lafive.apollo.data.PlayerData;
import me.lafive.apollo.event.Event;
import me.lafive.apollo.event.impl.FlyingEvent;
import me.lafive.apollo.event.impl.SwingEvent;
import me.lafive.apollo.util.FixedSizeDoubleArray;

public class AutoClickerA extends Check {
	
	private FixedSizeDoubleArray timings;
	private int ticksSince;
	private double lastDeviation;
	
	public AutoClickerA(PlayerData data) {
		super(data, "AutoClicker", "A", 0, 800);
		timings = new FixedSizeDoubleArray(30);
	}
	
	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof FlyingEvent) {
			
			ticksSince++;
			 
		} else if (event instanceof SwingEvent) {
			
			if (getData().isDigging())
				return;
			
			timings.add(ticksSince * 50);
			ticksSince = 0;
			
			if (timings.getAverage() < 7 || timings.getSize() < 30)
				return;
			
			double deviation = timings.getStandardDeviation();
			double lastDeviation = this.lastDeviation;
			this.lastDeviation = deviation;
			
			if (Math.abs(deviation - lastDeviation) < 4) {
				
				handleViolation(100, "Deviation:" + deviation + " LastDeviation:" + lastDeviation);
				
			} else {
				
				decreaseVl(1);
				
			}
			
		}
		
	}

}
