package Asgardia.Server.ClientProcess;

import java.sql.*;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.Server.Opcodes.ServerOpcodes;

public class AccountOperation
{
	public static final int LOGIN_OK = 0x00;
	public static final int CHARACTER_NAME_EXISTS  = 0x06;
	public static final int ACCOUNT_ALREADY_EXISTS = 0x07;
	public static final int ACCOUNT_PASSWORD_ERROR = 0x08;
	public static final int ACCOUNT_IN_USE          = 0x16;
	
	private String UserAccount;
	private String UserPassword;
	private String IP;
	private String HostName;
	
	public void Login (SessionHandler Handle, byte[] Data) {
		PacketBuilder Builder = new PacketBuilder () ;
		PacketReader Reader = new PacketReader (Data) ;
		
		UserAccount = Reader.ReadString ().toLowerCase () ;
		UserPassword = Reader.ReadString () ;
		
		IP = Handle.getIP () ;
		HostName = Handle.getHostName () ;
		int Port = Handle.getPort () ;
		
		Accounts Account = new Accounts (Handle, UserAccount, UserPassword) ;
		int LoginResult = Account.Load () ;
		
		/*
		 * 回報登入結果
		 */
		Builder.Reset () ;
		Builder.WriteByte (ServerOpcodes.LOGIN_RESULT) ;
		Builder.WriteByte (LoginResult) ; //LOGIN RESULT CODE
		Builder.WriteDoubleWord (0x00000000) ;
		Builder.WriteDoubleWord (0x00000000) ;
		Builder.WriteDoubleWord (0x00000000) ;		
		Handle.SendPacket (Builder.GetPacket () ) ;
		
		if (LoginResult == LOGIN_OK) {
			System.out.printf ("[LOGIN]") ;
			Handle.Account = Account;
			/*
			 * 登入公告訊息
			 */
			new LoginAnnounce (Handle) ;

			Handle.Account.UpdateLastLoginTime () ;
			
		} else if (LoginResult == ACCOUNT_ALREADY_EXISTS) {
			/*
			 * 帳號不存在, 建立新帳號
			 */
			System.out.printf ("[CREATE ACCOUNT]") ;
			CreateNewAccount (Handle) ;
			
		} else if (LoginResult == ACCOUNT_IN_USE) {
			/*
			 * 重複登入
			 */
			System.out.printf ("[ALREADY LOGIN]") ;
			
		} else {
			/*
			 * 密碼錯誤/
			 */
			System.out.printf ("[PW ERROR]") ;
			Handle.Account = null;
		}
		
		System.out.printf ("Account:%s Password:%s From %s:%d(Host:%s)\n", UserAccount, UserPassword, IP, Port, HostName) ;
	}
	
	public void CreateNewAccount (SessionHandler Handle) {
		DatabaseCmds.CreateAccount (UserAccount, UserPassword, IP, HostName) ;
	}
	
	public void Delete (SessionHandler Handle, byte[] Data) {
		/*
		 * not implement yet
		 */
	}
	
	public int ReportCharacterAmount (SessionHandler Handle, byte[] Data) {
		int CharacterAmount = 0;
		Accounts Account = Handle.Account;
		
		CharacterAmount = DatabaseCmds.AccountCharacterAmount (Account.UserAccount) ;
		
		return CharacterAmount;
	}
	
	/*
	 * CommonClick service
	 */
	public void ReportCharactersData (SessionHandler Handle, byte[] Data) {
		Accounts Account = Handle.Account;
		
		int CharacterAmount = 0;
		
		ResultSet rs = null;
		try {	
			/*
			 * 回報帳號腳色數量
			 */
			CharacterAmount = ReportCharacterAmount (Handle, Data) ;
			
			PacketBuilder Builder = new PacketBuilder () ;
			Builder.WriteByte (ServerOpcodes.CHAR_AMOUNT) ;
			Builder.WriteByte (CharacterAmount) ; //character amount
			Builder.WriteDoubleWord (0x00000000) ;
			Builder.WriteDoubleWord (0x00000000) ;
			Handle.SendPacket (Builder.GetPacket () ) ;

			if (CharacterAmount > 0) {
				rs = DatabaseCmds.AccountCharacters (Account.UserAccount) ;
				
				/*
				 * 回報帳號腳色資料
				 */
				while (rs.next () ) {
					Builder = new PacketBuilder () ;
					Builder.WriteByte (ServerOpcodes.CHAR_LIST) ;
					Builder.WriteString (rs.getString ("char_name") ) ;
					Builder.WriteString (rs.getString ("Clanname") ) ;
					
					/* Type - 0:Royal 1:Knight 2:Elf 3:Mage 4:Darkelf */
					Builder.WriteByte (rs.getInt ("Type") ) ;
					
					/* Sex - 0:male, 1:female */
					Builder.WriteByte (1) ;
					Builder.WriteWord (rs.getInt ("Lawful") ) ; //lawful
					Builder.WriteWord (rs.getInt ("CurHP") ) ; //hp
					Builder.WriteWord (rs.getInt ("CurMP") ) ; //mp
					Builder.WriteByte (rs.getByte ("Ac") ) ; //ac
					Builder.WriteByte (rs.getByte ("level") ) ; //level
					Builder.WriteByte (rs.getByte ("Str") ) ; //str
					Builder.WriteByte (rs.getByte ("Dex") ) ; //dex
					Builder.WriteByte (rs.getByte ("Con") ) ; //con
					Builder.WriteByte (rs.getByte ("Wis") ) ; //wis
					Builder.WriteByte (rs.getByte ("Cha") ) ; //cha
					Builder.WriteByte (rs.getByte ("Intel") ) ; //int
					Builder.WriteByte (0x00) ; //END	
					Handle.SendPacket (Builder.GetPacket () );
				}
			} else {
				//do nothing
			}
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (rs) ;
		}
	}
	
}
