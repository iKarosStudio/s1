package Asgardia.Server.ClientProcess;

import java.util.logging.*;
import java.util.Timer;
import java.sql.*;

import Asgardia.Server.*;
import Asgardia.Server.Utility.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.RoutineTasks.*;

public class CharacterOperation
{
	PcInstance Character = null;
	private static Logger Log = Logger.getLogger (CharacterOperation.class.getSimpleName () ) ;
	
	public void Login (SessionHandler Handle, byte[] Data) {
		PacketReader Reader = new PacketReader (Data) ;
		String CharName = Reader.ReadString ();
		
		/*
		 * 載入角色
		 */
		PcInstance Pc = new PcInstance (Handle) ;
		if (Pc.Load (CharName) ) {
			/*
			 * 開始回報角色登入資料
			 */
			Handle.Account.ActivePc = Pc;
			
			new Unknown1 (Handle) ;
			new Unknown2 (Handle) ;
			//new RequestStart (Handle) ;  //active spell
			/*
			 * 載入角色記憶座標
			 */
			
			Pc.LoadItem () ;
			Pc.ApplyEquipmentEffects () ;
			Pc.LoadSkills () ;
			Pc.LoadBuff () ;
			
			
			/*
			byte[] config = new SendClientConfig (Handle).getRaw () ;
			if (config.length > 0) {
				Handle.SendPacket (config) ;
			}
			*/
			
			new GameTime (Handle) ;
			
			Handle.SendPacket (new NodeStatus (Pc).getRaw () ) ;
			Handle.SendPacket (new Mapid (Pc.location.MapId).getRaw () ) ;
			Handle.SendPacket (new NodePacket (Pc).getRaw () );
			Handle.SendPacket (new ReportMatkMrst (Handle).getRaw () ) ;
			Handle.SendPacket (new ReportTitle (Handle).getRaw () ) ;
			
			Handle.SendPacket (new ReportWeather (Asgardia.getInstance ().Weather).getRaw () ) ;
			
			//Set Emblem here

			Handle.SendPacket (new NodeStatus (Pc).getRaw () ) ;
			
			/*
			 * 開始系統對時
			 */
			Pc.Tick.Start () ;
			
			/*
			 * 固定循環工作
			 */
			Pc.RoutineTask.Start () ;
			Pc.SkillTimer.Start () ;
			/*
			 * 開始經驗值,生命,魔力監測
			 */
			Pc.ExpKeeper.Start () ;
			Pc.HpKeeper.Start () ;
			Pc.MpKeeper.Start () ;
			
			/*
			 * 視距物件更新服務
			 */
			Pc.Ou = new ObjectUpdate (Pc) ;
			Pc.Ou.Start () ;
			
			Pc.UpdateCurrentMap () ;
			Handle.Account.ActivePc = Pc;
			Asgardia.getInstance ().addPc (Pc) ;
			
		} else {
			/*
				沒有角色ID, 非正常登入現象
			*/
			Log.warning ("不正常角色登入 form ip:" + Handle.getIP () );
		}
	}
	
	/*
	 * 參考C_CreateChar.java
	 */
	public void Create (SessionHandler Handle, byte[] Data) {
		HikariCP Db = Handle.getDbHandle () ;
		PacketReader Reader = new PacketReader (Data) ;
		
		String Name = Reader.ReadString () ;
		
		/* 
		 * 0: Prince/Princess
		 * 1: Knight
		 * 2: Elf
		 * 3: Mage
		 * 4: DarkElf
		 */
		int Type = Reader.ReadByte () ;
		
		/* 
		 * 0:Male
		 * 1:Female
		 */
		int Sex =  Reader.ReadByte () ; 
		
		int Str =  Reader.ReadByte () ;
		int Dex =  Reader.ReadByte () ;
		int Con =  Reader.ReadByte () ;
		int Wis =  Reader.ReadByte () ;
		int Cha =  Reader.ReadByte () ;
		int Intel= Reader.ReadByte () ;
		
		try {
			//檢查帳號角色數量
			String Check = String.format ("SELECT count(*) as cnt FROM characters where account_name=\'%s\';", 
					Handle.Account.UserAccount) ;
			ResultSet Result = Db.Query (Check) ;
			if (Result.next () ) {
				if (Result.getInt ("cnt") < 4) {
					//System.out.printf ("帳號角色數量正確:%d\n", Result.getInt ("cnt") ) ;
				} else {
					Handle.SendPacket (new CharCreateResult (CharCreateResult.WRONG_AMOUNT).getRaw () ) ;
					return;
				}
			}
			
			//檢查重複ID
			Check = String.format ("SELECT account_name FROM characters WHERE char_name=\'%s\';", Name) ;
			Result = Db.Query (Check) ;
			if (Result.next () ) {
				Handle.SendPacket (new CharCreateResult (CharCreateResult.ALREADY_EXIST).getRaw () ) ;
				return;
			}
			
			//檢查數值總和
			if ((Str + Dex + Con + Wis + Cha + Intel) != 75) {
				Handle.SendPacket (new CharCreateResult (CharCreateResult.WRONG_AMOUNT).getRaw () ) ;
				return;
			}
			
			//Done
			CharacterInitializer ci = new CharacterInitializer (Handle, Name, Type, Sex, Str, Dex, Con, Wis, Cha, Intel) ;
			System.out.printf ("Create Character:%s\t From Account:%s @ %s\n", Name, Handle.Account.UserAccount, Handle.getIP () ) ;
			
		} catch (Exception e) {e.printStackTrace () ; }
		
	}
	
	public void Delete (SessionHandler Handle, byte[] Data) {
		//刪除角色
	}
}


