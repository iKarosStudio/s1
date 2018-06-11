package Asgardia.World.Objects.Monster;

import java.util.*;

import Asgardia.Config.*;
import Asgardia.Types.*;
import Asgardia.Server.*;
import Asgardia.World.Map.AsgardiaMap;

public class MonsterAiKernel extends TimerTask implements Runnable
{
	public static final int STOP = 0;
	public static final int IDLE = 1;
	public static final int ATTACK = 2;
	public static final int DEAD = 3;
	
	
	private static TimerPool SchedulePool;
	private static Random r = new Random (System.currentTimeMillis () ) ;
	AsgardiaMap Map;
	MonsterInstance Mob;
	
	public boolean isAiRunning = false;
	public int TimeoutCounter = 0;
	
	public void run () {
		try {
			//System.out.printf ("ai %d go\n", Mob.Uuid) ;
			
			Ai () ;
			
			//Thread.sleep (Configurations.MONSTER_AI_UPDATE_RATE) ;
			
			/*
			 * 死亡狀態處理
			 */
				
		} catch (Exception e) {
			System.out.printf ("map:%d\n", Map.MapId) ;
			e.printStackTrace () ;
		}
	}
	
	public MonsterAiKernel (MonsterInstance mob) {
		Mob = mob;
		Map = Mob.Map;
		if (SchedulePool == null) {
			//SchedulePool = new TimerPool (4) ;
		}
	}
	
	public boolean Ai () {

		try {
			while (isAiRunning) {
				//System.out.println ("確保AI線性處理") ;
				Thread.sleep (300) ;
			}
			
			//System.out.printf ("ai kernel mob:%s(0x%08X)->\n", Mob.Name, Mob.Uuid) ;
			
			isAiRunning = true;
			if (Mob.ActionStatus == STOP) {
				Thread.sleep (10000) ;
			} else if (Mob.ActionStatus == IDLE) {
				Mob.MoveToHeading (r.nextInt (8) ) ;
				Thread.sleep (Mob.MoveInterval) ;
				
			} else if (Mob.ActionStatus == ATTACK) {
				/* pseudo code-
				 * MoveToTarget (x, y)
				 * 	h=SetDirectiotn (x, y)
				 * 		MoveTo (h)
				 *			D=getDistance (target)
				 *		d<2->Attack
				 *		d>=2 MovetoTarget
				 */
				
			} else if (Mob.ActionStatus == DEAD) {
				//
				return false;
				
			} else {
				System.out.printf ("UNKNOWN AI STATUS MOB:%s(%d)\n", Mob.Name, Mob.Uuid) ;
			}
			isAiRunning = false;
			
		} catch (Exception e) {
			isAiRunning = false;
			System.out.printf ("%s -> map:%d(%d,%d)\n", Mob.Name, Mob.location.MapId, Mob.location.x, Mob.location.y) ;
			e.printStackTrace () ;
			System.exit (999) ;
		}
		
		return true;
	}
}
