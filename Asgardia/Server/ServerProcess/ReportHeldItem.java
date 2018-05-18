package Asgardia.Server.ServerProcess;

import java.util.*;
import java.util.concurrent.*;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.*;
import Asgardia.World.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Items.*;

public class ReportHeldItem
{
	PacketBuilder builder = new PacketBuilder () ;
	
	public ReportHeldItem (ConcurrentHashMap<Integer, ItemInstance> ItemList) {
		
		builder.WriteByte (ServerOpcodes.ITEM_LIST) ;
		builder.WriteByte (ItemList.size () ) ;
		
		ItemList.forEach ((Integer id, ItemInstance item)->{
			builder.WriteDoubleWord (item.Uuid);
			builder.WriteByte (item.UseType) ;
			builder.WriteByte (0) ;
			builder.WriteWord (item.InvGfx) ;
			builder.WriteByte (item.Bless) ;
			builder.WriteDoubleWord (item.Count) ;
			builder.WriteByte (item.IsIdentified) ;				
			builder.WriteString (item.getName () ) ;
			
			if (item.IsIdentified) {
				byte[] Detail = item.getDetail () ;
				builder.WriteByte (Detail.length) ;
				builder.WriteByte (Detail) ;
			} else {
				builder.WriteByte (0) ;
			}
		}) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket ();
	}
}
