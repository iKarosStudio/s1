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
	PacketBuilder Builder = new PacketBuilder () ;
	
	public SendClientConfig (SessionHandler Handle) {
		PcInstance Pc = Handle.getPc () ;
		int length = 0;
		byte[] data = null;
		
		Connection con = HikariCP.getConnection ();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement ("SELECT * FROM character_config WHERE object_id=?;") ;
			ps.setInt (1, Pc.Uuid) ;
			
			rs = ps.executeQuery () ;
			
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
		} finally {
			DatabaseUtil.close (rs) ;
			DatabaseUtil.close (ps) ;
			DatabaseUtil.close (con) ;
		}
	}
	
	public byte[] getRaw () {
		return Builder.GetPacket () ;
	}
}
