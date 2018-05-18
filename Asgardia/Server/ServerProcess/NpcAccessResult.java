package Asgardia.Server.ServerProcess;

import Asgardia.Server.PacketBuilder;
import Asgardia.Server.SessionHandler;
import Asgardia.Server.Opcodes.ServerOpcodes;

public class NpcAccessResult
{
	private PacketBuilder builder = new PacketBuilder () ;
	
	public NpcAccessResult (int NpcId, String HtmlKey) {
		
		builder.WriteByte (ServerOpcodes.NPC_RESULT) ;
		builder.WriteDoubleWord (NpcId) ;
		builder.WriteString (HtmlKey) ;
		//有參數在這邊帶入
		//參考S_NPCTalksReturn.java
		builder.WriteWord (0x00) ;
		builder.WriteWord (0x00) ;

	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
