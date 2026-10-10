package net.EFTLM.EFN.CombatBehavior;

import com.hm.efn.gameasset.animations.EFNSekiroAnimations;
import net.EFTLM.EF.Animation.BehaviorsBuild;
import net.EFTLM.EF.Compat.CompatModList;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
// 1.21.1: the Nightfall-Enhance (com.guhao.efn_enhance) variant of this behaviour is
// dropped - that addon has no 1.21.1 build, so only the base EFNSekiroAnimations are used.
public class Kusabimaru {
    public static CombatBehaviors.Builder<HumanoidMobPatch<?>> Instance;
    static {
        if (CompatModList.LoadedEFN()) {
            Instance = CombatBehaviors.<HumanoidMobPatch<?>>builder()
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
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .custom(Patch -> BehaviorsBuild.hasStack(Patch,1))
                                                    .behavior(Patch -> {
                                                        Patch.playAnimationSynchronized(EFNSekiroAnimations.MORTAL_BLADE_1, 0F);
                                                        BehaviorsBuild.setStack(Patch,BehaviorsBuild.getStack(Patch) - 1);
                                                    })
                                                    .withinDistance(0.0D, 20.0D))
                                    .nextBehavior(
                                            CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                                    .custom(Patch -> BehaviorsBuild.hasStack(Patch,1))
                                                    .behavior(Patch -> {
                                                        Patch.playAnimationSynchronized(EFNSekiroAnimations.MORTAL_BLADE_2, 0F);
                                                        BehaviorsBuild.setStack(Patch,BehaviorsBuild.getStack(Patch) - 1);
                                                    })
                                                    .withinDistance(0.0D, 20.0D))
                    );
        }
    }
}
