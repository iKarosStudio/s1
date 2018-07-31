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
			Builder.writeByte (ServerOpcodes.NODE_STATUS) ;
			Builder.WriteDoubleWord (Node.Uuid) ; //id
			Builder.writeByte (Node.Level) ;
			Builder.WriteDoubleWord (Node.Exp) ;
			Builder.writeByte (Node.getStr () ) ;
			Builder.writeByte (Node.getIntel () ) ;
			Builder.writeByte (Node.getWis () ) ;
			Builder.writeByte (Node.getDex () ) ;
			Builder.writeByte (Node.getCon () ) ;
			Builder.writeByte (Node.getCha () ) ;
			Builder.WriteWord (Node.Hp) ;
			Builder.WriteWord (Node.getMaxHp ()) ;
			Builder.WriteWord (Node.Mp) ;
			Builder.WriteWord (Node.getMaxMp ()) ;
			Builder.writeByte (Node.getAc () ) ;
			Builder.WriteDoubleWord (St.getTime () ) ; //time
			Builder.writeByte (Node.Satiation) ;
			Builder.writeByte (Node.getWeightInScale30 () ) ;//weight 0~30 = 0~100%, 1:3.4
			Builder.WriteWord (Node.Lawful) ;
			Builder.writeByte (0) ;//fire
			Builder.writeByte (0) ;//water
			Builder.writeByte (0) ;//wind
			Builder.writeByte (0) ;//earth
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public byte[] getRaw () {
		return Builder.GetPacketNoPadding () ;
	}
}
