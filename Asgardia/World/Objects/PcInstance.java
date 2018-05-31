package Asgardia.World.Objects;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

import Asgardia.Config.*;
import Asgardia.Server.*;
import Asgardia.Server.Utility.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.*;
import Asgardia.World.Map.*;
import Asgardia.World.Npc.*;
import Asgardia.World.Skills.*;
import Asgardia.World.Objects.Dynamic.DynamicObject;
import Asgardia.World.Objects.Items.*;
import Asgardia.World.Objects.RoutineTasks.*;
import Asgardia.World.Objects.Template.*;

/*
 * 玩家腳色實例(Players)
 */
public class PcInstance extends DynamicObject implements Runnable
{	
	private SessionHandler Handle;
	public boolean exit = false;
	
	/* 持有道具 */
	public ConcurrentHashMap<Integer, ItemInstance> Item = null; // <uuid, iteminstance>
	public ItemInstance UsingWeapon = null;
	
	/* 視界內物件 */
	public ConcurrentHashMap<Integer, PcInstance> PcInsight = null;
	public ConcurrentHashMap<Integer, NpcInstance> NpcInsight = null;
	public ConcurrentHashMap<Integer, MonsterInstance> MonsterInsight = null;
	public ConcurrentHashMap<Integer, ItemInstance> GndItemInsight = null;
	public ConcurrentHashMap<Integer, DoorInstance> DoorInsight = null;
	public ConcurrentHashMap<Integer, PetInstance> Pet = null;
	//private ConcurrentHashMap<Integer, SlaveMonster> SummonMonster = null;
	
	/* Buff/Debuff 效果計時 */
	public SkillEffectTimer SkillTimer = null;
	
	public SystemTick Tick;
	public PcRoutineTasks RoutineTask;
	public ObjectUpdate Ou;
	public ExpMonitor ExpKeeper;
	public HpMonitor HpKeeper;
	public MpMonitor MpKeeper;
	
	public int AccessLevel = 0; //Priority control
	public boolean isGm = false;
	public boolean isRd = false;
	
	public int Exp = 0;
	public int ExpRes = 0;
	
	public int ClanId = 0;
	public String ClanName = null;
	
	QualityParameters EquipPara;
	
	public int Sex = 0;
	public int Type = 0; /* 0:Royal 1:Knight 2:Elf 3:Mage 4:Darkelf */
	
	public int Satiation = 0; //飽食度
	public int PkCount = 0;
	
	public void run () {
		//SaveToDatabase () ;
		System.out.println (Name + "Routine task...") ;
	}
	
	public PcInstance () {
		//建立角色用
		//pseudo instance
		BasicParameter = new QualityParameters () ;
		
		PcInsight = new ConcurrentHashMap<Integer, PcInstance> () ;
		NpcInsight = new ConcurrentHashMap<Integer, NpcInstance> () ;
		MonsterInsight = new ConcurrentHashMap<Integer, MonsterInstance> () ;
		GndItemInsight = new ConcurrentHashMap<Integer, ItemInstance> () ;
		DoorInsight = new ConcurrentHashMap<Integer, DoorInstance> () ;
	}
	
	public PcInstance (SessionHandler Handle) {
		this.Handle = Handle;
		
		PcInsight = new ConcurrentHashMap<Integer, PcInstance> () ;
		NpcInsight = new ConcurrentHashMap<Integer, NpcInstance> () ;
		MonsterInsight = new ConcurrentHashMap<Integer, MonsterInstance> () ;
		GndItemInsight = new ConcurrentHashMap<Integer, ItemInstance> () ;
		DoorInsight = new ConcurrentHashMap<Integer, DoorInstance> () ;
		//Item = new ConcurrentHashMap () ;
		//Pet = new ConcurrentHashMap () ;
		//SummonMonster = new ConcurrentHashMap () ;
	}
	
