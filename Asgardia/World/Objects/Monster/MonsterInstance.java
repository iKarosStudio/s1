package Asgardia.World.Objects.Monster;

import java.util.*;
import java.util.concurrent.*;

import Asgardia.Types.*;
import Asgardia.Config.*;
import Asgardia.Server.Utility.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.*;
import Asgardia.World.Skills.*;
import Asgardia.World.Map.*;
import Asgardia.World.Objects.Items.*;
import Asgardia.World.Objects.PcInstance;
import Asgardia.World.Objects.Dynamic.*;
import Asgardia.World.Objects.Template.*;

/*
 * 怪物實體
 */
public class MonsterInstance extends DynamicObject
{
	public String NameId; 
	public int Size;
	
	/* 
	 * 0:Stop
	 * 1:Idle(Roaming)
	 * 2:Attack(Attacking or Intent to attack)
	 * 3:Dead
	 */
	public int ActionStatus = 0;
	
	public int GroupId;

	public int RandomX;
	public int RandomY;
	public int MovementDistance;
	public int Rest; /* 復活次數 */
	
	public int MoveInterval = 0;
	public int AttackInterval = 0;
	public int MajorSkillInterval = 0;
	public int MinorSkillInterval = 0;
	
	public boolean Agro; /* 是否主動 */
	
	
	/*
	 * 怪物持有道具 <K, V> = <itemid, item>
	 */
	public ConcurrentHashMap<Integer, ItemInstance> Items = null;
	
	/*
	 * 怪物經驗值
	 */
	public int Exp;
	
	/*
	 * 仇恨清單
	 *  <K, V> = <仇恨對象UUID, 打擊次數>
	 */
	private int HateListTotal = 0;
	public ConcurrentHashMap<Integer, Integer> HateList;
	public PcInstance TargetPc = null;
	//TargetPet
	
	/*
	 * AI動作核心
	 */
	public MonsterAiKernel Aikernel;
	
	
	public MonsterInstance (NpcTemplate n, Location loc) {
		Uuid = n.Uuid;
		Gfx = n.Gfx;
		Name = n.Name;
		NameId = n.NameId;
		
		Level = n.Level;
		Exp = n.Exp;
		Size = n.Size;
		
		location.MapId = loc.MapId;
		location.x = loc.x;
		location.y = loc.y;
		location.Heading = loc.Heading;
		UpdateCurrentMap () ;
		
		BasicParameter = new CombatStatus () ;
		BasicParameter.MaxHp = n.BasicParameter.MaxHp;
		BasicParameter.MaxMp = n.BasicParameter.MaxMp;
		Hp = BasicParameter.MaxHp; Mp = BasicParameter.MaxMp;
		BasicParameter.Ac = n.BasicParameter.Ac;
		
		MoveInterval = n.MoveInterval;
		AttackInterval = n.AttackInterval;
		MajorSkillInterval = n.MajorSkillInterval;
		MinorSkillInterval = n.MinorSkillInterval;
		
		Agro = n.Agro;
		
		Items = new ConcurrentHashMap<Integer, ItemInstance> () ;
	}
	
	public synchronized void MoveToHeading (int heading) {
		
		int px = location.x;
		int py = location.y;
		
		switch (heading) {
		case 0: py--; break;
		case 1: px++; py--; break;
		case 2: px++; break;
		case 3: px++; py++; break;
		case 4: py++; break;
		case 5: px--; py++; break;
		case 6: px--; break;
		case 7: px--; py--; break;
		default: break;
		}
		//上段需要優化
		
		if (Map.isNextTileAccessible (location.x, location.y, heading) ) {			
			Map.setAccessible (location.x, location.y, true) ;
			
			List<PcInstance> pcs = Map.getPcInstance (location.x, location.y) ;
			byte[] MovePacket = new NodeMove (Uuid, location.x, location.y, heading).getRaw () ;
			for (PcInstance pc : pcs) {
				if (!pc.getHandler ().isClosed () ) {
					pc.getHandler ().SendPacket (MovePacket) ;
				} else {
					System.out.printf ("error pc handler\n") ;
				}
			}
			
			//update loc
			Map.setAccessible (px, py, false) ;
			location.x = px;
			location.y = py;
			location.Heading = heading;
		} else {
			//can't pass
			return ;
		}
	}
	
	public void SetAttactTarget (int uuid) {
	}
	
