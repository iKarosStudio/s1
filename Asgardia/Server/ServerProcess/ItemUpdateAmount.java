package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.Items.*;

public class ItemUpdateAmount
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ItemUpdateAmount (ItemInstance item) {
		builder.writeByte (ServerOpcodes.ITEM_UPDATE_AMOUNT) ;
		builder.WriteDoubleWord (item.Uuid) ;
		builder.WriteDoubleWord (item.Count) ;
		builder.writeByte (0) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
