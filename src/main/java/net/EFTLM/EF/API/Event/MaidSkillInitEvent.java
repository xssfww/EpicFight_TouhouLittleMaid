package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.API.AbstractMaidEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
public class MaidSkillInitEvent extends AbstractMaidEvent {
    public MaidSkillInitEvent(MaidPatch<?> MaidPatch) {
        super(MaidPatch);
    }
    public <V> void registerData(MaidSkill skill, MaidSkillDataManager.SkillDataKey<V> key, V data) {
        this.getMaidPatch().registerData(skill,key,data);
    }
}