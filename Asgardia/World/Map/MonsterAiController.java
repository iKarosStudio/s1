package Asgardia.World.Map;

import java.util.Timer;
import java.util.TimerTask;
import java.util.*;

import Asgardia.Server.*;
import Asgardia.World.Objects.MonsterInstance;

public class MonsterAiController extends TimerTask implements Runnable
{
	Timer AiTimer = new Timer () ;
	AsgardiaMap Map;
	Random Rand;
	MonsterInstance Mob;
	
	public void run () {
		try {
			/*
				這邊應該改成AI finite state machine
				配合timerpool排程
			*/
			
			List<MonsterInstance> Mobs = Map.getAllMonster () ;
			if (Mobs.size () > 0){ //有怪物的地圖
				for (MonsterInstance mob : Mobs) {
					int RandHeading = Rand.nextInt (9) ;
					
					mob.MoveToHeading (RandHeading) ;
					//mob.MoveToHeading (1) ;
				}
			}			
		} catch (Exception e) {
			e.printStackTrace () ;
			//System.console ().printf ("pause") ;
		}
	}
	
	public MonsterAiController (MonsterInstance mob) {
		Mob = mob;
		Map = Mob.Map;
		Rand = new Random (System.currentTimeMillis () ) ;
		
		//AiTimer.scheduleAtFixedRate (this, 0, 3000);
		//KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 0, 1000) ;
		System.out.printf ("create mob:%d ai kernel\n", Mob.Uuid) ;
	}
}
