package Asgardia.Server;

import java.sql.ResultSet;
import java.sql.Timestamp;

import Asgardia.Server.ClientProcess.AccountOperation;
import Asgardia.World.Objects.*;

public class Accounts extends SessionHandler
{
	private SessionHandler Session;
	
	public String UserAccount;
	public String UserPassword;
	
	public PcInstance ActivePc= null;
	
	public Accounts () {
		super () ;
	}
	
	public Accounts (SessionHandler session, String account, String password) {
		Session = session;
		UserAccount = account;
		UserPassword = password;
	}
	
	/*
	 * 載入帳號
	 */
	public int Load () {
		ResultSet rs = null;
		ResultSet online = null;
	
		int LoginResult = 0;
		try {
			rs = DatabaseCmds.LoadAccount (UserAccount) ;
			
			if (!rs.next () ) {
				System.out.println ("Account:" + UserAccount + " NOT EXISTS.") ;
				LoginResult = AccountOperation.ACCOUNT_ALREADY_EXISTS;
				
			} else {
				String pw = rs.getString ("password") ;
				if (UserPassword.equals (pw) ) {
					LoginResult = AccountOperation.LOGIN_OK;
				} else {
					LoginResult = AccountOperation.ACCOUNT_PASSWORD_ERROR;
				}
				
				online  = DatabaseCmds.CheckOnlineCharacters (UserAccount) ;
				if (online.next () ) {
					LoginResult = AccountOperation.ACCOUNT_IN_USE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace () ;
		} finally {
			DatabaseUtil.close (rs) ;
			DatabaseUtil.close (online) ;
		}
		return LoginResult;
	}
	
	public void UpdateLastLoginTime () {
		DatabaseCmds.UpdateAccountLoginTime (UserAccount, Session.getIP (), Session.getHostName () ) ;
	}
}
