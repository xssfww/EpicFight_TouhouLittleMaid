package net.EFTLM.EFN.Skill;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.hm.efn.EFN;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.MaidSkillDataKeys;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
public class MeenSpearSkill extends WeaponInnateSkill {
    public MeenSpearSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        super.onInit(event);
        event.registerData(this, MaidSkillDataKeys.MEEN_CHARGING_TIME);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        super.onMaidTick(event,patch);
        Integer time = patch.getDataValue(this, MaidSkillDataKeys.MEEN_CHARGING_TIME);
        if (time == null) return;
        if (time > 0) {
            patch.setData(this, MaidSkillDataKeys.MEEN_CHARGING_TIME, time - 1);
        }
    }
    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(EFN.MODID, "textures/item/meen_spear.png");
    }
}
