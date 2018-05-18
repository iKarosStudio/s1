package Asgardia.World.Npc;

public class NpcTalkData
{
	public final int NpcId;
	public final String NormalAction;
	public final String CaoticAction;
	public final String TeleportUrl;
	public final String TeleportUrla;
	
	public NpcTalkData (int npc_id, String normal_action, String caotic_action, String teleport_url, String teleport_urla) {
		NpcId = npc_id;
		NormalAction = normal_action;
		CaoticAction = caotic_action;
		TeleportUrl = teleport_url;
		TeleportUrla = teleport_urla;
	}
}
