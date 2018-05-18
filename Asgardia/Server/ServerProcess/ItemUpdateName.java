package Asgardia.Server.ServerProcess;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.Objects.Items.*;

public class ItemUpdateName
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ItemUpdateName (ItemInstance item) {
		builder.WriteByte (ServerOpcodes.ITEM_UPDATE_NAME) ;
		builder.WriteDoubleWord (item.Uuid) ;
		builder.WriteString (item.getName () ) ;
	}
	
	/*
	 * 更新為指定名稱
	 */
	public ItemUpdateName (ItemInstance item, String name) {
		builder.WriteByte (ServerOpcodes.ITEM_UPDATE_NAME) ;
		builder.WriteDoubleWord (item.Uuid) ;
		builder.WriteString (name) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket () ;
	}
}
