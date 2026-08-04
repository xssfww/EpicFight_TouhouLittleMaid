package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.hm.efn.EFN;
import com.hm.efn.registries.EFNMobEffectRegistry;
import net.EFTLM.EF.API.Event.MaidChangeItemEvent;
import net.EFTLM.EF.API.Event.MaidHurtTargetEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Compat.EFNCompat;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
public class BloodLustSkill extends WeaponInnateSkill {
    public BloodLustSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    protected float getEnergyCharge() {
        return 0;
    }
    @Override
    protected int getMaxStack() {
        return 0;
    }
    @Override
    public void onRemove(MaidChangeItemEvent event) {
        super.onRemove(event);
        EntityMaid maid = event.getMaidPatch().getOriginal();
        if (maid.hasEffect(EFNMobEffectRegistry.BLODDLUST.get())) {
            maid.removeEffect(EFNMobEffectRegistry.BLODDLUST.get());
        }
    }
    @Override
    public void onMaidTick(MaidTickEvent event, MaidPatch<?> patch) {
        super.onMaidTick(event, patch);
        EntityMaid maid = patch.getOriginal();
        if (patch.getTarget() == null) {
            if (maid.hasEffect(EFNMobEffectRegistry.BLODDLUST.get())) {
                EFNCompat.clearBloodLust(patch);
            }
        } else {
            if (maid.getHealth() >= (maid.getMaxHealth() * 0.5F)) {
                EFNCompat.giveBloodLust(patch);
            } else {
                EFNCompat.clearBloodLust(patch);
            }
        }
    }
    @Override
    public void onHurtTargetPost(MaidHurtTargetEvent.Post event) {
        super.onHurtTargetPost(event);
        EntityMaid maid = event.getMaidPatch().getOriginal();
        if (maid.hasEffect(EFNMobEffectRegistry.BLODDLUST.get())) {
            float healAmount = event.getAmount() * 0.3F;
            maid.heal(healAmount);
        }
    }
    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(EFN.MODID, "textures/item/co_tachi.png");
    }
}