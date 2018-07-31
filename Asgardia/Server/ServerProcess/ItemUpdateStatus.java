package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.Items.*;

public class ItemUpdateStatus
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ItemUpdateStatus (ItemInstance item) {
		builder.writeByte (ServerOpcodes.ITEM_UPDATE_STATUS) ;
		builder.WriteDoubleWord (item.Uuid) ;
		builder.WriteString (item.getName () ) ;
		builder.WriteDoubleWord (item.Count) ;
		if (item.IsIdentified) {
			byte[] detail = item.getDetail () ;
			builder.writeByte (detail.length) ;
			for (byte b : detail) {
				builder.writeByte (b) ;
			}
		} else {
			builder.writeByte (0) ;
		}
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
