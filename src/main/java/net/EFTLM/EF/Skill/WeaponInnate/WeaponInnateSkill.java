package net.EFTLM.EF.Skill.WeaponInnate;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.API.Event.MaidHurtTargetEvent;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.MaidSkillDataKeys;
import net.EFTLM.EF.Utils.CompoundTagManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import java.util.Objects;
public abstract class WeaponInnateSkill extends MaidSkill {
    protected float consumption;
    protected int maxStack;
    public WeaponInnateSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    public float getEnergyCharge() {
        return this.consumption;
    }
    public int getMaxStack() {
        return this.maxStack;
    }
    public void setParams(CompoundTag parameters) {
        this.consumption = parameters.getFloat(CompoundTagManager.Consumption);
        this.maxStack = parameters.getInt(CompoundTagManager.Stack);
    }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        event.registerData(this, MaidSkillDataKeys.STACK);
        event.registerData(this, MaidSkillDataKeys.ENERGY);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        if (patch == null) return;
        Float energy = patch.getDataValue(this, MaidSkillDataKeys.ENERGY);
        Integer stack = patch.getDataValue(this, MaidSkillDataKeys.STACK);
        if (energy == null || stack == null) return;
        float threshold = getEnergyCharge();
        int max = getMaxStack();
        while (energy >= threshold && stack < max) {
            energy -= threshold;
            stack++;
        }
        if (!Objects.equals(patch.getDataValue(this, MaidSkillDataKeys.ENERGY), energy)) {
            patch.setData(this, MaidSkillDataKeys.ENERGY, energy);
        }
        if (!Objects.equals(patch.getDataValue(this, MaidSkillDataKeys.STACK), stack)) {
            patch.setData(this, MaidSkillDataKeys.STACK, stack);
        }
    }
    @Override
    public void onHurtTargetPost(MaidHurtTargetEvent.Post event) {
        MaidPatch<?> patch = event.getMaidPatch();
        if (patch == null) return;
        EntityMaid maid = patch.getOriginal();
        if (!(maid.level() instanceof ServerLevel)) return;
        Float energy = patch.getDataValue(this, MaidSkillDataKeys.ENERGY);
        if (energy == null) return;
        patch.setData(this, MaidSkillDataKeys.ENERGY, energy + event.getAmount());
    }
}
