package Asgardia.Server.ServerProcess;

import java.sql.*;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.ServerOpcodes;
import Asgardia.World.Objects.*;

/*
 * 暫時沒用
 */
public class SendClientConfig
{
	HikariCP db;
	PacketBuilder Builder = new PacketBuilder () ;
	
	public SendClientConfig (SessionHandler Handle) {
		PcInstance Pc = Handle.getPc () ;
		int length = 0;
		byte[] data = null;
		
		db = Handle.getDbHandle () ;
		
		String q = String.format ("SELECT * FROM character_config WHERE object_id=\'%d\';", Pc.Uuid) ;
		ResultSet rs = db.Query (q) ;
		
		try {
			Builder.WriteByte (ServerOpcodes.FUNCTION_KEY) ;
			Builder.WriteByte (41) ;
			if (rs.next () ) {
				length = rs.getInt ("length") ;
				data = rs.getBytes ("data") ;

				Builder.WriteWord (length) ;
				if (length > 0) {	
					Builder.WriteByte (data) ;
				}
			} else {
				Builder.WriteByte (0) ;
			}
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public byte[] getRaw () {
		return Builder.GetPacket () ;
	}
}
