package me.lafive.apollo.check;

import java.util.ArrayList;

import me.lafive.apollo.check.impl.autoclicker.AutoClickerA;
import me.lafive.apollo.check.impl.badpackets.BadPacketsA;
import me.lafive.apollo.check.impl.badpackets.BadPacketsB;
import me.lafive.apollo.check.impl.badpackets.BadPacketsC;
import me.lafive.apollo.check.impl.badpackets.BadPacketsD;
import me.lafive.apollo.check.impl.fly.FlyA;
import me.lafive.apollo.check.impl.fly.FlyB;
import me.lafive.apollo.check.impl.fly.FlyC;
import me.lafive.apollo.check.impl.fly.FlyD;
import me.lafive.apollo.check.impl.fly.FlyE;
import me.lafive.apollo.check.impl.hitbox.HitboxA;
import me.lafive.apollo.check.impl.inventory.InventoryA;
import me.lafive.apollo.check.impl.inventory.InventoryB;
import me.lafive.apollo.check.impl.inventory.InventoryC;
import me.lafive.apollo.check.impl.killaura.KillAuraA;
import me.lafive.apollo.check.impl.killaura.KillAuraB;
import me.lafive.apollo.check.impl.killaura.KillAuraC;
import me.lafive.apollo.check.impl.killaura.KillAuraD;
import me.lafive.apollo.check.impl.killaura.KillAuraE;
import me.lafive.apollo.check.impl.reach.ReachA;
import me.lafive.apollo.check.impl.speed.SpeedA;
import me.lafive.apollo.check.impl.speed.SpeedB;
import me.lafive.apollo.check.impl.timer.TimerA;
import me.lafive.apollo.check.impl.timer.TimerB;
import me.lafive.apollo.check.impl.velocity.VelocityA;
import me.lafive.apollo.data.PlayerData;

public class CheckManager {
	
	public void loadChecks(PlayerData data) {
		
		ArrayList<Check> checks = new ArrayList<Check>();
		checks.add(new FlyA(data));
		checks.add(new FlyB(data));
		checks.add(new FlyC(data));
		checks.add(new FlyD(data));
		checks.add(new FlyE(data));
		checks.add(new SpeedA(data));
		checks.add(new SpeedB(data));
		checks.add(new VelocityA(data));
		checks.add(new BadPacketsA(data));
		checks.add(new BadPacketsB(data));
		checks.add(new BadPacketsC(data));
		checks.add(new BadPacketsD(data));
		checks.add(new KillAuraA(data));
		checks.add(new KillAuraB(data));
		checks.add(new KillAuraC(data));
		checks.add(new KillAuraD(data));
		checks.add(new KillAuraE(data));
		checks.add(new ReachA(data));
		checks.add(new HitboxA(data));
		checks.add(new InventoryA(data));
		checks.add(new InventoryB(data));
		checks.add(new InventoryC(data));
		checks.add(new TimerA(data));
		checks.add(new TimerB(data));
		checks.add(new AutoClickerA(data));
		data.setChecks(checks);
		
	}

}
