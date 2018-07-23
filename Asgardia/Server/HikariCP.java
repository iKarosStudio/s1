package Asgardia.Server;

import java.sql.*;
import java.lang.management.*;
import javax.management.*;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

public class HikariCP
{
	private static HikariCP instance;
	private static HikariDataSource Ds;
	private static Connection backup_con;

	public static HikariCP getInstance () {
		if (instance == null) {
			instance = new HikariCP () ;
		}
		return instance;
	}
	
	public HikariCP () {
		System.out.printf ("HikariCP Initializing...") ;

		try {			
			HikariConfig Config = new HikariConfig ("configs/hikari.properties") ;
			
			Ds = new HikariDataSource (Config) ;
			
			backup_con = Ds.getConnection () ;
			if (backup_con.isValid (1000) ) {
				System.out.println ("success") ;
			}			
		} catch (Exception e) {
			e.printStackTrace () ;
			System.out.printf ("Database connect fail, Check hikariCP config\n") ;
			System.exit (666) ;
			
		}
	}
	
	public void Disconnect () {
		try {
			backup_con.close () ;
			Ds.close () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public static Connection getConnection () {
		int RetryCounter = 0;
		Connection con = null;
		
		do {
			try {
				con = Ds.getConnection () ;
			} catch (Exception e) {
				System.out.printf ("[HikariCP] Connection used up\n") ;
				if (RetryCounter < 3) {
					RetryCounter++;
				} else {
					con = backup_con;
					System.out.printf ("[HikariCP] Use backup connection\n") ;
				}
			}
		} while (con == null) ;
		
		return con;
	}
	
	public String getUrl () {
		return Ds.getJdbcUrl () ;
	}
	
	public String getUser () {
		return Ds.getUsername () ;
	}
	
	public String getPassword () {
		return Ds.getPassword () ;
	}
	
	/*
	public ResultSet Query (String Cmd) {
		ResultSet rs = null;
		try { 
			Statement st = con.createStatement () ;
			rs = st.executeQuery (Cmd) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		} 
		return rs;
	}
	
	public boolean Insert (String cmd) {
		boolean rs = false;
		try {
			PreparedStatement st = con.prepareStatement (cmd) ;
			rs = st.execute () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
		return rs;
	}
	*/
}