	//參照MysqlCharacterStorage.java (l1j270)
	public boolean Load (String CharName)
	{
		HikariCP db = Handle.getDbHandle () ;
		
		try {
			String q = String.format ("SELECT * FROM characters WHERE char_name=\'%s\'" , CharName) ; 
			ResultSet rs = db.Query (q) ;
			
			if (rs.next () ) {
				location.MapId = rs.getInt ("MapID") ;
				location.x = rs.getInt ("LocX") ;
				location.y = rs.getInt ("LocY") ;
				location.Heading = rs.getInt ("Heading") ;
				
				if (location.MapId > MapLoader.MAPID_LIMIT) {
					location.MapId = 0;
				}
				
				Uuid = rs.getInt ("objid") ;
				Name = rs.getString ("char_name") ;
				Title = rs.getString ("Title") ;
				ClanId = rs.getInt ("ClanID") ;
				if (ClanId > 0) {
					ClanName = rs.getString ("Clanname") ;
				}
				
				Level = rs.getInt ("level") ;
				Exp = rs.getInt ("Exp") ;
				ExpRes = rs.getInt ("ExpRes") ;
				
				Sex = rs.getInt ("Sex") ;
				Type = rs.getInt ("Type") ;
				Gfx = rs.getInt ("Class") ;
				TempGfx = Gfx;
				
				BasicParameter = new QualityParameters () ;
				EquipPara = new QualityParameters () ;
				SkillParameter = new QualityParameters () ;
				
				BasicParameter.Str = rs.getInt ("Str") ;
				BasicParameter.Con = rs.getInt ("Con") ;
				BasicParameter.Dex = rs.getInt ("Dex") ;
				BasicParameter.Wis = rs.getInt ("Wis") ;
				BasicParameter.Cha = rs.getInt ("Cha") ;
				BasicParameter.Intel = rs.getInt ("Intel") ;
				
				//load bounes parameters
				
				Satiation = rs.getInt ("Food") ;
				BasicParameter.Ac = rs.getInt ("Ac") ;
				Status = rs.getInt ("Status") ;
				Status |= 4;
				
				Hp = rs.getInt ("CurHp") ;
				Mp = rs.getInt ("CurMp") ;
				BasicParameter.MaxHp = rs.getInt ("MaxHp") ;
				BasicParameter.MaxMp = rs.getInt ("MaxMp") ;
				
				PkCount = rs.getInt ("PKcount") ;
				
				
				Tick = new SystemTick (this) ;
				SkillTimer = new SkillEffectTimer (this) ;
				RoutineTask = new PcRoutineTasks (this) ;
				ExpKeeper = new ExpMonitor (this) ;		
				HpKeeper = new HpMonitor (this) ;
				MpKeeper = new MpMonitor (this) ;
				
				this.UpdateCurrentMap () ;
				
				return true;
				
			} else {
				return false;
			}
			
		} catch (Exception e) {
			System.out.println (e.toString () ) ;
		}
		return false;
	}
	
	public void LoadItem () {
		Item = new ConcurrentHashMap<Integer, ItemInstance> () ;
		ResultSet rs = DatabaseCmds.LoadItem (Uuid) ;
		try {
			while (rs.next () ) {
				ItemInstance i = new ItemInstance (
					rs.getInt ("id"),
					rs.getInt ("item_id"),
					rs.getInt ("char_id"),
					rs.getInt ("count"),
					rs.getInt ("enchantlvl"),
					rs.getInt ("durability"),
					rs.getInt ("charge_count"),
					rs.getBoolean ("is_equipped"),
					rs.getBoolean ("is_id")
				) ;
				Item.put (rs.getInt ("id"), i) ;
				
				if ((i.MajorType == 1) && (i.IsEquipped) ) {
					UsingWeapon = i;
				}
			}
			
			Handle.SendPacket (new ReportHeldItem(Item).getRaw () ) ;
		} catch (Exception e) {e.printStackTrace () ; }
	}
	
	public void LoadBuff () {
		//
	}
	
	public List<ItemInstance> FindItem (int ItemId) {
		ArrayList<ItemInstance> Found = new ArrayList<ItemInstance> () ;
		
		Item.forEach ((Integer uuid, ItemInstance item)->{
			if (item.ItemId == ItemId) {
				Found.add (item) ;
			}
		});
		
		return Found;
	}
	
