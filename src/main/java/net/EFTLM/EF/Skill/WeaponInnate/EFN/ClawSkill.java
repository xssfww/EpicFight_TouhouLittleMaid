package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.hm.efn.EFN;
import net.EFTLM.EF.API.Event.MaidHurtTargetEvent;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
public class ClawSkill extends WeaponInnateSkill {
    private static final float ENERGY_PER_STACK = 30.0F;
    private static final int MAX_STACKS = 3;
    private static final float HEAL_RATIO = 0.25F;
    public static final MaidSkillDataManager.SkillDataKey<Integer> CLAW_TIME =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.INTEGER);
    public ClawSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    protected float getEnergyCharge() { return ENERGY_PER_STACK; }
    @Override
    protected int getMaxStack() { return MAX_STACKS; }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        super.onInit(event);
        event.registerData(this, CLAW_TIME, 0);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        super.onMaidTick(event,patch);
        Integer time = patch.getDataValue(this, CLAW_TIME);
        if (time == null) return;
        if (time > 0) {
            patch.setData(this, CLAW_TIME, time - 1);
        }
    }
    @Override
    public void onHurtTargetPost(MaidHurtTargetEvent.Post event) {
        super.onHurtTargetPost(event);
        MaidPatch<?> patch = event.getMaidPatch();
        EntityMaid maid = event.getMaidPatch().getOriginal();
        if (maid == null) return;
        Integer time = patch.getDataValue(this, CLAW_TIME);
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

