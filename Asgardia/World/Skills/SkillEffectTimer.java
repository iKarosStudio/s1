package Asgardia.World.Skills;

import java.util.*;
import java.util.concurrent.*;

import Asgardia.Server.*;
import Asgardia.World.Objects.*;
import Asgardia.World.Skills.CommonSkill.*;

/*
 * 所有技能共用的計時器
 * 新增技能效果要新增在HashMap Effect裡面
 */
public class SkillEffectTimer extends TimerTask implements Runnable
{
	private final Timer timer = new Timer () ;
	private PcInstance Pc;
	private SessionHandler Handle;
	public ConcurrentHashMap<Integer, SkillEffect> Effect = null;
	
	
	public SkillEffectTimer (PcInstance pc) {
		Effect = new ConcurrentHashMap<Integer, SkillEffect> () ;
		Pc = pc;
		Handle = Pc.getHandler () ;
	}
	
	public void run () {
		if (!Effect.isEmpty () ) {
			Effect.forEach ((Integer SkillId, SkillEffect Buff)->{
				if (Buff.RemainTime == 0xFFFF) {
					return;
				} else if (Buff.RemainTime > 0) {
					Buff.RemainTime--;
				} else {
					//Stop buff
					stopSkill (SkillId) ;
					Effect.remove (SkillId) ;
				}
			});
		}
	}
	
	public void Start () {
		timer.scheduleAtFixedRate (this, 0, 1000) ; //1S interval
	}
	
	public void Stop () {
		timer.cancel () ;
	}
	
	public void stopSkill (int skill_id) {
		if (skill_id == SkillId.STATUS_HASTE) {
			Pc.MoveSpeed = 0;
			Handle.SendPacket (new SkillHaste (Pc.Uuid, 0, 0).getRaw () ) ;
		} else if (skill_id == SkillId.STATUS_BRAVE) {
			Pc.BraveSpeed = 0;
			Handle.SendPacket (new SkillBrave (Pc.Uuid, 0, 0).getRaw () ) ;
		}
	}
}
