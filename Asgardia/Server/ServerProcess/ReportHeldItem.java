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
		
		builder.writeByte (ServerOpcodes.ITEM_LIST) ;
		builder.writeByte (ItemList.size () ) ;
		
		ItemList.forEach ((Integer id, ItemInstance item)->{
			builder.WriteDoubleWord (item.Uuid);
			builder.writeByte (item.UseType) ;
			builder.writeByte (0) ;
			builder.WriteWord (item.InvGfx) ;
			builder.writeByte (item.Bless) ;
			builder.WriteDoubleWord (item.Count) ;
			builder.WriteByte (item.IsIdentified) ;				
			builder.WriteString (item.getName () ) ;
			
			if (item.IsIdentified) {
				byte[] Detail = item.getDetail () ;
				builder.writeByte (Detail.length) ;
				builder.WriteByte (Detail) ;
			} else {
				builder.writeByte (0) ;
			}
		}) ;
	}
	
	public byte[] getRaw () {
		return builder.GetPacket ();
	}
}
