package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.API.AbstractMaidEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
import net.minecraft.resources.ResourceLocation;
public class MaidSkillInitEvent extends AbstractMaidEvent {
    private final ResourceLocation skillName;
    public MaidSkillInitEvent(MaidPatch<?> MaidPatch, ResourceLocation RegisterName) {
        super(MaidPatch);
        this.skillName = RegisterName;
    }
    public MaidSkillInitEvent(MaidPatch<?> MaidPatch) {
        super(MaidPatch);
        this.skillName = null;
    }
    public <V> void registerData(MaidSkill skill, MaidSkillDataManager.SkillDataKey<V> key) {
        this.getMaidPatch().registerData(skill, key, key.getDefaultValue());
    }
    public <V> void registerData(MaidSkill skill, MaidSkillDataManager.SkillDataKey<V> key, V data) {
        this.getMaidPatch().registerData(skill,key,data);
    }
    public ResourceLocation getSkillName()  {
        return this.skillName;
    }
}
