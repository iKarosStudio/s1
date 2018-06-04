package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;
import Asgardia.World.Objects.*;

public class Attack
{
	public Attack (SessionHandler Handle, byte[] Data) {
		PacketReader reader = new PacketReader (Data) ;
		PcInstance Pc = Handle.getPc () ;
		
		int TargetUuid = reader.ReadDoubleWord () ;
		int TargetX = reader.ReadWord () ;
		int TargetY = reader.ReadWord () ;
		
		if (Pc.getWeightInScale30 () > 24) {
			Handle.SendPacket (new ServerMessage (110).getRaw () ) ;
			return;
		}
		
		/*
		 * 表現攻擊動作
		 */
		Pc.location.Heading = Pc.getDirection (TargetX, TargetY) ;
		byte[] action_packet= new NodeAction (1, Pc.Uuid, Pc.location.Heading).getRaw () ;
		Handle.SendPacket (action_packet) ;
		Pc.BoardcastPcInsight (action_packet) ;
		
		/*
		 * 傷害判定
		 */		
		Pc.Attack (TargetUuid, TargetX, TargetY) ;
		
		/*
		 * 攻擊驗算參考
		 * model/L1Attack.java
		 */
	}
}
