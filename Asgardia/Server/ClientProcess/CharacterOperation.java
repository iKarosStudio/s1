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
		
		Connection conn = HikariCP.getConnection ();
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs_amount = null;
		ResultSet rs_idrepeat = null;
		
		try {
			//檢查帳號角色數量
			ps1 = conn.prepareStatement ("SELECT count(*) as cnt FROM characters WHERE account_name=?;") ;
			ps1.setString (1, Handle.Account.UserAccount) ;
			rs_amount = ps1.executeQuery () ;
			if (rs_amount.next () ) {
				if (rs_amount.getInt ("cnt") > 4) {
					Handle.SendPacket (new CharCreateResult (CharCreateResult.WRONG_AMOUNT).getRaw () ) ;
					return;
				}
			}
			
			//檢查重複ID
			ps2 = conn.prepareStatement ("SELECT account_name FROM characters WHERE char_name=?;") ;
			ps2.setString (1, Name) ;
			rs_idrepeat = ps2.executeQuery () ;
			if (rs_idrepeat.next () ) {
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
			
		} catch (Exception e) {
			e.printStackTrace () ; 
		} finally {
			DatabaseUtil.close (rs_amount) ;
			DatabaseUtil.close (rs_idrepeat) ;
			DatabaseUtil.close (ps1) ;
			DatabaseUtil.close (ps2) ;
			DatabaseUtil.close (conn) ;
		}
		
	}
	
	public void Delete (SessionHandler Handle, byte[] Data) {
		//刪除角色
	}
}


