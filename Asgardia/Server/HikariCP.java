package Asgardia.Server;

import java.sql.*;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCP
{
	private static HikariCP instance;
	public static HikariDataSource Ds;
	public static Connection con;

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
			Config.setConnectionTimeout (1000) ;
			Ds = new HikariDataSource (Config) ;
			
			con = Ds.getConnection () ;
			if (con.isValid (1000) ) {
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
			con.close () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
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
}
