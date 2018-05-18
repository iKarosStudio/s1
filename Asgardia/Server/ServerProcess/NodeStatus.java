package Asgardia.Server.ServerProcess;

import java.sql.ResultSet;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.ServerOpcodes;
import Asgardia.World.Objects.*;

public class NodeStatus
{
	PacketBuilder Builder = new PacketBuilder () ;
	/*
	 * 報告物件狀態
	 */
	public NodeStatus (PcInstance Node) {
		ServerTime St = ServerTime.getInstance ();
		try {
			Builder.WriteByte (ServerOpcodes.NODE_STATUS) ;
			Builder.WriteDoubleWord (Node.Uuid) ; //id
			Builder.WriteByte (Node.Level) ;
			Builder.WriteDoubleWord (Node.Exp) ;
			Builder.WriteByte (Node.getStr () ) ;
			Builder.WriteByte (Node.getIntel () ) ;
			Builder.WriteByte (Node.getWis () ) ;
			Builder.WriteByte (Node.getDex () ) ;
			Builder.WriteByte (Node.getCon () ) ;
			Builder.WriteByte (Node.getCha () ) ;
			Builder.WriteWord (Node.Hp) ;
			Builder.WriteWord (Node.getMaxHp ()) ;
			Builder.WriteWord (Node.Mp) ;
			Builder.WriteWord (Node.getMaxMp ()) ;
			Builder.WriteByte (Node.getAc () ) ;
			Builder.WriteDoubleWord (St.getTime () ) ; //time
			Builder.WriteByte (Node.Satiation) ;
			Builder.WriteByte (Node.getWeightInScale30 () ) ;//weight 0~30 = 0~100%, 1:3.4
			Builder.WriteWord (Node.Lawful) ;
			Builder.WriteByte (0) ;//fire
			Builder.WriteByte (0) ;//water
			Builder.WriteByte (0) ;//wind
			Builder.WriteByte (0) ;//earth
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public byte[] getRaw () {
		return Builder.GetPacketNoPadding () ;
	}
}
