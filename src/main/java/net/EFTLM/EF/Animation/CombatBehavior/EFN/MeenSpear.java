package net.EFTLM.EF.Animation.CombatBehavior.EFN;

import com.hm.efn.gameasset.animations.EFNLanceAnimations;
import net.EFTLM.EF.Animation.CombatBehavior.BehaviorsBuild;
import net.EFTLM.EF.Compat.CompatModList;
import net.EFTLM.EF.Compat.EFNCompat;
import net.EFTLM.EF.Skill.WeaponInnate.EFN.MeenSpearSkill;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
public class MeenSpear {
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> Instance;
    static {
        if (CompatModList.LoadedEFN()) {
            Instance = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(10)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNLanceAnimations.NF_MEEN_DASH)
                                            .withinDistance(5.0D, 10.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(10)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNLanceAnimations.NF_MEEN_AUTO1)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNLanceAnimations.NF_MEEN_AUTO2)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNLanceAnimations.NF_MEEN_AUTO3)
                                            .withinDistance(0.0D, 5.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNLanceAnimations.NF_MEEN_AUTO4)
                                            .withinDistance(0.0D, 5.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(10)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .custom(Patch -> BehaviorsBuild.hasStack(Patch,1))
                                            .behavior(Patch -> {
                                                Patch.playAnimationSynchronized(EFNLanceAnimations.NF_MEEN_CHARGE1, 0F);
                                                MeenSpearSkill skill = BehaviorsBuild.getWeaponInnateSkill(Patch, MeenSpearSkill.class);
                                                if (skill == null) return;
                                                BehaviorsBuild.setStack(Patch, BehaviorsBuild.getStack(Patch) - 1);
                                            })
                                            .withinDistance(0.0D, 6.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(10)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .custom(Patch -> BehaviorsBuild.hasStack(Patch,1))
                                            .behavior(Patch -> {
                                                Patch.playAnimationSynchronized(EFNLanceAnimations.NF_MEEN_CHARGE2, 0F);
                                                MeenSpearSkill skill = BehaviorsBuild.getWeaponInnateSkill(Patch, MeenSpearSkill.class);
                                                if (skill == null) return;
                                                BehaviorsBuild.setStack(Patch, BehaviorsBuild.getStack(Patch) - 1);
                                            })
                                            .withinDistance(6.0D, 12.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(10)
                            .weight(200.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .custom(EFNCompat::isMeenChargingBrink)
                                            .behavior(Patch -> {
                                                Patch.playAnimationSynchronized(EFNLanceAnimations.NF_MEEN_FINISHER, 0F);
                                                EFNCompat.clearMeenEffect(Patch);
                                            })
                                            .withinDistance(0.0D, 6.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(20)
                            .weight(150.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .custom(Patch -> !EFNCompat.isMeenCharging(Patch) && BehaviorsBuild.hasStack(Patch,1))
                                            .behavior(Patch -> {
                                                Patch.playAnimationSynchronized(EFNLanceAnimations.NF_MEEN_CHARGING_MOB, 0F);
                                                MeenSpearSkill skill = BehaviorsBuild.getWeaponInnateSkill(Patch, MeenSpearSkill.class);
                                                if (skill == null) return;
                                                BehaviorsBuild.setData(Patch, skill, MeenSpearSkill.CHARGING_TIME, 1000);
                                                BehaviorsBuild.setStack(Patch, BehaviorsBuild.getStack(Patch) - 1);
                                            })
                                            .withinDistance(0.0D, 10.0D))
                    );
        }
    }
}
