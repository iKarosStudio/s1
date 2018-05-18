package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.Items.*;

public class ItemUpdateStatus
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ItemUpdateStatus (ItemInstance item) {
		builder.WriteByte (ServerOpcodes.ITEM_UPDATE_STATUS) ;
		builder.WriteDoubleWord (item.Uuid) ;
		builder.WriteString (item.getName () ) ;
		builder.WriteDoubleWord (item.Count) ;
		if (item.IsIdentified) {
			byte[] detail = item.getDetail () ;
			builder.WriteByte (detail.length) ;
			for (byte b : detail) {
				builder.WriteByte (b) ;
			}
		} else {
			builder.WriteByte (0) ;
		}
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
