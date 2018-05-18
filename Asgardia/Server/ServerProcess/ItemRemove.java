package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.Items.*;

public class ItemRemove
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ItemRemove (ItemInstance item) {
		builder.WriteByte (ServerOpcodes.ITEM_REMOVE) ;
		builder.WriteDoubleWord (item.Uuid) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
