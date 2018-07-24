package Asgardia.Server.Utility;

import java.sql.*;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;

public class CharacterInitializer
{
	public static final int CLASSID_PRINCE = 0;
	public static final int CLASSID_PRINCESS = 1;
	public static final int CLASSID_KNIGHT_MALE = 61;
	public static final int CLASSID_KNIGHT_FEMALE = 48;
	public static final int CLASSID_ELF_MALE = 138;
	public static final int CLASSID_ELF_FEMALE = 37;
	public static final int CLASSID_WIZARD_MALE = 734;
	public static final int CLASSID_WIZARD_FEMALE = 1186;
	public static final int CLASSID_DARK_ELF_MALE = 2786;
	public static final int CLASSID_DARK_ELF_FEMALE = 2796;
	
	private static final int[] MALE_LIST = new int[] {0, 61, 138, 734, 2786};
	private static final int[] FEMALE_LIST = new int[] {1, 48, 37, 1186, 2796};
	
	public CharacterInitializer (SessionHandler Handle,
		String name, int type, int sex,
		int str, int dex, int con, int wis, int cha, int intel
	) {
		
		PcInstance pc = new PcInstance () ;
		pc.setHandler (Handle) ;
		pc.Uuid = UuidGenerator.Next () ;
		pc.Name = name;
		pc.Title = "";
		pc.ClanName = "";
		pc.Type = type;
		pc.Sex = sex;
		if (pc.Sex == 0) { //Male
			pc.Gfx = MALE_LIST[pc.Type];
		} else { //Female
			pc.Gfx = FEMALE_LIST[pc.Type];
		}
		pc.TempGfx = pc.Gfx;
		pc.Lawful = 0;
		pc.Level = 1;
		pc.Exp = 0;
		pc.BasicParameter.Ac = 10;
		pc.Satiation = 3;
		
		pc.BasicParameter.Str = str;
		pc.BasicParameter.Dex = dex;
		pc.BasicParameter.Con = con;
		pc.BasicParameter.Wis = wis;
		pc.BasicParameter.Cha = cha;
		pc.BasicParameter.Intel = intel;
		
		/* 出生地點 */
		pc.location.MapId = 0;
		pc.location.x = 32643;
		pc.location.y = 32960;
		
		if (pc.isRoyal () ) {
			pc.BasicParameter.MaxHp = 14;
			if (pc.BasicParameter.Wis > 15) {
				pc.BasicParameter.MaxMp = 4;
			} else if (pc.BasicParameter.Wis > 11) {
				pc.BasicParameter.MaxMp = 3;
			} else {
				pc.BasicParameter.MaxMp = 2;
			}
			
		} else if (pc.isKnight () ) {
			pc.BasicParameter.MaxHp = 16;
			if (pc.BasicParameter.Wis > 11) {
				pc.BasicParameter.MaxMp = 2;
			} else {
				pc.BasicParameter.MaxMp = 1;
			}
				
			
		} else if (pc.isElf () ) {
			pc.BasicParameter.MaxHp = 15;
			if (pc.BasicParameter.Wis > 15) {
				pc.BasicParameter.MaxMp = 6;
			} else {
				pc.BasicParameter.MaxMp = 4;
			}
			
		} else if (pc.isMage () ) {
			pc.BasicParameter.MaxHp = 12;
			if (pc.BasicParameter.Wis > 15) {
				pc.BasicParameter.MaxMp = 8;
			} else {
				pc.BasicParameter.MaxMp = 6;
			}
			
		} else if (pc.isDarkElf () ) {
			pc.BasicParameter.MaxHp = 12;
			if (pc.BasicParameter.Wis > 15) {
				pc.BasicParameter.MaxMp = 6;
			} else if (pc.BasicParameter.Wis > 11) {
				pc.BasicParameter.MaxMp = 4;
			} else {
				pc.BasicParameter.MaxMp = 3;
			}
			
		}
		pc.Hp = pc.BasicParameter.MaxHp ;
		pc.Mp = pc.BasicParameter.MaxMp ;
		
		/*
		 * 寫入資料庫
		 */
		Connection Conn = HikariCP.getConnection ();
		PreparedStatement ps = null;
		try {
			ps = Conn.prepareStatement ("INSERT INTO characters SET account_name=?, objid=?, char_name=?, level=?, Exp=?, MaxHp=?, MaxMp=?, CurHp=?, CurMp=?, Ac=?, Str=?, Con=?, Dex=?, Cha=?, Intel=?, Wis=?, Status=?, Class=?, Sex=?, Type=?, Heading=?, LocX=?, LocY=?, MapID=?, Food=?, Lawful=?, Title=?, ClanID=?, Clanname=?;") ;
			ps.setString (1, Handle.Account.UserAccount) ;
			ps.setInt (2, pc.Uuid) ;
			ps.setString (3, pc.Name) ;
			ps.setInt (4, pc.Level) ;
			ps.setInt (5, pc.Exp) ;
			ps.setInt (6, pc.BasicParameter.MaxHp) ;
			ps.setInt (7, pc.BasicParameter.MaxMp) ;
			ps.setInt (8, pc.Hp) ;
			ps.setInt (9, pc.Mp) ;
			ps.setInt (10, pc.BasicParameter.Ac) ;
			ps.setInt (11, pc.BasicParameter.Str) ;
			ps.setInt (12, pc.BasicParameter.Con) ;
			ps.setInt (13, pc.BasicParameter.Dex) ;
			ps.setInt (14, pc.BasicParameter.Cha) ;
			ps.setInt (15, pc.BasicParameter.Intel) ;
			ps.setInt (16, pc.BasicParameter.Wis);
			ps.setInt (17, pc.Status) ;
			ps.setInt (18, pc.Gfx) ;
			ps.setInt (19, pc.Sex) ;
			ps.setInt (20, pc.Type) ;
			ps.setInt (21, pc.location.Heading) ;
			ps.setInt (22, pc.location.x) ;
			ps.setInt (23, pc.location.y) ;
			ps.setInt (24, pc.location.MapId) ;
			ps.setInt (25, pc.Satiation) ;
			ps.setInt (26, pc.Lawful) ;
			ps.setString (27, pc.Title) ;
			ps.setInt (28, pc.ClanId) ;
			ps.setString (29, pc.ClanName) ;
			
			ps.execute () ;
			
			/*
			 * 給初始道具
			 */
			//
			
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (Conn) ;
		}
		
		/*
		 * 更新客戶端角色顯示		
		 */
		Handle.SendPacket (new CharCreateResult (CharCreateResult.OK).getRaw () ) ;
		Handle.SendPacket (new NewCharacterPack (pc).getRaw () ) ;
	}
}