	public ItemInstance FindItemByUuid (int uuid) {
		ItemInstance i = Item.get (uuid) ;
		return i;
	}
	
	public int getMoney () {
		int Money = 0;
		
		List<ItemInstance> Find = FindItem (40308) ; //找金幣
		if (Find.size () > 0) {
			Money = Find.get (0).Count ;
		} 
		return Money;
	}
	
	public int getWeight () {
		int TotalWeight = 0;
		ArrayList<ItemInstance> AllItems = new ArrayList<ItemInstance> () ;
		Item.forEach ((Integer uuid, ItemInstance item)->{
			AllItems.add (item) ;
		});
		
		for (ItemInstance i : AllItems) {
			TotalWeight += i.Weight * i.Count;
		}
		
		return TotalWeight;
	}
	
	public int getMaxWeight () {
		int max_wieght = 1500 + (((getStr () + getCon () - 18) >> 1) * 150) ;
		//apply skill effect
		//apply equip effect
		//apply doll effect
		
		return max_wieght * 1000;
	}
	
	public int getWeightInScale30 () {
		return (getWeight () * 100) / (int) (getMaxWeight () * 3.4) ;
	}
	
	public int getWeaponGfx () {		
		ArrayList<ItemInstance> Found = new ArrayList<ItemInstance> () ;
		int Gfx = 0;
		
		try {
			Item.forEach ((Integer uuid, ItemInstance item)->{
				if ((item.MajorType == 1) && item.IsEquipped) {
					Found.add (item) ;
				}
			}) ;
			
			if (Found.size () > 0) {
				Gfx = ItemTypeTable.WeaponGfxTable[Found.get (0).MinorType] ;
			}
		} catch (Exception e) {e.printStackTrace () ; }
		
		return Gfx;
	}
	
	public int getAc () {
		return BasicParameter.Ac + EquipPara.Ac + SkillParameter.Ac;
	}
	
	public int getStr () {
		return BasicParameter.Str + EquipPara.Str + SkillParameter.Str;
	}
	
	public int getCon () {
		return BasicParameter.Con + EquipPara.Con + SkillParameter.Con;
	}
	
	public int getDex () {
		return BasicParameter.Dex + EquipPara.Dex + SkillParameter.Dex;
	}
	
	public int getWis () {
		return BasicParameter.Wis + EquipPara.Wis + SkillParameter.Wis;
	}
	
	public int getCha () {
		return BasicParameter.Cha + EquipPara.Cha + SkillParameter.Cha;
	}
	
	public int getIntel () {
		return BasicParameter.Intel + EquipPara.Intel + SkillParameter.Intel;
	}
	
	public int getMaxHp () {
		return BasicParameter.MaxHp + EquipPara.MaxHp + SkillParameter.MaxHp;
	}
	
	public int getMaxMp () {
		return BasicParameter.MaxMp + EquipPara.MaxMp + SkillParameter.MaxMp;
	}
	
	public int getSp () {
		return BasicParameter.Sp + EquipPara.Sp + SkillParameter.Sp;
	}
	
	public int getMr () {
		return BasicParameter.Mr + EquipPara.Mr + SkillParameter.Mr;
	}
	
	public int getDirection (int x, int y) {
		byte Dir = 0;
		
		//int Dist = Utility.getDistance (PosX, PosY, x, y) ;
		
		//if (Dist < 2) {
			if (location.x == x && location.y == y) {
				return location.Heading;
			} else {
				if ( (x != location.x) && (y != location.y) ) {
					Dir |= 0x01;
				}
				
				if ( ((x > location.x) && !(y < location.y)) || ((x < location.x) && !(y > location.y)) ) {
					Dir |= 0x02;
				}
				
				if ( ((x == location.x) && (y > location.y)) || (x < location.x) ) {
					Dir |= 0x04;
				}
			}
		//}
		return Dir & 0x0FF;
	}
	
	public List<PcInstance> getPcInsight () {
		List<PcInstance> Results = Map.getPcInstance (location.x, location.y) ;
		return Results;
	}
	
