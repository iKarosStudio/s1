package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.Items.*;

public class ItemInsert
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ItemInsert (ItemInstance item) {
		builder.WriteByte (ServerOpcodes.ITEM_INSERT) ;
		builder.WriteDoubleWord (item.Uuid) ;
		builder.WriteByte (item.UseType) ;
		builder.WriteByte (0) ;
		builder.WriteWord (item.InvGfx) ;
		builder.WriteByte (item.Bless) ;
		builder.WriteDoubleWord (item.Count) ;
		builder.WriteByte (item.IsIdentified) ;
		builder.WriteString (item.getName () ) ;
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
