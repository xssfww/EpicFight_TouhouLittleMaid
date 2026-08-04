package net.EFTLM.EF.Skill.WeaponInnate;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.API.Event.MaidChangeItemEvent;
import net.EFTLM.EF.API.Event.MaidHurtTargetEvent;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
import net.minecraft.server.level.ServerLevel;
import java.util.Objects;
public abstract class WeaponInnateSkill extends MaidSkill {
    protected abstract float getEnergyCharge();
    protected abstract int getMaxStack();
    public static final MaidSkillDataManager.SkillDataKey<Integer> STACK =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Float> ENERGY =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.FLOAT);
    public WeaponInnateSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        event.registerData(this, STACK, 0);
        event.registerData(this, ENERGY, 0.0F);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        if (patch == null) return;
        Float energy = patch.getDataValue(this, ENERGY);
        Integer stack = patch.getDataValue(this, STACK);
        if (energy == null || stack == null) return;
        float threshold = getEnergyCharge();
        int max = getMaxStack();
        while (energy >= threshold && stack < max) {
            energy -= threshold;
            stack++;
        }
        if (!Objects.equals(patch.getDataValue(this, ENERGY), energy)) {
            patch.setData(this, ENERGY, energy);
        }
        if (!Objects.equals(patch.getDataValue(this, STACK), stack)) {
            patch.setData(this, STACK, stack);
        }
    }
    @Override
    public void onHurtTargetPost(MaidHurtTargetEvent.Post event) {
        MaidPatch<?> patch = event.getMaidPatch();
        if (patch == null) return;
        EntityMaid maid = patch.getOriginal();
        if (!(maid.level() instanceof ServerLevel)) return;
        Float energy = patch.getDataValue(this, ENERGY);
        if (energy == null) return;
        patch.setData(this, ENERGY, energy + event.getAmount());
    }
    public void onRemove(MaidChangeItemEvent event) {
        MaidPatch<?> MaidPatch = event.getMaidPatch();
        MaidPatch.removeData(this);
    }
}