	public List<NpcInstance> getNpcInsight () {
		List<NpcInstance> Results = Map.getNpcInstance (location.x, location.y) ;
		return Results;
	}
	
	public List<MonsterInstance> getMonsterInsight () {
		List<MonsterInstance> Results = Map.getMonsterInstance (location.x, location.y) ;
		return Results;
	}
	
	public List<ItemInstance> getItemInsight () {
		List<ItemInstance> Results = Map.getGndItemInstance (location.x, location.y) ;
		return Results;
	}
	
	public List<DoorInstance> getDoorInsight () {
		List<DoorInstance> Results = Map.getDoorInstance (location.x, location.y) ;
		return Results;
	}

	public SessionHandler getHandler () {
		return Handle;
	}
	
	public void setHandler (SessionHandler handle) {
		Handle = handle;
	}
	
	public void Offline () {
		Asgardia.getInstance ().removePc (this) ;
		//BoardcastPcInsight (new RemoveObject(Uuid).getRaw () ) ;
		
		Tick.Stop () ;
		SkillTimer.Stop () ;
		Ou.Stop () ;
		RoutineTask.Stop () ;
		ExpKeeper.Stop () ;
		HpKeeper.Stop () ;
		MpKeeper.Stop () ;
		
		DatabaseCmds.SavePc (this) ;
	}
	
	/*
	 * 將物件加入自身辨識清單
	 */
	public void addPcInstance (PcInstance Pc) {
		PcInsight.putIfAbsent (Pc.Uuid, Pc) ;
	}
	
	public void addNpcInstance (NpcInstance Npc) {
		NpcInsight.putIfAbsent (Npc.Uuid, Npc) ;
	}
	
	public void addMonsterInstance (MonsterInstance Mob) {
		MonsterInsight.putIfAbsent (Mob.Uuid, Mob) ;
	}
	
	public void addGndItemInstance (ItemInstance item) {
		GndItemInsight.putIfAbsent (item.Uuid, item) ;
	}
	
	public void addDoorInstance (DoorInstance door) {
		DoorInsight.putIfAbsent (door.Uuid, door) ;
	}

	/*
	 * 將物件移除自身辨識清單
	 */
	public void removePcInsight (PcInstance Pc) {
		PcInsight.remove (Pc.Uuid) ;
	}
	
	public void removeNpcInsight (NpcInstance Npc) {
		NpcInsight.remove (Npc.Uuid) ;
	}
	
	public void removeMonsterInsight (MonsterInstance Mob) {
		MonsterInsight.remove (Mob.Uuid) ;
	}
	
	public void removeDoorInsight (DoorInstance Door) {
		DoorInsight.remove (Door.Uuid) ;
	}
	
	public void removeGndItemInsight (ItemInstance i) {
		GndItemInsight.remove (i.Uuid) ;
	}
	
	public void removeAllInsight () {
		PcInsight.clear () ;
		NpcInsight.clear () ;
		GndItemInsight.clear () ;
		DoorInsight.clear () ;
	}
	
	public void addSkillEffect (int skill_id, int remain_time) {
		SkillEffect sk = new SkillEffect (skill_id, remain_time) ;
		SkillTimer.Effect.put (skill_id, sk) ;
	}
	
	public void removeSkillEffect(int skill_id) {
		if (SkillTimer.Effect.containsKey (skill_id) ) {
			SkillTimer.Effect.get(skill_id).RemainTime = 0;
		}
	}
	
	public boolean CanRecognizePc (PcInstance p) {
		return PcInsight.containsValue (p) ;
	}
	
	public void BoardcastAll (byte[] Packet) {
		List<PcInstance> Pcs = Asgardia.getInstance ().getAllPc () ;
		for (PcInstance p : Pcs) {
			p.Handle.SendPacket (Packet) ;
		}
	}
	
	public void BoardcastMap (byte[] Packet) {
		//
	}
	
	public void BoardcastPcInsight (byte[] Packet) {
		PcInsight.forEach ((Integer u, PcInstance pc)->{
			pc.Handle.SendPacket (Packet) ;
		}) ;
	}
	/*
	public void BoardcastPcInsightExceptSelf (byte[] Packet) {
		PcInsight.forEach ((Integer u, PcInstance pc)->{
			if (this != pc) {
				pc.Handle.SendPacket (Packet) ;
			}
		}) ;
	}
	*/
	public void BoardcastNpcInsight (byte[] Packet) {
		//
	}
	
