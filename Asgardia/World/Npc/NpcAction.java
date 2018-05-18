package Asgardia.World.Npc;

import org.xml.*;
import java.io.*;
import javax.xml.*;
import javax.xml.parsers.*;

public class NpcAction
{
	private final String Name;
	private final int NpcId;
	
	public NpcAction (String name, int npc_id) {
		Name = name;
		NpcId = npc_id;
		
		System.out.printf ("Create action name=%s, id=%d\n", Name, NpcId) ;
	}
}
