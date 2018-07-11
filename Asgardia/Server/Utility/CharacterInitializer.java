package Asgardia.Server.Utility;

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
		try {
			HikariCP Db = Handle.getDbHandle () ;
			String create = String.format ("INSERT INTO characters SET account_name=\'%s\', objid=\'%d\', char_name=\'%s\', level=\'%d\', Exp=\'%d\', MaxHp=\'%d\', MaxMp=\'%d\', CurHp=\'%d\', CurMp=\'%d\', Ac=\'%d\', Str=\'%d\', Con=\'%d\', Dex=\'%d\', Cha=\'%d\', Intel=\'%d\', Wis=\'%d\', Status=\'%d\', Class=\'%d\', Sex=\'%d\', Type=\'%d\', Heading=\'%d\', LocX=\'%d\', LocY=\'%d\', MapID=\'%d\', Food=\'%d\', Lawful=\'%d\', Title=\'%s\', ClanID=\'%d\', Clanname=\'%s\';",
					Handle.Account.UserAccount,
					pc.Uuid,
					pc.Name,
					pc.Level,
					pc.Exp,
					pc.BasicParameter.MaxHp,
					pc.BasicParameter.MaxMp,
					pc.Hp,
					pc.Mp,
					pc.BasicParameter.Ac,
					pc.BasicParameter.Str,
					pc.BasicParameter.Con,
					pc.BasicParameter.Dex,
					pc.BasicParameter.Cha,
					pc.BasicParameter.Intel,
					pc.BasicParameter.Wis,
					pc.Status,
					pc.Gfx, //Class
					pc.Sex,
					pc.Type,
					pc.location.Heading,
					pc.location.x,
					pc.location.y,
					pc.location.MapId,
					pc.Satiation,
					pc.Lawful,
					pc.Title,
					pc.ClanId,
					pc.ClanName
					) ;
			Db.Insert (create) ;
			
			/*
			 * 給初始道具
			 */
			
		} catch (Exception e) {e.printStackTrace (); }
		
		/*
		 * 更新客戶端角色顯示		
		 */
		Handle.SendPacket (new CharCreateResult (CharCreateResult.OK).getRaw () ) ;
		Handle.SendPacket (new NewCharacterPack (pc).getRaw () ) ;
	}
}