	public synchronized void addItem (ItemInstance item) {
		List<ItemInstance> find_item = FindItem (item.ItemId) ;
		
		if (item.isStackable () && find_item.size () > 0) {
			ItemInstance i = Item.get (find_item.get (0).Uuid) ;
			i.Count += item.Count;
			DatabaseCmds.UpdatePcItem (i);
			
			Handle.SendPacket (new ItemUpdateAmount(i).getRaw () ) ;
			Handle.SendPacket (new ItemUpdateName(i).getRaw () ) ;
			Handle.SendPacket (new ItemUpdateStatus(i).getRaw () );
		} else {
			Item.put (item.Uuid, item) ;
			DatabaseCmds.InsertPcItem (item) ;
			
			Handle.SendPacket (new ItemInsert (item).getRaw () ) ;
		}
	}
	
	public synchronized void removeItemByItemId (int item_id, int amount) {
		List<ItemInstance> Found = FindItem (item_id) ;
		if (Found.size () > 0) {
			ItemInstance item = Found.get (0) ;
			removeItem (item.Uuid, amount) ;
		}
	}
	
	public synchronized void removeItem (ItemInstance item) {
		removeItem (item.Uuid, item.Count) ;
	}
	
	public synchronized void removeItem (int uuid) {
		removeItem (uuid, 0xFFFFFFFF) ;
	}
	
	public synchronized void removeItem (int uuid, int amount) {
		HikariCP Db = Handle.getDbHandle () ;
		
		if (Item.containsKey (uuid) ) {
			ItemInstance item = Item.get (uuid) ;
			
			if (item.Count > amount) {
				item.Count -= amount;
				DatabaseCmds.UpdatePcItem (item) ;
				Handle.SendPacket (new ItemUpdateAmount(item).getRaw () ) ;
				Handle.SendPacket (new ItemUpdateName(item).getRaw () ) ;
				Handle.SendPacket (new ItemUpdateStatus(item).getRaw () );
			} else {
				Item.remove (item.Uuid) ;
				DatabaseCmds.DeletePcItem (item) ;
				Handle.SendPacket (new ItemRemove (item).getRaw () ) ;
			}
		}
	}
	
	public void EquipWeapon (int uuid) {
		System.out.printf ("裝備武器 %d\n", uuid) ;
		
		if (UsingWeapon != null) {
			if (UsingWeapon.Uuid == uuid) {
				UsingWeapon.IsEquipped = false;
				Handle.SendPacket (new ItemUpdateName(UsingWeapon).getRaw () ) ;
				DatabaseCmds.UpdatePcItem (UsingWeapon) ;
				UsingWeapon = null;
			} else {
				UsingWeapon.IsEquipped = false;
				Handle.SendPacket (new ItemUpdateName(UsingWeapon).getRaw () ) ;
				DatabaseCmds.UpdatePcItem (UsingWeapon) ;
				UsingWeapon = FindItemByUuid (uuid) ;
				UsingWeapon.IsEquipped = true;
				Handle.SendPacket (new ItemUpdateName(UsingWeapon).getRaw () ) ;
				DatabaseCmds.UpdatePcItem (UsingWeapon) ;
			}
		} else {
			UsingWeapon = FindItemByUuid (uuid) ;
			UsingWeapon.IsEquipped = true;
			Handle.SendPacket (new ItemUpdateName(UsingWeapon).getRaw () ) ;
			DatabaseCmds.UpdatePcItem (UsingWeapon) ;
		}
	}
	
	public void EquipArmor (int uuid) {
		System.out.printf ("裝備防具 %d\n", uuid) ;
	}
	
