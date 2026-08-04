package net.EFTLM.EF.Animation.CombatBehavior.EFN;

import com.hm.efn.gameasset.animations.EFNClawAnimations_N;
import com.hm.efn.registries.EFNMobEffectRegistry;
import net.EFTLM.EF.Animation.CombatBehavior.BehaviorsBuild;
import net.EFTLM.EF.Compat.CompatModList;
import net.EFTLM.EF.Compat.EFNCompat;
import net.EFTLM.EF.Skill.WeaponInnate.EFN.ClawSkill;
import net.minecraft.world.effect.MobEffectInstance;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
public class Claw {
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
                                            .animationBehavior(EFNClawAnimations_N.NF_CLAW_DASH)
                                            .withinDistance(3.0D, 5.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(20)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNClawAnimations_N.NF_CLAW_DASH)
                                            .withinDistance(3.0D, 5.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(10)
                            .weight(100.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNClawAnimations_N.NF_CLAW_AUTO1)
                                            .withinDistance(0.0D, 4.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNClawAnimations_N.NF_CLAW_AUTO2)
                                            .withinDistance(0.0D, 4.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNClawAnimations_N.NF_CLAW_AUTO3)
                                            .withinDistance(0.0D, 4.0D))
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .animationBehavior(EFNClawAnimations_N.NF_CLAW_AIRSLASH)
                                            .withinDistance(0.0D, 4.0D))
                    )
                    .newBehaviorSeries(CombatBehaviors.BehaviorSeries.<HumanoidMobPatch<?>>builder()
                            .cooldown(20)
                            .weight(150.0F)
                            .canBeInterrupted(false)
                            .looping(false)
                            .nextBehavior(
                                    CombatBehaviors.Behavior.<HumanoidMobPatch<?>>builder()
                                            .custom(Patch -> EFNCompat.canBeastRoar(Patch) && BehaviorsBuild.hasStack(Patch,1))
                                            .behavior(Patch -> {
                                                Patch.playAnimationSynchronized(EFNClawAnimations_N.NF_CLAW_BEASTROAR,0F);
                                                ClawSkill skill = BehaviorsBuild.getWeaponInnateSkill(Patch, ClawSkill.class);
                                                if (skill == null) return;
                                                BehaviorsBuild.setStack(Patch,0);
                                                BehaviorsBuild.setData(Patch,skill,ClawSkill.CLAW_TIME, 600);
                                                Patch.getOriginal().addEffect(new MobEffectInstance(EFNMobEffectRegistry.CLAW.get(), 600, 0));
                                            })
                                            .withinDistance(0.0D, 4.0D))
                    );
        }
    }
}
