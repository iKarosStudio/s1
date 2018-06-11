package Asgardia.World.Objects.Monster;

import java.util.concurrent.*;

import Asgardia.Server.*;
import Asgardia.World.Map.*;

/*
 * 抓地圖中的怪物AI執行需求塞到Queue
 */
public class MonsterAiDistributor implements Runnable
{
	private AsgardiaMap Map;
	private ScheduledFuture<?> t;
	private static java.util.Queue<Runnable> queue;
	
	public void run () {
		Map.Monsters.forEach ((Integer u, MonsterInstance m)->{
			//主動模式
			/*
			if (!queue.contains (m.Aikernel) ) {
				queue.offer (m.Aikernel) ;
			}*/
			
			//被動模式
			if (m.Aikernel != null) {
				if (m.Aikernel.TimeoutCounter < 60) {
					m.Aikernel.TimeoutCounter++;
				} else {
					m.Aikernel = null;
				}
			}
			
			if (m.Hp < 1) {
				m.isDead = true;
			}
		});
	}
	
	public MonsterAiDistributor (AsgardiaMap map) {
		Map = map;
		queue = MonsterAiQueue.getInstance ().getQueue () ;
	}
	
	public void Start () {
		t = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 100, 500) ;
	}
	
	public void Stop () {
		t.cancel (false) ;
	}
}