package Asgardia.World.Objects.Monster;

import java.util.*;

public class MoveTask extends TimerTask implements Runnable
{
	MonsterInstance Mob;
	Random r = new Random (System.currentTimeMillis () ) ;
	
	public void run () {
		Mob.MoveToHeading (r.nextInt (8) ) ;
		System.out.println ("Move!") ;
	}
	
	public MoveTask (MonsterInstance mob) {
		Mob = mob;
	}
}
