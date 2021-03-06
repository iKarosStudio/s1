package Asgardia.World.Objects.RoutineTasks;

import java.util.*;
import java.util.concurrent.*;

import Asgardia.Config.*;
import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Npc.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;
import Asgardia.World.Objects.Monster.MonsterInstance;

public class ObjectUpdate extends Thread implements Runnable
{
	//private final Timer t = new Timer () ;
	ScheduledFuture s;
	private PcInstance Pc;
	private SessionHandler Handle;

	public ObjectUpdate (PcInstance pc) {
		Pc = pc;
		Handle = Pc.getHandler () ;
	}
	
	public void run () {
		/*
		 * 更新視界各類物件
		 */
		Pc () ;
		Monster () ;
		Npc () ;
		Item () ;
		Door () ;
	}
	
	public void Pc () {
		/*
		 * 加入/移出不在清單內卻在視距內的物件
		 * 注意:不會自己將自身視為端點
		 */
		List<PcInstance> Pcs = Pc.getPcInsight () ;//取得視線中中所有玩家節點
		for (PcInstance Node : Pcs) {
			if (!Pc.PcInsight.containsKey (Node.Uuid) && (Node.Uuid != Pc.Uuid) ) {
				Pc.addPcInstance (Node) ;
				Handle.SendPacket (new NodePacket (Node).getRaw () ) ;
			}	
		}
		
		/*
		 * 玩家必須額外注意是否已經離線, 檢查socket close or thread is Alive
		 */
		Pc.PcInsight.forEach ( (Integer u, PcInstance p)->{
			//if (p.getHandler ().isClosed () || !Pc.isInsight (p)) {
			if (!Pc.isInsight (p) || !Pcs.contains (p) ) {
				Pc.removePcInsight (p) ;
				Handle.SendPacket (new RemoveObject (p.Uuid).getRaw () ) ;
			}
		}) ;
	}
	
	public void Npc () {
		List<NpcInstance> Npcs = Pc.getNpcInsight () ;
		for (NpcInstance NpcNode : Npcs) {
			if (!Pc.NpcInsight.containsKey (NpcNode.Uuid) ) {
				Pc.addNpcInstance (NpcNode) ;
				Handle.SendPacket (new NodePacket (NpcNode).getRaw () ) ;
			}
		}
		Npcs = null;
		
		Pc.NpcInsight.forEach ((Integer u, NpcInstance n)->{
			if (!Pc.isInsight (n) ) {
				Pc.removeNpcInsight (n) ;
				Handle.SendPacket (new RemoveObject (n.Uuid).getRaw () ) ;
			}
		});
	}
	
	public void Monster () {
		List<MonsterInstance> Mobs = Pc.getMonsterInsight () ;
		for (MonsterInstance m : Mobs) {
			if (!Pc.MonsterInsight.containsKey (m.Uuid) ) {
				Pc.addMonsterInstance (m) ;
				Handle.SendPacket (new NodePacket (m).getRaw () );
			}
		}
		Mobs = null;
		
		Pc.MonsterInsight.forEach ((Integer u, MonsterInstance m)->{
			/*
			if (m.isDead () ) {
				return;
			}
			*/
			if (!Pc.isInsight (m) ) {
				Pc.removeMonsterInsight (m) ;
				Handle.SendPacket (new RemoveObject (m.Uuid).getRaw () ) ;
			} else {
				m.ToggleAi () ;
			}
		}) ;
	}
	
	public void Item () {
		List<ItemInstance> Items = Pc.getItemInsight () ;
		for (ItemInstance i : Items) {
			if (!Pc.GndItemInsight.containsKey (i.Uuid) ) {
				Pc.addGndItemInstance (i) ;
				Handle.SendPacket (new NodePacket (i).getRaw () ) ;
			}
		}
		Items = null;
		
		Pc.GndItemInsight.forEach ((Integer u, ItemInstance i)->{
			if (!Pc.isInsight (i) ) {
				Pc.removeGndItemInsight (i) ;
				Handle.SendPacket (new RemoveObject (i.Uuid).getRaw () ) ;
			}
		});
	}
	
	public void Door () {
		List<DoorInstance> Doors = Pc.getDoorInsight () ;
		for (DoorInstance d : Doors) {
			if (!Pc.DoorInsight.containsKey (d.Uuid) ) {
				Pc.addDoorInstance (d) ;
				Handle.SendPacket (new NodePacket (d).getRaw () ) ;
				Handle.SendPacket (new DoorDetail (d).getRaw () ) ;
			}
		}
		Doors = null;
		
		Pc.DoorInsight.forEach ((Integer u, DoorInstance d)->{
			if (!Pc.isInsight (d) ) {
				Pc.removeDoorInsight (d) ;
				Handle.SendPacket (new RemoveObject (d.Uuid).getRaw () ) ;
			}
		});
	}
	
	public void Pet () {
	}
	
	public void Start () {
		s = KernelThreadPool.getInstance ().ScheduleAtFixedRate (this, 200, 500) ;
		//t.scheduleAtFixedRate (this, 200, 500) ; //1000ms interval
	}
	
	public void Stop () {
		s.cancel (true) ;
	}
}