	public void AttackPc (PcInstance p) {
		System.out.printf ("%s 嘗試攻擊 %s, ", Name, TargetPc.Name) ;
		location.Heading = getDirection (p.location.x, p.location.y) ;
		
		/*
		 * 更新攻擊距離
		 */
		if (getDistance (p.location.x, p.location.y) > 1) {
			MoveToHeading (location.Heading) ;
			return;
		}
		
		/*
		 * 顯示攻擊動作
		 */
		
		byte[] action_packet = new NodeAction (1, Uuid, location.Heading).getRaw () ;
		BoardcastPcInsight (action_packet) ;
		
		/*
		 * 命中與傷害運算
		 */
		if (isInsight (p) ) {
			new CommonAttack (this, p) ;
		}
	}
	
	public void AttackPet () {
	}
	
	synchronized public void TakeDamage (int dmg) {		
		if (Hp > dmg) {
			Hp -= dmg;
		} else {
			/*
			try {
				Aikernel.wait () ;
			} catch (Exception e) {e.printStackTrace () ; }
			*/
			Hp = 0;
			setDead (true) ;
			ActionStatus = 3;
			System.out.printf ("%s 死了!\n", Name) ;
		}
	}
	
	public void BoardcastPcInsight (byte[] Data) {
		List<PcInstance> pcs = Map.getPcInstance (location.x, location.y) ;			
		for (PcInstance p : pcs) {
			p.getHandler ().SendPacket (Data) ;
		}
	}
	
	public void ToggleAi () {
		if (Aikernel == null) {
			Aikernel = new MonsterAiKernel (this) ;
		} else {
			//Clear timeout counter			
			if (Aikernel.isAiRunning) {
				/*
				 * 同步問題, AI工作位睡眠結束就被執行
				 */
				return;
				
			} else {
				/*
				 * AI工作加入Queue等候執行
				 */
				MonsterAiQueue.getInstance ().getQueue ().offer (Aikernel) ;
			}
		}
	}
	
	public void ToggleHateList (PcInstance p, int dmg) {
		HateListTotal ++;
		if (HateList == null) {
			HateList = new ConcurrentHashMap<Integer, Integer> () ;
		}
		
		if (HateList.containsKey (p.Uuid) ) {
			int hate = HateList.get (p.Uuid) + 1;
			HateList.put (p.Uuid, hate) ;
		} else {
			HateList.put (p.Uuid, 1) ;
		}
		
	}
	
	public void TransferExp (PcInstance p) {
		int MonsterExp = Exp * Configurations.RateExp;
		
		System.out.printf ("%s 經驗值轉移:\n", Name) ;
		
		HateList.forEach ((Integer pc, Integer h)->{
			PcInstance recv = Map.getPc (pc) ;
			recv.Exp += MonsterExp * h / HateListTotal;
			
			System.out.printf ("\tHateList->%s 分到 %d經驗值\n", recv.Name, MonsterExp * h / HateListTotal) ;
			recv.getHandler ().SendPacket (new UpdateExp (recv).getRaw () ) ;
		}) ;
	}
	
	public void TransferLawful () {
		//
	}
	
	public void TransferItems () {
		
		System.out.printf ("%s 道具轉移(Total:%d):\n", Name, HateListTotal) ;
		
		Items.forEach ((Integer u, ItemInstance i)->{
			HateList.forEach ((Integer p, Integer h)->{
				PcInstance recv = Map.getPc (p) ;
				
				System.out.printf ("\tHatelist->%s : %d 分到 %s\n", recv.Name, h, i.getName () ) ;
				
				/*
				 * 道具真實UUID
				 */
				i.Uuid = UuidGenerator.Next () ;
				i.OwnerId = recv.Uuid;
				recv.addItem (i) ;
				
				String[] ServerMessage = {Name, i.getName () } ;
				byte[] Msg = new ServerMessage (143, ServerMessage).getRaw () ;
				//超過20E金幣丟msgid:166
				recv.getHandler ().SendPacket (Msg) ;
				
			}) ;
			
			Items.remove (u) ;
		});		
	}
	
	public boolean isStoped () {
		return ActionStatus == 0;
	}
	
	public boolean isRoaming () {
		return ActionStatus == 1;
	}
	
	public boolean isAttacking () {
		return ActionStatus == 2;
	}
	
	public boolean isDead () {
		return ActionStatus == 3;
	}
	
	
}
