package Asgardia.Server.ClientProcess;

import Asgardia.Server.*;
import Asgardia.Server.ServerProcess.*;

public class SkillBuy
{
	PacketHandler Handle;
	PacketReader reader;
	
	public SkillBuy (SessionHandler handle, byte[] data) {
		reader = new PacketReader (data) ;
		int Skill = reader.ReadDoubleWord () ;
		
		new SkillBuyList (handle, handle.getPc ().Type) ;
		
		System.out.printf ("購買魔法:%d\n", Skill) ;
	}
}
