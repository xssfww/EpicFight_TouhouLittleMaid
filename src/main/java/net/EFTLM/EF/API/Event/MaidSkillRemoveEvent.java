package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.API.AbstractMaidEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Cancelable;
@Cancelable
public class MaidSkillRemoveEvent extends AbstractMaidEvent {
    private final MaidSkill skill;
    public MaidSkillRemoveEvent(MaidPatch<?> MaidPatch, MaidSkill skill) {
        super(MaidPatch);
        this.skill = skill;
    }
    public MaidSkill getSkill() {
        return this.skill;
    }
    public ResourceLocation getSkillName() {
        return this.skill == null ? null : this.skill.getRegistryName();
    }
    public void removeData() {
        MaidPatch<?> MaidPatch = this.getMaidPatch();
        if (MaidPatch != null && this.skill != null) {
            MaidPatch.removeData(this.skill);
        }
    }
}
