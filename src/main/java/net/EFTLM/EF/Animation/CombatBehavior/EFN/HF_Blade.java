package net.EFTLM.EF.Animation.CombatBehavior.EFN;

import com.hm.efn.gameasset.animations.EFNHfBladeAnimations;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Compat.CompatModList;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.EFTLM.EF.Skill.WeaponInnate.EFN.HF_BladeSkill;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
public class HF_Blade {
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> Instance;
    static {
        if (CompatModList.LoadedEFN()) {
            Instance = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(20)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_X)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XX)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_DASH_Y_SP)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XXY_CHARGE)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XXXY)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XXXY_CHARGE)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XXXX)
                                            .withinDistance(0.0D, 5.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(20)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_X_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_XX_AIR)
                                            .withinDistance(0.0D, 2.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNHfBladeAnimations.HF_BLADE_Y_CHARGE_THROUGH)
                                            .withinDistance(0.0D, 4.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(20)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                    .animationBehavior(EFNHfBladeAnimations.HF_BLADE_Y_CHARGE_THROUGH)
                                    .withinDistance(2.0D, 4.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(300)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .behavior(patch -> {
                                                if (patch instanceof MaidPatch<?> maid) {
                                                    for (ResourceLocation RL : maid.getLearnedSkills()) {
                                                        if (MaidSkillManager.getSkillFor(RL) instanceof HF_BladeSkill blade) {
                                                            maid.setData(blade, HF_BladeSkill.isZansetsu_Blade,true);
                                                        }
                                                    }
                                                }
                                            })
                                            .withinDistance(0.0D, 2.0D))
                    );
        }
    }
}
