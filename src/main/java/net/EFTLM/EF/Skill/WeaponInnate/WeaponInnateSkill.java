package net.EFTLM.EF.Skill.WeaponInnate;

import net.EFTLM.EF.API.Event.MaidChangeItemEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
public abstract class WeaponInnateSkill extends MaidSkill {
    public WeaponInnateSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    public void onRemove(MaidChangeItemEvent event) {
        MaidPatch<?> MaidPatch = event.getMaidPatch();
        MaidPatch.removeData(this);
    }
}
