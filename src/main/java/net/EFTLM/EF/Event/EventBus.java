package net.EFTLM.EF.Event;

import com.github.tartaricacid.touhoulittlemaid.api.event.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.EFTLM.EF.API.Event.*;
import net.EFTLM.EF.Animation.EFTLM_Animations;
import net.EFTLM.EF.Animation.EFTLM_LivingMotions;
import net.EFTLM.EF.Capability.ClientMaidPatch;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Compat.EFNCompat;
import net.EFTLM.EF.Item.MaidSkillBookItem;
import net.EFTLM.EF.Model.EFTLM_Armatures;
import net.EFTLM.EF.Register.EFTLM_Tab;
import net.EFTLM.EF.Skill.Dodge.Step;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.EFTLM.EF.Skill.Guard.BladeClash;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.api.forgeevent.InitAnimatorEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import java.util.ArrayList;
import java.util.List;
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
        public static void MaidKilledListener(LivingDeathEvent event) {
            if (!(event.getEntity().level() instanceof ServerLevel)) return;
            if (event.getSource().getEntity() instanceof EntityMaid Maid) {
                MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(Maid, MaidPatch.class);
                if (MaidPatch != null) {
                    MinecraftForge.EVENT_BUS.post(new MaidKilledEvent(MaidPatch, event.getEntity(), event.getSource()));
                }
            }
        }
        @SubscribeEvent
        public static void MaidHurtListener(LivingHurtEvent event) {
            if (!(event.getEntity().level() instanceof ServerLevel)) return;
            if (event.getSource().getEntity() instanceof EntityMaid maid) {
                MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
                if (MaidPatch != null) {
                    MaidHurtTargetEvent.Pre Pre = new MaidHurtTargetEvent.Pre(MaidPatch, event.getEntity(), event.getSource());
                    MinecraftForge.EVENT_BUS.post(Pre);
                    if (Pre.isCanceled()) {
                        event.setCanceled(true);
                        return;
                    }
                    MinecraftForge.EVENT_BUS.post(new MaidHurtTargetEvent.Post(MaidPatch, event.getEntity(), event.getSource()));
                }
            }
        }
        @SubscribeEvent
        public static void MaidInteract(InteractMaidEvent event) {
            EntityMaid Maid = event.getMaid();
            ItemStack stack = event.getStack();
            if (stack.getItem() instanceof MaidSkillBookItem) {
                if (Maid.level() instanceof ServerLevel) {
                    MaidSkill Skill = MaidSkillBookItem.getContainSkill(stack);
                    if (Skill != null) {
                        MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(Maid, MaidPatch.class);
                        if (MaidPatch != null) {
                            if (MaidPatch.hasLearnedSkill(Skill.getRegistryName())) {
                                event.getPlayer().displayClientMessage(Component.translatable("message.eftlm.learn_skill_failure"),true);
                                event.setCanceled(true);
                            } else {
                                MaidPatch.addLearnedSkill(Skill.getRegistryName());
                                MaidPatch.playSound(SoundEvents.PLAYER_LEVELUP, -0.05F, 0.1F);
                                event.getPlayer().displayClientMessage(Component.translatable("message.eftlm.learn_skill_success", Skill.getTitle()),true);
                                stack.setCount(0);
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
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
        public static void MaidTransformItem(MaidAndItemTransformEvent.ToItem event) {
            MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            if (MaidPatch != null) {
                CompoundTag NBT = event.getData();
                ListTag SkillsList = new ListTag();
                for (ResourceLocation SkillRegisterId : MaidPatch.getLearnedSkills()) {
                    SkillsList.add(StringTag.valueOf(SkillRegisterId.toString()));
                }
                NBT.put(CompoundTagManager.LearnedSkills, SkillsList);
            }
        }
        @SubscribeEvent
        public static void ItemTransformMaid(MaidAndItemTransformEvent.ToMaid event) {
            MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            if (MaidPatch != null) {
                CompoundTag NBT = event.getData();
                if (NBT.contains(CompoundTagManager.LearnedSkills)) {
                    ListTag SkillsList = NBT.getList(CompoundTagManager.LearnedSkills, StringTag.TAG_STRING);
                    for (int i = 0; i < SkillsList.size(); i++) {
                        MaidPatch.addLearnedSkill(ResourceLocation.parse(SkillsList.getString(i)));
                    }
                }
            }
        }
        @SubscribeEvent
        public static void MaidAttack(MaidAttackEvent event) {
            MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            if (MaidPatch != null) {
                List<ResourceLocation> Skills = MaidPatch.getLearnedSkills();
                Skills.forEach(RL -> {
                    MaidSkill Skill = MaidSkillManager.getSkillFor(RL);
                    if (Skill != null) {
                        if (Skill.canExecute(MaidPatch)) {
                            Skill.MaidAttack(event);
                        }
                    }
                });
            }
        }
        @SubscribeEvent
        public static void MaidHurt(MaidHurtEvent event) {
            MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            if (MaidPatch != null) {
                List<ResourceLocation> Skills = MaidPatch.getLearnedSkills();
                Skills.forEach(RL -> {
                    MaidSkill Skill = MaidSkillManager.getSkillFor(RL);
                    if (Skill != null) {
                        if (Skill.canExecute(MaidPatch)) {
                            Skill.MaidHurt(event);
                        }
                    }
                });
            }
        }
        @SubscribeEvent
        public static void MaidDamage(MaidDamageEvent event) {
            MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            if (MaidPatch != null) {
                List<ResourceLocation> Skills = MaidPatch.getLearnedSkills();
                Skills.forEach(RL -> {
                    MaidSkill Skill = MaidSkillManager.getSkillFor(RL);
                    if (Skill != null) {
                        if (Skill.canExecute(MaidPatch)) {
                            Skill.MaidDamage(event);
                        }
                    }
                });
            }
        }
        @SubscribeEvent
        public static void MaidDeath(MaidDeathEvent event) {
            MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            if (MaidPatch != null) {
                List<ResourceLocation> Skills = MaidPatch.getLearnedSkills();
                Skills.forEach(RL -> {
                    MaidSkill Skill = MaidSkillManager.getSkillFor(RL);
                    if (Skill != null) {
                        if (Skill.canExecute(MaidPatch)) {
                            Skill.MaidDeath(event);
                        }
                    }
                });
            }
        }
        @SubscribeEvent
        public static void MaidTick(MaidTickEvent event) {
            MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(event.getMaid(), MaidPatch.class);
            if (MaidPatch != null) {
                List<ResourceLocation> Skills = MaidPatch.getLearnedSkills();
                Skills.forEach(RL -> {
                    MaidSkill Skill = MaidSkillManager.getSkillFor(RL);
                    if (Skill != null) {
                        if (Skill.canExecute(MaidPatch)) {
                            Skill.MaidTick(event);
                        }
                    }
                });
            }
        }
        @SubscribeEvent
        public void MaidHurtTargetPost(MaidHurtTargetEvent.Post event) {
            MaidPatch<?> MaidPatch = event.getMaidPatch();
            if (MaidPatch != null) {
                List<ResourceLocation> Skills = MaidPatch.getLearnedSkills();
                Skills.forEach(RL -> {
                    MaidSkill Skill = MaidSkillManager.getSkillFor(RL);
                    if (Skill != null) {
                        if (Skill.canExecute(MaidPatch)) {
                            Skill.MaidHurtTargetPost(event);
                        }
                    }
                });
            }
        }
        @SubscribeEvent
        public void MaidHurtTargetPre(MaidHurtTargetEvent.Pre event) {
            MaidPatch<?> MaidPatch = event.getMaidPatch();
            if (MaidPatch != null) {
                List<ResourceLocation> Skills = MaidPatch.getLearnedSkills();
                Skills.forEach(RL -> {
                    MaidSkill Skill = MaidSkillManager.getSkillFor(RL);
                    if (Skill != null) {
                        if (Skill.canExecute(MaidPatch)) {
                            Skill.MaidHurtTargetPre(event);
                        }
                    }
                });
            }
        }
        @SubscribeEvent
        public static void MaidChangeItem(MaidChangeItemEvent event) {
            if (event.getMaidPatch() != null) {
                MaidPatch<?> MaidPatch = event.getMaidPatch();
                if (MaidPatch.getOriginal().level() instanceof ServerLevel) {
                    List<ResourceLocation> skills = new ArrayList<>(MaidPatch.getLearnedSkills());
                    List<ResourceLocation> toRemove = new ArrayList<>();
                    List<ResourceLocation> toAdd = new ArrayList<>();
                    Item item = MaidPatch.CurrentMain;
                    for (ResourceLocation rl : skills) {
                        MaidSkill skill = MaidSkillManager.getSkillFor(rl);
                        if (skill == null) continue;
                        if (skill.canExecute(MaidPatch)) {
                            skill.MaidChangeItemOnHand(event);
                        }
                        if (skill instanceof WeaponInnateSkill innate) {
                            innate.onRemove(event);
                            toRemove.add(innate.getRegistryName());
                        }
                    }
                    if (item != null) {
                        if (MaidSkillManager.hasSkillFor(item)) {
                            toAdd.add(MaidSkillManager.getSkillFor(item).getRegistryName());
                        }
                    }
                    for (ResourceLocation rl : toRemove) {
                        MaidPatch.removeLearnedSkill(rl);
                    }
                    for (ResourceLocation rl : toAdd) {
                        MaidPatch.addLearnedSkill(rl);
                    }
                }
            }
        }
        @SubscribeEvent
        public static void MaidSkillInit(MaidSkillInitEvent event) {
            if (event.getMaidPatch() != null) {
                MaidPatch<?> MaidPatch = event.getMaidPatch();
                if (MaidPatch.getOriginal().level() instanceof ServerLevel) {
                    List<ResourceLocation> skills = new ArrayList<>(MaidPatch.getLearnedSkills());
                    List<ResourceLocation> toAdd = new ArrayList<>();
                    for (ResourceLocation rl : skills) {
                        MaidSkill skill = MaidSkillManager.getSkillFor(rl);
                        if (skill == null) continue;
                        if (skill.canExecute(MaidPatch)) {
                            skill.onInit(event);
                        }
                    }
                    if (MaidPatch.CurrentMain != null) {
                        if (MaidSkillManager.hasSkillFor(MaidPatch.CurrentMain)) {
                            toAdd.add(MaidSkillManager.getSkillFor(MaidPatch.CurrentMain).getRegistryName());
                        }
                    }
                    for (ResourceLocation rl : toAdd) {
                        MaidPatch.addLearnedSkill(rl);
                    }
                }
            }
        }
        @SubscribeEvent
        public static void MaidKillTarget(MaidKilledEvent event) {
            MaidPatch<?> MaidPatch = event.getMaidPatch();
            if (MaidPatch != null) {
                List<ResourceLocation> Skills = MaidPatch.getLearnedSkills();
                Skills.forEach(RL -> {
                    MaidSkill Skill = MaidSkillManager.getSkillFor(RL);
                    if (Skill != null) {
                        if (Skill.canExecute(MaidPatch)) {
                            Skill.MaidKillTarget(event);
                        }
                    }
                });
            }
        }
        @SubscribeEvent
        public static void MaidCombatBehaviors(CombatBehaviorsEvent event) {
            EFNCompat.trySetWeaponMotions(event.getItemAttackMotions(), event.getItemStyleAttackMotions(), event.getItemArmatures());
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
        public static void AttributeModificationEvent(EntityAttributeModificationEvent event) {
            MaidPatch.initAttribute(event);
        }
        @SubscribeEvent
        public static void MaidSkillBuild(MaidSkillBuildEvent event) {
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"blade_clash"), BladeClash::new, BladeClash.createBuilder().setCreativeTab(EFTLM_Tab.SKILL.get()));
            event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"step"), Step::new, Step.createStepBuilder().setCreativeTab(EFTLM_Tab.SKILL.get()));
            EFNCompat.tryBuildSkills(event);
        }
        @SubscribeEvent
        public static void MaidSkillCreate(MaidSkillBuildEvent.SkillCreateEvent<?> event) {
            EFNCompat.tryCreateSkills(event);
        }
        @SubscribeEvent
        public static void CommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(EFTLM_Armatures::RegisterArmatures);
            event.enqueueWork(MaidSkillManager::MaidSkillBuild);
        }
    }
}

