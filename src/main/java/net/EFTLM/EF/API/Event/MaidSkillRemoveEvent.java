package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.API.AbstractMaidEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.ICancellableEvent;
// 1.21.1: the @Cancelable annotation is gone; cancellable events implement ICancellableEvent instead
public class MaidSkillRemoveEvent extends AbstractMaidEvent implements ICancellableEvent {
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
