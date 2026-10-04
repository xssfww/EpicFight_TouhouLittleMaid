package net.EFTLM.EFN.Skill;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.hm.efn.EFN;
import net.EFTLM.EF.API.Event.MaidHurtTargetEvent;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.MaidSkillDataKeys;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
public class ClawSkill extends WeaponInnateSkill {
    private float HEAL_RATIO;
    public ClawSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    public void setParams(CompoundTag parameters) {
        super.setParams(parameters);
        HEAL_RATIO = parameters.getFloat("heal_ratio");
    }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        super.onInit(event);
        event.registerData(this, MaidSkillDataKeys.CLAW_TIME);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        super.onMaidTick(event,patch);
        Integer time = patch.getDataValue(this, MaidSkillDataKeys.CLAW_TIME);
        if (time == null) return;
        if (time > 0) {
            patch.setData(this, MaidSkillDataKeys.CLAW_TIME, time - 1);
        }
    }
    @Override
    public void onHurtTargetPost(MaidHurtTargetEvent.Post event) {
        super.onHurtTargetPost(event);
        MaidPatch<?> patch = event.getMaidPatch();
        EntityMaid maid = event.getMaidPatch().getOriginal();
        if (maid == null) return;
        Integer time = patch.getDataValue(this, MaidSkillDataKeys.CLAW_TIME);
        if (time == null) return;
        if (time > 0) {
            maid.heal(event.getAmount() * HEAL_RATIO);
        }
    }
    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(EFN.MODID, "textures/item/nf_claw.png");
    }
}