	public void pickItem (int uuid, int count, int x, int y) {
		ItemInstance pick = Map.getGndItemInstanceByUuid (uuid) ;
		if (pick != null) {
			byte[] packet = new RemoveObject (pick.Uuid).getRaw () ;
			byte[] action = new NodeAction (15, Uuid, location.Heading).getRaw () ;
			
			Handle.SendPacket (packet) ;
			Handle.SendPacket (action) ;
			BoardcastPcInsight (packet) ;
			BoardcastPcInsight (action) ;
			
			pick.OwnerId = Uuid;
			addItem (pick) ;
			Map.removeGndItem (pick) ;
		}
	}
	
	public void dropItem (int uuid, int amount, int x, int y) {
		HikariCP Db = Handle.getDbHandle () ;
		
		if (Item.containsKey (uuid) ) {
			ItemInstance item = Item.get (uuid) ;
			ItemInstance drop = null;
			
			if (item.Count > amount) {
				item.Count -= amount;
				
				drop = new ItemInstance (
					UuidGenerator.Next (),
					item.ItemId,
					0, //char uuid
					amount,
					item.Enchant,
					item.Durability,
					item.ChargeCount,
					false,
					false
				) ;
				
				DatabaseCmds.UpdatePcItem (item) ;
				Handle.SendPacket (new ItemUpdateAmount(item).getRaw () ) ;
				Handle.SendPacket (new ItemUpdateName(item).getRaw () ) ;
				Handle.SendPacket (new ItemUpdateStatus(item).getRaw () );
			} else {
				drop = new ItemInstance (
					UuidGenerator.Next (),
					item.ItemId,
					0, //char uuid
					item.Count,
					item.Enchant,
					item.Durability,
					item.ChargeCount,
					false,
					false
				) ;
				
				DatabaseCmds.DeletePcItem (item) ;
				Handle.SendPacket (new ItemRemove (item).getRaw () ) ;

				Item.remove (item.Uuid) ;
			}
			
			drop.location.x = x;
			drop.location.y = y;
			drop.location.MapId = location.MapId;
			
			Map.addGndItem (drop) ;
		}
	}
	
	public boolean isFacing (int uuid) {
		return false;
	}
	
	public boolean isFace2Face (int uuid) {
		return false;
	}
	
	public boolean isPoisioned () {
		return (Status & 0x01) > 0 ; //check bit 0
	}
	
	public boolean isInvisible () {
		return (Status & 0x02) > 0 ; //check bit 1
	}
	
	public boolean isFrozen () {
		return (Status & 0x08) > 0 ; //check bit 3
	}
	
	public boolean isBraved () {
		return (Status & 0x10) > 0 ; //check bit 4
	}
	
	public boolean isElfBraved () {
		return (Status & 0x20) > 0 ; //check bit 5
	}
	
	public boolean isFastMove () {
		return (Status & 0x40) > 0 ; //check bit 6
	}
	
	public boolean isGhost () {
		return (Status & 0x80) > 0 ; //check bit 7
	}
	
	public boolean isRoyal () {
		return Type == 0;
	}
	
	public boolean isKnight () {
		return Type == 1;
	}
	
	public boolean isElf () {
		return Type == 2;
	}
	
	public boolean isMage () {
		return Type == 3;
	}
	
	public boolean isDarkElf () {
		return Type == 4;
	}

	public void Attack (int TargetUuid) {
		byte[] action_packet= new NodeAction (1, Uuid, location.Heading).getRaw () ;
		
		Handle.SendPacket (action_packet) ;
		BoardcastPcInsight (action_packet) ;
	}
	
	public void SaveItem () {
		//
	}
	
	public void UpdateParameters () {
		//
	}
	
	public void UpdateOnlineStatus (boolean isOnline) {
		HikariCP Db = Handle.getDbHandle () ;
		String Cmd = String.format ("UPDATE characters SET OnlineStatus=\'%d\' where objid=\'%d\';",
				(isOnline) ? 1:0,
				Uuid) ;
		try {
			Db.Insert (Cmd) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public void LoadTeleportBookmark () {
		ResultSet rs = DatabaseCmds.LoadTeleportBookmark (Uuid) ;
		try {
			while (rs.next () ) {
				//回報記憶傳送資訊
			}
		} catch (Exception e) {e.printStackTrace () ;}
	}
}
