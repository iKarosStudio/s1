package Asgardia.Server.ServerProcess;

import java.util.concurrent.atomic.AtomicInteger;

import Asgardia.Server.*;
import Asgardia.Server.Opcodes.ServerOpcodes;
import Asgardia.World.Objects.*;

public class CastTargetedSkill
{
	PacketBuilder pack = new PacketBuilder () ;
	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);
	
	public CastTargetedSkill (SessionHandler handle, int skill_id, int skill_gfx, int target_uuid, int target_x, int target_y) {
		PcInstance pc = handle.getPc ();
		pc.location.Heading = pc.getDirection (target_x, target_y) ;
		pack.WriteByte (ServerOpcodes.NODE_ACTION);
		pack.WriteByte (18);//action id
		pack.WriteDoubleWord (pc.Uuid); //
		pack.WriteDoubleWord (target_uuid);
		pack.WriteByte (6); 
		pack.WriteByte (pc.location.Heading) ;
		pack.WriteDoubleWord (_sequentialNumber.getAndIncrement () );
		pack.WriteWord (skill_gfx);
		pack.WriteByte (6);//0:Arrow 6:RemoteSkill 8:RemoteAOE Skill
		pack.writeByte (pc.location.x);
		pack.writeByte (pc.location.y);
		pack.WriteWord (target_x);
		pack.WriteWord (target_y);
		
		pack.WriteWord (0);
		pack.WriteByte (0);
	}
	
	public byte[] getRaw () {
		return pack.GetPacket ();
	}
}
