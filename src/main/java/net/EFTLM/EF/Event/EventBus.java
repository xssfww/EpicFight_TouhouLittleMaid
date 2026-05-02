package net.EFTLM.EF.Event;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAttackEvent;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTaskEnableEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.EFTLM.EF.Animation.EFTLM_Animations;
import net.EFTLM.EF.Animation.EFTLM_LivingMotions;
import net.EFTLM.EF.Api.Event.CombatBehaviorsEvent;
import net.EFTLM.EF.Capability.ClientMaidPatch;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Compat.EFNCompat;
import net.EFTLM.EF.Model.EFTLM_Armatures;
import net.EFTLM.EFTLM;
import net.EFTLM.TLM.Task.FightModeTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.api.forgeevent.InitAnimatorEvent;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
public class EventBus {
    @Mod.EventBusSubscriber(
            modid = EFTLM.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class ForgeEvents {
        @SubscribeEvent
        public static void RegisterAnimator(InitAnimatorEvent event) {
            if (event.getEntityPatch() instanceof PlayerPatch<?>) {
                Animator animator = event.getAnimator();
                animator.addLivingAnimation(EFTLM_LivingMotions.HUG, EFTLM_Animations.Biped_Hug);
                animator.addLivingAnimation(EFTLM_LivingMotions.HUG_KNEEL, EFTLM_Animations.Biped_Hug_Kneel);
                animator.addLivingAnimation(EFTLM_LivingMotions.HUG_WALK, EFTLM_Animations.Biped_Hug_Walk);
                animator.addLivingAnimation(EFTLM_LivingMotions.HUG_RUN, EFTLM_Animations.Biped_Hug_Run);
                animator.addLivingAnimation(EFTLM_LivingMotions.HUG_SNEAK, EFTLM_Animations.Biped_Hug_Sneak);
            }
        }
        @SubscribeEvent
        public static void MaidAttackBehaviors(CombatBehaviorsEvent event) {
            EFNCompat.trySetWeaponMotions(event.getItemAttackMotions(), event.getItemStyleAttackMotions(), event.getItemArmatures());
        }
        @SubscribeEvent
        public static void MaidChangeTask(MaidTaskEnableEvent event) {
            if (event.getTargetTask() instanceof FightModeTask) {
                MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(event.getEntityMaid(),MaidPatch.class);
                if (MaidPatch != null) {
                    MaidPatch.resetAi();
                }
            }
        }
        @SubscribeEvent
        public static void MaidAttack(MaidAttackEvent event) {
            EntityMaid Maid = event.getMaid();
            MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(Maid, MaidPatch.class);
            DamageSource Source = event.getSource();
            if (Maid.level() instanceof ServerLevel Level) {
                if (MaidPatch != null) {
                    PlayerPatch<?> OwnerPatch = MaidPatch.getOwnerPatch();
                    if (Source.getEntity() != null) {
                        int phaseLevel = MaidPatch.getEntityState().getLevel();
                        if (OwnerPatch.getOriginal().equals(Source.getEntity())) {
                            return;
                        }
                        if (EFNCompat.isInvulnerability(MaidPatch) || EFNCompat.isImmunity(MaidPatch)) {
                            return;
                        }
                        if (phaseLevel > 0 && phaseLevel < 3 && MaidPatch.canUseSkill() && MaidPatch.isFrontAttack(Source) && MaidPatch.isBlockableSource(Source)) {
                            MaidPatch.playSound(EpicFightSounds.CLASH.get(), -0.05F, 0.1F);
                            MaidPatch.setCoolDown(20);
                            EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument(Level, HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, Maid, Source.getDirectEntity());
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }
    @Mod.EventBusSubscriber(
            modid = EFTLM.MODID,
            bus = Mod.EventBusSubscriber.Bus.MOD
    )
    public static class ModEvents {
        @SubscribeEvent
        public static void RegistryPatch(EntityPatchRegistryEvent event) {
            event.getTypeEntry().put(InitEntities.MAID.get(), (entity -> {
                if (entity.level().isClientSide()) {
                    return ClientMaidPatch::new;
                } else {
                    return MaidPatch::new;
                }
            }));
        }
        @SubscribeEvent
        public static void CommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(EFTLM_Armatures::RegisterArmatures);
        }
    }
}

