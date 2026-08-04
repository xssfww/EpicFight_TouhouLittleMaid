package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.hm.efn.EFN;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
public class MeenSpearSkill extends WeaponInnateSkill {
    private static final float ENERGY_PER_STACK = 30.0F;
    private static final int MAX_STACKS = 3;
    public static final MaidSkillDataManager.SkillDataKey<Integer> CHARGING_TIME =
            MaidSkillDataManager.SkillDataKey.createDataKey(MaidSkillDataManager.SkillDataKey.INTEGER);
    public MeenSpearSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    protected float getEnergyCharge() { return ENERGY_PER_STACK; }
    @Override
    protected int getMaxStack() { return MAX_STACKS; }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        super.onInit(event);
        event.registerData(this, CHARGING_TIME, 0);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        super.onMaidTick(event,patch);
        Integer time = patch.getDataValue(this, CHARGING_TIME);
        if (time == null) return;
        if (time > 0) {
            patch.setData(this, CHARGING_TIME, time - 1);
        }
    }
    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(EFN.MODID, "textures/item/meen_spear.png");
    }
}