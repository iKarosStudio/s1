package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.Items.*;

public class ItemUpdateBless
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ItemUpdateBless (ItemInstance item) {
		builder.WriteByte (ServerOpcodes.ITEM_UPDATE_BLESS) ;
		builder.WriteDoubleWord (item.Uuid) ;
		builder.WriteByte (item.Bless) ;
	}
	
	/*
	 writeC(Opcodes.S_OPCODE_ITEMCOLOR);
		writeD(item.getId());
		writeC(item.getItem().getBless()); // 0:b 1:n 2:c -?文????荔????堆???嚗?
	 */
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
