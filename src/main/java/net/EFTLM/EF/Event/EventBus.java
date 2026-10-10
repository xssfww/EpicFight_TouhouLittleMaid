package net.EFTLM.EF.Event;

import com.github.tartaricacid.touhoulittlemaid.api.event.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.Maps;
import net.EFTLM.EF.API.Data.BehaviorReloadListener;
import net.EFTLM.EF.API.Event.*;
import net.EFTLM.EF.Animation.EFTLM_Animations;
import net.EFTLM.EF.Animation.EFTLM_LivingMotions;
import net.EFTLM.EF.Capability.ClientMaidPatch;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Compat.EFNCompat;
import net.EFTLM.EF.Item.MaidSkillBookItem;
import net.EFTLM.EF.Model.EFTLM_Armatures;
import net.EFTLM.EF.Skill.Dodge.Step;
import net.EFTLM.EF.Skill.Guard.BladeClash;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.EFTLM.EF.Utils.CompoundTagManager;
import net.EFTLM.EFTLM;
import net.EFTLM.TLM.Task.FightModeTask;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.api.event.types.animation.InitAnimatorEvent;
import yesman.epicfight.api.event.types.registry.EntityPatchRegistryEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {
    /**
     * Epic Fight 21.x no longer fires its events on the NeoForge bus; it has its own
     * {@link EpicFightEventHooks} registry. Called from the mod constructor so the listeners are in
     * place before Epic Fight builds its entity patch / animator registries.
     */
    public static void RegisterEpicFightHooks() {
        EpicFightEventHooks.Animation.INIT_ANIMATOR.registerEvent(ForgeEvents::RegisterAnimator);
        EpicFightEventHooks.Registry.ENTITY_PATCH.registerEvent(ModEvents::RegistryPatch);
    }

    @EventBusSubscriber(
            modid = EFTLM.MODID,
            bus = EventBusSubscriber.Bus.GAME
    )
    public static class ForgeEvents {
        public static void RegisterAnimator(InitAnimatorEvent event) {
            if (!(event.getEntityPatch() instanceof PlayerPatch<?>)) return;
            Animator animator = event.getAnimator();
            animator.addLivingAnimation(EFTLM_LivingMotions.HUG, EFTLM_Animations.Biped_Hug);
            animator.addLivingAnimation(EFTLM_LivingMotions.HUG_KNEEL, EFTLM_Animations.Biped_Hug_Kneel);
            animator.addLivingAnimation(EFTLM_LivingMotions.HUG_WALK, EFTLM_Animations.Biped_Hug_Walk);
            animator.addLivingAnimation(EFTLM_LivingMotions.HUG_RUN, EFTLM_Animations.Biped_Hug_Run);
            animator.addLivingAnimation(EFTLM_LivingMotions.HUG_SNEAK, EFTLM_Animations.Biped_Hug_Sneak);
        }
        @SubscribeEvent
        public static void MaidKilledListener(LivingDeathEvent event) {
            if (!(event.getEntity().level() instanceof ServerLevel)) return;
            if (!(event.getSource().getEntity() instanceof EntityMaid maid)) return;
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
            if (maidPatch == null) return;
            NeoForge.EVENT_BUS.post(new MaidKilledEvent(maidPatch, event.getEntity(), event.getSource()));
        }
        @SubscribeEvent
        public static void MaidAttackListener(LivingIncomingDamageEvent event) {
            if (!(event.getEntity().level() instanceof ServerLevel)) return;
            if (!(event.getSource().getEntity() instanceof EntityMaid maid)) return;
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
            if (maidPatch == null) return;
            NeoForge.EVENT_BUS.post(new MaidHurtTargetEvent.Pre(maidPatch, event.getEntity(), event.getSource()));
        }
        /**
         * 1.21.1 fix: Epic Fight applies its damage through its own animation pipeline
         * (entity.hurt(...)), which bypasses {@code Player#attack} — so the owner's
         * {@code lastHurtMob} never gets updated during an Epic Fight battle.
         * <p>
         * Touhou Little Maid decides whether a maid may attack something in
         * {@code IAttackTask#canAttack} / {@code DefaultMonsterType}: anything that is not a vanilla
         * {@code Enemy} counts as NEUTRAL and is only attacked while it is the owner's
         * {@code lastHurtMob} / {@code lastHurtByMob} (or the maid's {@code lastHurtByMob}).
         * Because Epic Fight attacks never recorded the target, maids ignored exactly those
         * mobs — typically modded monsters, which rarely implement {@code Enemy} — that the player
         * was fighting, while vanilla monsters (HOSTILE) still worked.
         * <p>
         * Mirroring what vanilla {@code Player#attack} does restores the maid's assist behaviour.
         */
        @SubscribeEvent
        public static void MaidOwnerHurtTargetRecord(LivingIncomingDamageEvent event) {
            if (event.getSource().getEntity() instanceof Player player
                    && event.getEntity() instanceof LivingEntity target
                    && player.getLastHurtMob() != target) {
                player.setLastHurtMob(target);
            }
        }
        @SubscribeEvent
        public static void MaidHurtListener(LivingDamageEvent.Post event) {
            if (!(event.getEntity().level() instanceof ServerLevel)) return;
            if (!(event.getSource().getEntity() instanceof EntityMaid maid)) return;
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
            if (maidPatch == null) return;
            NeoForge.EVENT_BUS.post(new MaidHurtTargetEvent.Post(maidPatch, event.getEntity(), event.getSource(), event.getNewDamage()));
        }
        @SubscribeEvent
        public static void MaidInteract(InteractMaidEvent event) {
            EntityMaid maid = event.getMaid();
            ItemStack stack = event.getStack();
            if (!(stack.getItem() instanceof MaidSkillBookItem)) return;
            if (!(maid.level() instanceof ServerLevel)) return;
            MaidSkill skill = MaidSkillBookItem.getContainSkill(stack);
            if (skill == null) return;
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
            if (maidPatch == null) return;
            if (maidPatch.hasLearnedSkill(skill.getRegistryName())) {
                event.getPlayer().displayClientMessage(Component.translatable("message.eftlm.learn_skill_failure"), true);
                event.setCanceled(true);
                return;
            }
            maidPatch.addLearnedSkill(skill.getRegistryName());
            maidPatch.playSound(SoundEvents.PLAYER_LEVELUP, -0.05F, 0.1F);
            event.getPlayer().displayClientMessage(Component.translatable("message.eftlm.learn_skill_success", skill.getTitle()), true);
            stack.setCount(0);
            event.setCanceled(true);
        }
        @SubscribeEvent
        public static void MaidChangeTask(MaidTaskEnableEvent event) {
            if (!(event.getTargetTask() instanceof FightModeTask)) return;
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(event.getEntityMaid(), MaidPatch.class);
            if (maidPatch != null) {
                maidPatch.resetAi();
            }
        }
        @SubscribeEvent
        public static void MaidTransformItem(MaidAndItemTransformEvent.ToItem event) {
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            if (maidPatch == null) return;
            CompoundTag nbt = event.getData();
            ListTag skillsList = new ListTag();
            for (ResourceLocation id : maidPatch.getLearnedSkills()) {
                skillsList.add(StringTag.valueOf(id.toString()));
            }
            nbt.put(CompoundTagManager.LearnedSkills, skillsList);
        }
        @SubscribeEvent
        public static void ItemTransformMaid(MaidAndItemTransformEvent.ToMaid event) {
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            if (maidPatch == null) return;
            CompoundTag nbt = event.getData();
            if (!nbt.contains(CompoundTagManager.LearnedSkills)) return;
            ListTag skillsList = nbt.getList(CompoundTagManager.LearnedSkills, StringTag.TAG_STRING);
            for (int i = 0; i < skillsList.size(); i++) {
                maidPatch.addLearnedSkill(ResourceLocation.parse(skillsList.getString(i)));
            }
        }
        @SubscribeEvent
        public static void MaidAttack(MaidAttackEvent event) {
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            forEachExecutableSkill(maidPatch, skill -> skill.MaidAttack(event));
        }
        @SubscribeEvent
        public static void MaidHurt(MaidHurtEvent event) {
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            forEachExecutableSkill(maidPatch, skill -> skill.MaidHurt(event));
        }
        @SubscribeEvent
        public static void MaidDamage(MaidDamageEvent event) {
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            forEachExecutableSkill(maidPatch, skill -> skill.MaidDamage(event));
        }
        @SubscribeEvent
        public static void MaidDeath(MaidDeathEvent event) {
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            forEachLearnedSkill(maidPatch, skill -> skill.MaidDeath(event));
        }
        @SubscribeEvent
        public static void MaidTick(MaidTickEvent event) {
            MaidPatch<?> maidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            forEachExecutableSkill(maidPatch, skill -> skill.MaidTick(event));
        }
        @SubscribeEvent
        public static void MaidHurtTargetPre(MaidHurtTargetEvent.Pre event) {
            MaidPatch<?> maidPatch = event.getMaidPatch();
            forEachExecutableSkill(maidPatch, skill -> skill.onHurtTargetPre(event));
        }
        @SubscribeEvent
        public static void MaidHurtTargetPost(MaidHurtTargetEvent.Post event) {
            MaidPatch<?> maidPatch = event.getMaidPatch();
            forEachExecutableSkill(maidPatch, skill -> skill.onHurtTargetPost(event));
        }
        @SubscribeEvent
        public static void MaidKillTarget(MaidKilledEvent event) {
            MaidPatch<?> maidPatch = event.getMaidPatch();
            forEachExecutableSkill(maidPatch, skill -> skill.onKillTarget(event));
        }
        @SubscribeEvent
        public static void MaidChangeItem(MaidChangeItemEvent event) {
            MaidPatch<?> maidPatch = event.getMaidPatch();
            if (maidPatch == null) return;
            if (!(maidPatch.getOriginal().level() instanceof ServerLevel)) return;
            WeaponInnateSkill targetWeaponSkill = maidPatch.getWeaponInnateSkill();
            ResourceLocation targetSkill = targetWeaponSkill != null ? targetWeaponSkill.getRegistryName() : null;
            List<ResourceLocation> toRemove = new ArrayList<>();
            forEachLearnedSkill(maidPatch, skill -> {
                if (skill instanceof WeaponInnateSkill innate && !innate.getRegistryName().equals(targetSkill)) {
                    innate.onRemove(event);
                    toRemove.add(innate.getRegistryName());
                }
            });
            toRemove.forEach(maidPatch::removeLearnedSkill);
            if (targetSkill != null) {
                maidPatch.addLearnedSkill(targetSkill);
            }
        }
        @SubscribeEvent
        public static void MaidSkillInit(MaidSkillInitEvent event) {
            MaidPatch<?> maidPatch = event.getMaidPatch();
            if (maidPatch == null) return;
            if (!(maidPatch.getOriginal().level() instanceof ServerLevel)) return;
            ResourceLocation target = event.getSkillName();
            if (target == null) {
                forEachLearnedSkill(maidPatch, skill -> skill.onInit(event));
            } else {
                MaidSkill skill = MaidSkillManager.getSkillFor(target);
                if (skill != null) {
                    skill.onInit(event);
                }
            }
        }
        @SubscribeEvent
        public static void MaidSkillRemove(MaidSkillRemoveEvent event) {
            MaidPatch<?> maidPatch = event.getMaidPatch();
            if (maidPatch == null) return;
            if (!(maidPatch.getOriginal().level() instanceof ServerLevel)) return;
            MaidSkill skill = event.getSkill();
            if (skill == null) return;
            if (skill instanceof WeaponInnateSkill) {
                event.setCanceled(true);
                return;
            }
            maidPatch.removeLearnedSkill(skill.getRegistryName());
            event.removeData();
        }
        @SubscribeEvent
        public static void MaidCombatBehaviors(CombatBehaviorsEvent event) {
            event.getItemAttackMotions().putAll(BehaviorReloadListener.ITEM_ATTACK_MOTIONS);
            event.getItemArmatures().putAll(BehaviorReloadListener.ITEM_ARMATURES);
            for (Map.Entry<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> entry : BehaviorReloadListener.WEAPON_STYLE_MOTIONS.entrySet()) {
                event.getWeaponStyleAttackMotions().computeIfAbsent(entry.getKey(), k -> Maps.newHashMap()).putAll(entry.getValue());
            }
            for (Map.Entry<Item, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> entry : BehaviorReloadListener.ITEM_STYLE_MOTIONS.entrySet()) {
                event.getItemStyleAttackMotions().computeIfAbsent(entry.getKey(), k -> Maps.newHashMap()).putAll(entry.getValue());
            }
            EFNCompat.trySetWeaponMotions(event.getItemAttackMotions(), event.getItemStyleAttackMotions(), event.getItemArmatures());
        }
        private static void forEachExecutableSkill(MaidPatch<?> patch, Consumer<MaidSkill> action) {
            if (patch == null) return;
            for (ResourceLocation rl : patch.getLearnedSkills()) {
                MaidSkill skill = MaidSkillManager.getSkillFor(rl);
                if (skill != null && skill.canExecute(patch)) {
                    action.accept(skill);
                }
            }
        }
        private static void forEachLearnedSkill(MaidPatch<?> patch, Consumer<MaidSkill> action) {
            if (patch == null) return;
            for (ResourceLocation rl : patch.getLearnedSkills()) {
                MaidSkill skill = MaidSkillManager.getSkillFor(rl);
                if (skill != null) {
                    action.accept(skill);
                }
            }
        }
    }

    @EventBusSubscriber(
            modid = EFTLM.MODID,
            bus = EventBusSubscriber.Bus.MOD
    )
    public static class ModEvents {
        public static void RegistryPatch(EntityPatchRegistryEvent event) {
            event.registerEntityPatch(InitEntities.MAID.get(), entity -> {
                if (entity.level().isClientSide()) {
                    return new ClientMaidPatch(entity);
                } else {
                    return new MaidPatch<>(entity);
                }
            });
        }
        @SubscribeEvent
        public static void AttributeModificationEvent(EntityAttributeModificationEvent event) {
            MaidPatch.initAttribute(event);
        }
        @SubscribeEvent
        public static void MaidSkillBuild(MaidSkillBuildEvent event) {
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "blade_clash"), BladeClash::new,
                    BladeClash.createBuilder());
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "step"), Step::new,
                    Step.createStepBuilder());
            EFNCompat.tryBuildSkills(event);
        }
        @SubscribeEvent
        public static void MaidSkillCreate(MaidSkillBuildEvent.SkillCreateEvent<?> event) {
            EFNCompat.tryCreateSkills(event);
        }
        @SubscribeEvent
        public static void CommonSetup(FMLCommonSetupEvent event) {
            EFTLM_Armatures.RegisterArmatures();
            MaidSkillManager.MaidSkillBuild();
        }
    }
}
