package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.ServerOpcodes;

/*
 * 告知客戶端NPC含有對話內容的HTML ID編號
 */
public class NpcAccessResult
{
	private PacketBuilder builder = new PacketBuilder () ;
	
	public NpcAccessResult (int NpcId, String HtmlKey) {
		
		builder.writeByte (ServerOpcodes.NPC_RESULT) ;
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
