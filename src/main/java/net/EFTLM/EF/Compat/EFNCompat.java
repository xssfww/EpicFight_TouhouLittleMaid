package net.EFTLM.EF.Compat;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import com.guhao.efn_enhance.entity.fakeman.FakeManEntity;
import com.guhao.efn_enhance.gameassets.animations.EFN_ESekiroAnimations;
import com.hm.efn.entity.effect.BlastSummonedSwordEntity;
import com.hm.efn.entity.effect.SummonedSwordEntity_Out;
import com.hm.efn.gameasset.EFNAnimations;
import com.hm.efn.gameasset.animations.EFNDodgeAnimations;
import com.hm.efn.gameasset.animations.EFNLanceAnimations;
import com.hm.efn.gameasset.animations.EFNScytheAnimations;
import com.hm.efn.gameasset.animations.EFNSekiroAnimations;
import com.hm.efn.registries.EFNItem;
import com.hm.efn.registries.EFNMobEffectRegistry;
import net.EFTLM.EF.API.Event.MaidSkillBuildEvent;
import net.EFTLM.EF.Animation.CombatBehavior.BehaviorsBuild;
import net.EFTLM.EF.Animation.CombatBehavior.EFN.*;
import net.EFTLM.EF.Animation.CombatBehavior.EFTLM_Behaviors;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Model.EFTLM_Armatures;
import net.EFTLM.EF.Skill.Dodge.Step;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.WeaponInnate.EFN.*;
import net.EFTLM.EFTLM;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import java.util.List;
import java.util.Map;
import java.util.Objects;
public class EFNCompat {
    public static void trySetWeaponMotions(Map<Item, CombatBehaviors.Builder<HumanoidMobPatch<?>>> ItemAttackMotions, Map<Item, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> SpecialItemAttackMotions, Map<Item, HumanoidArmature> ItemArmatures) {
        if (CompatModList.LoadedEFN()) {
            Internal.setupWeaponMotions(ItemAttackMotions, SpecialItemAttackMotions, ItemArmatures);
        }
    }
    public static void tryBuildSkills(MaidSkillBuildEvent event) {
        if (CompatModList.LoadedEFN()) {
            Internal.setupSkills(event);
        }
    }
    public static void tryCreateSkills(MaidSkillBuildEvent.SkillCreateEvent<?> event) {
        if (CompatModList.LoadedEFN()) {
            Internal.setupCreate(event);
        }
    }
    public static void summonFakeMan(LivingEntityPatch<?> Patch, AssetAccessor<? extends StaticAnimation> animation, float transitionTimeModifier) {
        if (CompatModList.LoadedEFN() && CompatModList.LoadedEFN_Enhance()) {
            InternalEnhance.summonFakeMan(Patch, animation, transitionTimeModifier);
        }
    }
    public static boolean canSummonAtWaist(LivingEntityPatch<?> Patch) {
        if (CompatModList.LoadedEFN()) {
            YamatoSkill skill = BehaviorsBuild.getWeaponInnateSkill(Patch, YamatoSkill.class);
            if (skill == null) return false;
            Integer value = BehaviorsBuild.getDataValue(Patch, skill, YamatoSkill.formationTime);
            return value != null && value <= 0;
        }
        return false;
    }
    public static void summonAtWaist(LivingEntityPatch<?> Patch) {
        if (CompatModList.LoadedEFN()) {
            Internal.summonAtWaist(Patch);
        }
    }
    public static void summonBlastSword(LivingEntityPatch<?> Patch) {
        if (CompatModList.LoadedEFN()) {
            Internal.summonBlastSword(Patch);
        }
    }
    public static boolean canBeastRoar(LivingEntityPatch<?> Patch) {
        if (CompatModList.LoadedEFN()) {
            ClawSkill skill = BehaviorsBuild.getWeaponInnateSkill(Patch, ClawSkill.class);
            if (skill == null) return false;
            Integer value = BehaviorsBuild.getDataValue(Patch, skill, ClawSkill.CLAW_TIME);
            return value != null && value <= 0;
        }
        return false;
    }
    public static boolean isBloodLust(LivingEntityPatch<?> Patch) {
        if (CompatModList.LoadedEFN()) {
            return Internal.isBloodLust(Patch);
        }
        return false;
    }
    public static boolean isMeenCharging(LivingEntityPatch<?> Patch) {
        if (CompatModList.LoadedEFN()) {
            MeenSpearSkill skill = BehaviorsBuild.getWeaponInnateSkill(Patch, MeenSpearSkill.class);
            if (skill == null) return false;
            Integer value = BehaviorsBuild.getDataValue(Patch, skill, MeenSpearSkill.CHARGING_TIME);
            return value != null && value > 0;
        }
        return false;
    }
    public static boolean isMeenChargingBrink(LivingEntityPatch<?> Patch) {
        if (CompatModList.LoadedEFN()) {
            MeenSpearSkill skill = BehaviorsBuild.getWeaponInnateSkill(Patch, MeenSpearSkill.class);
            if (skill == null) return false;
            Integer value = BehaviorsBuild.getDataValue(Patch, skill, MeenSpearSkill.CHARGING_TIME);
            return value != null && value < 100 && isMeenCharging(Patch);
        }
        return false;
    }
    public static void clearMeenEffect(LivingEntityPatch<?> Patch) {
        if (CompatModList.LoadedEFN()) {
            Internal.clearMeenEffect(Patch);
        }
    }
    public static void giveBloodLust(LivingEntityPatch<?> Patch) {
        if (CompatModList.LoadedEFN()) {
            Internal.giveBloodLust(Patch);
        }
    }
    public static void clearBloodLust(LivingEntityPatch<?> Patch) {
        if (CompatModList.LoadedEFN()) {
            Internal.clearBloodLust(Patch);
        }
    }
    public static boolean isSpecialAnimation(MaidPatch<?> patch) {
        if (CompatModList.LoadedEFN()) {
            if (CompatModList.LoadedEFN_Enhance()) {
                return Internal.isSpecialAnimation(patch) || InternalEnhance.isSpecialAnimation(patch);
            }
            return Internal.isSpecialAnimation(patch);
        }
        return false;
    }
    protected static class Internal {
        static void setupWeaponMotions(Map<Item, CombatBehaviors.Builder<HumanoidMobPatch<?>>> ItemAttackMotions, Map<Item, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> SpecialItemAttackMotions, Map<Item, HumanoidArmature> ItemArmatures) {
            ItemAttackMotions.put(EFNItem.YAMATO_DMC_IN_SHEATH.get(), Yamato.getInstance());
            ItemAttackMotions.put(EFNItem.YAMATO_DMC4_IN_SHEATH.get(), Yamato.getInstance());
            ItemAttackMotions.put(EFNItem.HF_MURASAMA.get(), HF_Murasama.Instance);
            ItemAttackMotions.put(EFNItem.HF_BLADE.get(), HF_Blade.Instance);
            ItemAttackMotions.put(EFNItem.KUSABIMARU.get(), Kusabimaru.Instance);
            ItemAttackMotions.put(EFNItem.MEEN_SPEAR.get(), MeenSpear.Instance);
            ItemAttackMotions.put(EFNItem.RUINSGREATSWORD.get(), Ruins_GreatSword.Instance);
            ItemAttackMotions.put(EFNItem.AETHERIAL_DUSK_DUALSWORD.get(), Aetherial_Dusk.Instance);
            ItemAttackMotions.put(EFNItem.BROADBLADE.get(), BroadBlade.Instance);
            ItemAttackMotions.put(EFNItem.NF_CLAW.get(), Claw.Instance);
            ItemAttackMotions.put(EFNItem.AIR_TACHI.get(), BloodLust.Instance);
            ItemAttackMotions.put(EFNItem.CO_TACHI.get(), BloodLust.Instance);
            ItemAttackMotions.put(EFNItem.SWORD_OF_PIONEER.get(), Pioneer.Instance);
            ItemAttackMotions.put(EFNItem.NF_SHORT_SWORD.get(), ShortSword.Instance);
            ItemAttackMotions.put(EFNItem.NF_SHORT_SWORD_2.get(), ShortSword.Instance);
            ItemAttackMotions.put(EFNItem.CRIMSON_MOON.get(), Scythe.Instance);
            ItemAttackMotions.put(EFNItem.CRESCENT_MOON.get(), Crescent.Instance);
            SpecialItemAttackMotions.put(EFNItem.EXSILIUMGLADIUS.get(), ImmutableMap.of(
                    CapabilityItem.Styles.ONE_HAND, EFTLM_Behaviors.Sword_OneHand,
                    CapabilityItem.Styles.TWO_HAND, Exsiliumgladius.Instance)
            );
            ItemArmatures.put(EFNItem.NF_CLAW.get(), EFTLM_Armatures.Claw.get());
            ItemArmatures.put(EFNItem.BROADBLADE.get(), EFTLM_Armatures.BroadBlade.get());
            ItemArmatures.put(EFNItem.KUSABIMARU.get(), EFTLM_Armatures.Kusabimaru.get());
            ItemArmatures.put(EFNItem.YAMATO_DMC_IN_SHEATH.get(), Armatures.BIPED.get());
            ItemArmatures.put(EFNItem.YAMATO_DMC4_IN_SHEATH.get(), Armatures.BIPED.get());
            ItemArmatures.put(EFNItem.HF_MURASAMA.get(), Armatures.BIPED.get());
            ItemArmatures.put(EFNItem.HF_BLADE.get(), Armatures.BIPED.get());
        }
        static void setupSkills(MaidSkillBuildEvent event) {
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"board_blade_innate"),
                    BroadBladeSkill::new, MaidSkill.createBuilder(), EFNItem.BROADBLADE.get());
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"hf_murasama_innate"),
                    HF_MurasamaSkill::new, MaidSkill.createBuilder(), EFNItem.HF_MURASAMA.get());
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"hf_blade_innate"),
                    HF_BladeSkill::new, MaidSkill.createBuilder(), EFNItem.HF_BLADE.get());
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"yamato_innate"),
                    YamatoSkill::new, MaidSkill.createBuilder(), EFNItem.YAMATO_DMC_IN_SHEATH.get(), EFNItem.YAMATO_DMC4_IN_SHEATH.get());
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"meen_innate"),
                    MeenSpearSkill::new, MaidSkill.createBuilder(), EFNItem.MEEN_SPEAR.get());
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"claw_innate"),
                    ClawSkill::new, MaidSkill.createBuilder(), EFNItem.NF_CLAW.get());
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"scythe_innate"),
                    ScytheSkill::new, MaidSkill.createBuilder(), EFNItem.CRIMSON_MOON.get());
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "kusabimaru_innate"),
                    KusabimaruSkill::new, MaidSkill.createBuilder(), EFNItem.KUSABIMARU.get());
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "blood_lust_innate"),
                    BloodLustSkill::new, MaidSkill.createBuilder(), EFNItem.CO_TACHI.get(), EFNItem.AIR_TACHI.get());
        }
        static void setupCreate(MaidSkillBuildEvent.SkillCreateEvent<?> event) {
            if (event.getSkillBuilder() instanceof Step.Builder builder) {
                builder.addDodgeMotions(EFNItem.YAMATO_DMC_IN_SHEATH.get(),
                        List.of(EFNDodgeAnimations.YAMATO_STEP_F,EFNDodgeAnimations.YAMATO_STEP_B,
                                EFNDodgeAnimations.YAMATO_STEP_L,EFNDodgeAnimations.YAMATO_STEP_R));
                builder.addDodgeMotions(EFNItem.YAMATO_DMC4_IN_SHEATH.get(),
                        List.of(EFNDodgeAnimations.YAMATO_STEP_F,EFNDodgeAnimations.YAMATO_STEP_B,
                                EFNDodgeAnimations.YAMATO_STEP_L,EFNDodgeAnimations.YAMATO_STEP_R));
                builder.addDodgeMotions(EFNItem.KUSABIMARU.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.BROADBLADE.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.CRESCENT_MOON.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.MEEN_SPEAR.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.NF_SHORT_SWORD.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.NF_SHORT_SWORD_2.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.SWORD_OF_PIONEER.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.NF_CLAW.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.RUINSGREATSWORD.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.AETHERIAL_DUSK_DUALSWORD.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.AIR_TACHI.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.CO_TACHI.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.EXSILIUMGLADIUS.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.KUSABIMARU.get(),
                        List.of(EFNDodgeAnimations.DODGE_STEP_F,EFNDodgeAnimations.DODGE_STEP_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.HF_MURASAMA.get(),
                        List.of(EFNDodgeAnimations.MURASAMA_ROLL_F,EFNDodgeAnimations.MURASAMA_ROLL_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
                builder.addDodgeMotions(EFNItem.HF_BLADE.get(),
                        List.of(EFNDodgeAnimations.MURASAMA_ROLL_F,EFNDodgeAnimations.MURASAMA_ROLL_B,
                                EFNDodgeAnimations.DODGE_STEP_L,EFNDodgeAnimations.DODGE_STEP_R));
            }
        }
        static void clearMeenEffect(LivingEntityPatch<?> Patch) {
            Patch.getOriginal().removeEffect(EFNMobEffectRegistry.MEEN_LANCE.get());
        }
        static void summonAtWaist(LivingEntityPatch<?> Patch) {
            if (Patch instanceof MaidPatch<?> MaidPatch) {
                EntityMaid owner = MaidPatch.getOriginal();
                if (owner.level() instanceof ServerLevel level) {
                    Vec3 waistPos = owner.position().add(0.0, (double) owner.getBbHeight() * 0.6, 0.0);
                    Vec3 spawnPos = waistPos.add(Vec3.ZERO);
                    SummonedSwordEntity_Out Sword = new SummonedSwordEntity_Out(owner, 1.0F, Vec3.ZERO);
                    Sword.setPos(spawnPos);
                    Sword.setYRot(owner.getYRot());
                    Sword.setXRot(0.0F);
                    Patch.playSound(SoundEvents.AMETHYST_BLOCK_STEP, 1.0F, 1.0F);
                    level.addFreshEntity(Sword);
                    YamatoSkill skill = BehaviorsBuild.getWeaponInnateSkill(Patch, YamatoSkill.class);
                    if (skill == null) return;
                    MaidPatch.setData(skill,YamatoSkill.formationTime,600);
                }
            }
        }
        static void summonBlastSword(LivingEntityPatch<?> Patch) {
            if (Patch instanceof MaidPatch<?> MaidPatch) {
                EntityMaid owner = MaidPatch.getOriginal();
                if (owner.level() instanceof ServerLevel level) {
                    BlastSummonedSwordEntity.summon(level, owner);
                    MaidPatch.playSound(SoundEvents.TRIDENT_RETURN, 1.5F, 1.0F, 1.0F);
                }
            }
        }
        static boolean isSpecialAnimation(MaidPatch<?> patch) {
            AssetAccessor<? extends StaticAnimation> animation = Objects.requireNonNull(patch.getAnimator().getPlayerFor(null)).getRealAnimation();
            return animation == EFNAnimations.DMC5_V_JC
                    || animation == EFNLanceAnimations.NF_MEEN_CHARGING_MOB
                    || animation == EFNSekiroAnimations.MORTAL_BLADE_1
                    || animation == EFNSekiroAnimations.MORTAL_BLADE_2
                    || animation == EFNScytheAnimations.SCYTHE_SCARLET_END;
        }
        static boolean isBloodLust(LivingEntityPatch<?> Patch) {
            if (Patch instanceof MaidPatch<?> MaidPatch) {
                EntityMaid owner = MaidPatch.getOriginal();
                if (owner.level() instanceof ServerLevel) {
                    return owner.hasEffect(EFNMobEffectRegistry.BLODDLUST.get());
                }
            }
            return false;
        }
        static void giveBloodLust(LivingEntityPatch<?> Patch) {
            if (Patch instanceof MaidPatch<?> MaidPatch) {
                EntityMaid owner = MaidPatch.getOriginal();
                if (owner.level() instanceof ServerLevel) {
                    owner.addEffect(new MobEffectInstance(EFNMobEffectRegistry.BLODDLUST.get(),-1));
                }
            }
        }
        static void clearBloodLust(LivingEntityPatch<?> Patch) {
            if (Patch instanceof MaidPatch<?> MaidPatch) {
                EntityMaid owner = MaidPatch.getOriginal();
                if (owner.level() instanceof ServerLevel) {
                    owner.removeEffect(EFNMobEffectRegistry.BLODDLUST.get());
                }
            }
        }
    }
    protected static class InternalEnhance {
        static void summonFakeMan(LivingEntityPatch<?> Patch, AssetAccessor<? extends StaticAnimation> animation, float transitionTimeModifier) {
            if (Patch instanceof MaidPatch<?> MaidPatch) {
                if (MaidPatch.getOriginal().level() instanceof ServerLevel Level) {
                    PlayerPatch<?> Owner = MaidPatch.getOwnerPatch();
                    if (Owner instanceof ServerPlayerPatch ServerPatch) {
                        FakeManEntity FakeMan = new FakeManEntity(ServerPatch.getOriginal(), animation, transitionTimeModifier);
                        FakeMan.setItemInHand(InteractionHand.MAIN_HAND, MaidPatch.getOriginal().getMainHandItem().copy());
                        Vec3 vec3 = MaidPatch.getOriginal().position();
                        FakeMan.moveTo(new Vec3(vec3.x, vec3.y, vec3.z));
                        Level.addFreshEntity(FakeMan);
                    }
                }
            }
        }
        static boolean isSpecialAnimation(MaidPatch<?> patch) {
            AssetAccessor<? extends StaticAnimation> animation = Objects.requireNonNull(patch.getAnimator().getPlayerFor(null)).getRealAnimation();
            return animation == EFN_ESekiroAnimations.OPEN_MORTAL_BLADE_1;
        }
    }
}
