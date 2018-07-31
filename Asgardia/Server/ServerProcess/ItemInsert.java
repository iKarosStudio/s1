package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.Items.*;

public class ItemInsert
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ItemInsert (ItemInstance item) {
		builder.writeByte (ServerOpcodes.ITEM_INSERT) ;
		builder.WriteDoubleWord (item.Uuid) ;
		builder.writeByte (item.UseType) ;
		builder.writeByte (0) ;
		builder.WriteWord (item.InvGfx) ;
		builder.writeByte (item.Bless) ;
		builder.WriteDoubleWord (item.Count) ;
		builder.WriteByte (item.IsIdentified) ;
		builder.WriteString (item.getName () ) ;
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
