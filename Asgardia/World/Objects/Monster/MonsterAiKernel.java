package Asgardia.World.Objects.Monster;

import java.util.*;

import Asgardia.Config.*;
import Asgardia.Types.*;
import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Map.AsgardiaMap;

public class MonsterAiKernel extends TimerTask implements Runnable
{
	public static final int ACTION_STOP = 0;
	public static final int ACTION_IDLE = 1;
	public static final int ACTION_ATTACK = 2;
	public static final int ACTION_DEAD = 3;
	
	
	//private static TimerPool SchedulePool;
	private static Random r = new Random (System.currentTimeMillis () ) ;
	AsgardiaMap Map;
	MonsterInstance Mob;
	
	public boolean isAiRunning = false;
	public int TimeoutCounter = 0;
	public int DeadTimeCounter = 0;
	
	public void run () {
		try {			
			/*
			 * 清空AI移除計時器
			 */
			TimeoutCounter = 0;
			
			/*
			 * 死亡檢查
			 */
			
			if (!Mob.isDead () ) {
				if (Mob.Hp < 1) {
					byte[] die = new NodeAction (8, Mob.Uuid, Mob.location.Heading).getRaw () ;
					
					//轉移經驗值與道具
					Mob.TransferExp (Mob.TargetPc) ;
					Mob.TransferItems () ;
					
					Mob.BoardcastPcInsight (die) ;
					
					Mob.setDead (true) ;
					Mob.ActionStatus = 3;
				}
			}
			
			/*
			 * 執行AI動作
			 */
			Ai () ;
			
		} catch (Exception e) {
			System.out.printf ("ai uuid : %d\n", Mob.Uuid) ;
			e.printStackTrace () ;
		}
	}
	
	public MonsterAiKernel (MonsterInstance mob) {
		Mob = mob;
		Map = Mob.Map;
	}
	
	public boolean Ai () {
		try {			
			isAiRunning = true;
			
			if (Mob.ActionStatus == ACTION_STOP) {
				Thread.sleep (500) ;
				
			} else if (Mob.ActionStatus == ACTION_IDLE) {
				Mob.MoveToHeading (r.nextInt (8) ) ;
				Thread.sleep (Mob.MoveInterval) ;
				
				/*
				 * 0~3S隨機停頓
				 */
				Thread.sleep (r.nextInt (3000) ) ;
				
			} else if (Mob.ActionStatus == ACTION_ATTACK) {
				if (Mob.TargetPc == null) {
					if (Mob.Agro) {
						//find target
					} else {
						Mob.ActionStatus = ACTION_IDLE;
					}
				} else {
					Mob.AttackPc (Mob.TargetPc) ;
					Thread.sleep (Mob.AttackInterval) ;
				}
				
			} else if (Mob.ActionStatus == ACTION_DEAD) {
				//
				
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
