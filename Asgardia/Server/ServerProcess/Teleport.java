package Asgardia.Server.ServerProcess;

import java.lang.*;

import Asgardia.Types.*;
import Asgardia.Server.*;
import Asgardia.World.*;
import Asgardia.World.Map.*;
import Asgardia.World.Objects.*;

public class Teleport
{
	public Teleport (PcInstance Pc, Location Dest, boolean useVirtualEffect) {
		SessionHandler Handle = Pc.getHandler () ;
		
		if (Pc.location.MapId != Dest.MapId) {
			Pc.location.MapId = Dest.MapId;
			
			AsgardiaMap map = Asgardia.getInstance ().getMap (Pc.location.MapId) ;
			map.addPc (Pc) ;
			Pc.Map.removePc (Pc) ;
			Pc.Map = map;
		}
		
		Pc.location.x = Dest.x;
		Pc.location.y = Dest.y;
		Pc.location.Heading = Dest.Heading;
		
		byte[] mapid_packet = new Mapid (Pc.location.MapId).getRaw () ;
		byte[] pc_packet = new NodePacket (Pc).getRaw () ;
		
		if (useVirtualEffect) {
			byte[] effect_packet = new SkillGfx (Pc.Uuid, 169).getRaw () ;
			Handle.SendPacket (effect_packet) ;
			Pc.BoardcastPcInsight (effect_packet) ;
			
			try {
				Thread.sleep (700) ;
			} catch (Exception e) {e.printStackTrace () ; }
		}
		
		Pc.removeAllInsight () ;
		
		Handle.SendPacket (mapid_packet) ;
		Handle.SendPacket (pc_packet) ;
		Pc.BoardcastPcInsight (pc_packet) ;
	}
}
