package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.Items.*;

public class ItemIdentify
{
	PacketBuilder builder = new PacketBuilder () ;

	public ItemIdentify (ItemInstance i) {
		builder.writeByte (ServerOpcodes.ITEM_IDENTIFY);
		builder.WriteWord (i.ItemDescId) ;
		builder.WriteString (i.getName () ) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
