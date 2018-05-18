package Asgardia.World.Npc;

import java.util.concurrent.*;

import Asgardia.Server.*;
import Asgardia.World.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Objects.Dynamic.DynamicObject;
import Asgardia.World.Objects.Template.*;


public class NpcInstance extends DynamicObject
{
	public String NameId;
	
	public void run () {
		//
	}
	
	public NpcInstance (NpcTemplate NpcData) {
		Uuid = NpcData.Uuid;
		Gfx = NpcData.Gfx;
		Name = NpcData.Name;
		location.Heading = NpcData.location.Heading;
		NameId = NpcData.NameId;
		Level = NpcData.Level;
	}
}
