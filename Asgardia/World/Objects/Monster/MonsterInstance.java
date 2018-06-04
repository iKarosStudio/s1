package Asgardia.World.Objects.Monster;

import java.util.*;
import java.util.concurrent.*;

import Asgardia.Types.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.*;
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
	
	
	/*
	 * 怪物持有道具
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
	public ConcurrentHashMap<Integer, Integer> HateList;
	
	public MonsterAiKernel AiController;
	public MoveTask Move;
	
	public MonsterInstance (NpcTemplate n, Location loc) {
		Uuid = n.Uuid;
		Gfx = n.Gfx;
		Name = n.Name;
		NameId = n.NameId;
		
		Level = n.Level;
		Exp = n.Exp;
		
		location = loc;
		UpdateCurrentMap () ;
		
		BasicParameter = new QualityParameters () ;
		BasicParameter.MaxHp = n.BasicParameter.MaxHp;
		BasicParameter.MaxMp = n.BasicParameter.MaxMp;
		BasicParameter.Ac = n.BasicParameter.Ac;
		
		MoveInterval = n.MoveInterval;
		AttackInterval = n.AttackInterval;
		MajorSkillInterval = n.MajorSkillInterval;
		MinorSkillInterval = n.MinorSkillInterval;
		
		Items = new ConcurrentHashMap<Integer, ItemInstance> () ;
		
		ActionStatus = 1; //roaminig
		AiController = new MonsterAiKernel (this) ;
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
		
		if (Map.isNextTileAccessible (location.x, location.y, heading) ) {
			
			//System.out.printf ("Move->(%d,%d)=0x%02x", px, py, Map.getTile (px, py) ) ;
			
			Map.setAccessible (location.x, location.y, true) ;
			
			List<PcInstance> pcs = Map.getPcInstance (location.x, location.y) ;
			byte[] MovePacket = new NodeMove (Uuid, location.x, location.y, heading).getRaw () ;
			for (PcInstance pc : pcs) {
				pc.getHandler ().SendPacket (MovePacket) ;
			}
			
			//update loc
			Map.setAccessible (px, py, false) ;
			location.x = px;
			location.y = py;
			location.Heading = heading;
		} else {
			//System.out.printf ("next p(%d,%d) can't pass", px, py) ;
			return ;
		}
	}
	
	public void SetAttactTarget (int uuid) {
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
