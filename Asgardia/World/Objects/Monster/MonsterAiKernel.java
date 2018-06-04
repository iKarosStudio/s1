package Asgardia.World.Objects.Monster;

import java.util.*;

import Asgardia.Types.*;
import Asgardia.Server.*;
import Asgardia.World.Map.AsgardiaMap;

public class MonsterAiKernel extends TimerTask implements Runnable
{
	public static final int STOP = 0;
	public static final int IDLE = 1;
	public static final int ATTACK = 2;
	public static final int DEAD = 3;
	
	
	TimerPool SchedulePool;
	Random r = new Random (System.currentTimeMillis () ) ;
	AsgardiaMap Map;
	MonsterInstance Mob;
	
	public void run () {
		try {
			while (Mob.Hp > 0) {
				if (!AiKernel () ) {
					break;
				}
				Thread.sleep (300) ;
			}
			
			/*
			 * 死亡狀態處理
			 */
				
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public MonsterAiKernel (MonsterInstance mob) {
		Mob = mob;
		Map = Mob.Map;
		SchedulePool = new TimerPool (4) ;
	}
	
	
	public void Start () {
		KernelThreadPool.getInstance ().execute (this) ;
	}
	
	public void Stop () {
		this.cancel () ;
	}
	
	public boolean AiKernel () {
		//System.out.printf ("ai kernel mob:%s(%d)->", Mob.Name, Mob.Uuid) ;
		
		try {
			if (Mob.ActionStatus == STOP) {
				Thread.sleep (10000) ;
			} else if (Mob.ActionStatus == IDLE) {
				
				Mob.MoveToHeading (r.nextInt (8) ) ;
				Thread.sleep (Mob.MoveInterval) ;
				
			} else if (Mob.ActionStatus == ATTACK) {
				//
				
			} else if (Mob.ActionStatus == DEAD) {
				//
				return false;
				
			} else {
				System.out.printf ("UNKNOWN AI STATUS MOB:%s(%d)\n", Mob.Name, Mob.Uuid) ;
			}
		} catch (Exception e) {
			e.printStackTrace () ;
		}
		
		return true;
	}
}
