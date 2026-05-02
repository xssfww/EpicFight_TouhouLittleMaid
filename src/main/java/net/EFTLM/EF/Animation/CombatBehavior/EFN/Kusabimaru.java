package net.EFTLM.EF.Animation.CombatBehavior.EFN;

import com.guhao.efn_enhance.gameassets.animations.EFN_ESekiroAnimations;
import com.hm.efn.gameasset.animations.EFNDodgeAnimations;
import com.hm.efn.gameasset.animations.EFNSekiroAnimations;
import net.EFTLM.EF.Animation.CombatBehavior.BehaviorsBuild;
import net.EFTLM.EF.Compat.CompatModList;
import net.EFTLM.EF.Compat.EFNCompat;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
public class Kusabimaru {
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> Instance;
    static {
        if (CompatModList.LoadedEFN()) {
            if (CompatModList.LoadedEFN_Enhance()) {
                Instance = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(10)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(
                                                CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                        .animationBehavior(EFNDodgeAnimations.DODGE_STEP_F)
                                                        .withinDistance(8.0D, 20.0D))
                        )
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(10)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(
                                                CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                        .animationBehavior(EFN_ESekiroAnimations.SHADOW_RUSH)
                                                        .withinDistance(4.0D, 6.0D))
                        )
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(20)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFN_ESekiroAnimations.KUSABIMARU_AUTO1)
                                                .withinDistance(0.0D, 5.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFN_ESekiroAnimations.KUSABIMARU_AUTO2)
                                                .withinDistance(0.0D, 5.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFN_ESekiroAnimations.KUSABIMARU_AUTO3)
                                                .withinDistance(0.0D, 5.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFN_ESekiroAnimations.KUSABIMARU_AUTO4)
                                                .withinDistance(0.0D, 5.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFN_ESekiroAnimations.KUSABIMARU_AUTO5)
                                                .withinDistance(0.0D, 5.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFN_ESekiroAnimations.SAKURA_DANCE)
                                                .withinDistance(0.0D, 5.0D))
                        )
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(20)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFN_ESekiroAnimations.DRAGON_FLASH)
                                                .withinDistance(0.0D, 10.0D))
                        )
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(20)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFN_ESekiroAnimations.SAKURA_DANCE)
                                                .withinDistance(0.0D, 10.0D))
                        )
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(20)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFNSekiroAnimations.MORTAL_BLADE_1)
                                                .custom(BehaviorsBuild::canUseSkill)
                                                .withinDistance(0.0D, 20.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFNSekiroAnimations.MORTAL_BLADE_2)
                                                .withinDistance(0.0D, 20.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .behavior(Patch -> {
                                                    Patch.playAnimationSynchronized(EFN_ESekiroAnimations.OPEN_MORTAL_BLADE_1, 0F);
                                                    EFNCompat.summonFakeMan(Patch, EFN_ESekiroAnimations.FAKE_OPEN_MORTAL_BLADE_2, -0.2F);
                                                    BehaviorsBuild.setCoolDown(Patch,1200);
                                                })
                                                .withinDistance(0.0D, 20.0D))
                        );
            } else {
                Instance = CombatBehaviors.<HumanoidMobPatch<?>>builder()
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(10)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(
                                                CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                        .animationBehavior(EFNDodgeAnimations.DODGE_STEP_F)
                                                        .withinDistance(8.0D, 20.0D))
                        )
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(10)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(
                                                CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                        .animationBehavior(EFNSekiroAnimations.SHADOW_RUSH)
                                                        .withinDistance(4.0D, 6.0D))
                        )
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(20)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFNSekiroAnimations.KUSABIMARU_AUTO1)
                                                .withinDistance(0.0D, 5.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFNSekiroAnimations.KUSABIMARU_AUTO2)
                                                .withinDistance(0.0D, 5.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFNSekiroAnimations.KUSABIMARU_AUTO3)
                                                .withinDistance(0.0D, 5.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFNSekiroAnimations.KUSABIMARU_AUTO4)
                                                .withinDistance(0.0D, 5.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFNSekiroAnimations.KUSABIMARU_AUTO5)
                                                .withinDistance(0.0D, 5.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFNSekiroAnimations.SAKURA_DANCE)
                                                .withinDistance(0.0D, 5.0D))
                        )
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(20)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFNSekiroAnimations.DRAGON_FLASH)
                                                .withinDistance(0.0D, 10.0D))
                        )
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(20)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFNSekiroAnimations.SAKURA_DANCE)
                                                .withinDistance(0.0D, 10.0D))
                        )
                        .newBehaviorSeries(
                                CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                                        .cooldown(20)
                                        .weight(100.0F)
                                        .canBeInterrupted(false)
                                        .looping(false)
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .animationBehavior(EFNSekiroAnimations.MORTAL_BLADE_1)
                                                .custom(BehaviorsBuild::canUseSkill)
                                                .withinDistance(0.0D, 20.0D))
                                        .nextBehavior(CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                .behavior(Patch -> {
                                                    Patch.playAnimationSynchronized(EFNSekiroAnimations.MORTAL_BLADE_2, 0F);
                                                    BehaviorsBuild.setCoolDown(Patch,1200);
                                                })
                                                .withinDistance(0.0D, 20.0D))
                        );
            }
        }
    }
}
