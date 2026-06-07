package net.EFTLM.TLM.Task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.EFTLM.EFTLM;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.item.EpicFightItems;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
public class FightModeTask implements IAttackTask {
    private static final ResourceLocation UUID = ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "fight_mode_task");
    private static final ItemStack ICON = EpicFightItems.SKILLBOOK.get().getDefaultInstance();
    @Override
    public @NotNull ResourceLocation getUid() {
        return UUID;
    }
    @Override
    public @NotNull ItemStack getIcon() {
        return ICON;
    }
    @Override
    @Nullable
    public SoundEvent getAmbientSound(@NotNull EntityMaid maid) {
        return SoundUtil.attackSound(maid, InitSounds.MAID_ATTACK.get(), 0.5F);
    }
    @Override
    public @NotNull List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(@NotNull EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(this::hasCapWeapon, this::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create((target) -> !this.hasCapWeapon(maid) || this.farAway(target, maid));
        BehaviorControl<Mob> moveToTargetTask = SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(0.6F);
        return Lists.newArrayList(Pair.of(5, supplementedTask), Pair.of(5, findTargetTask), Pair.of(5, moveToTargetTask));
    }
    public Optional<? extends LivingEntity> findFirstValidAttackTarget(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).flatMap((mobs) -> mobs.findClosest((e) ->
                maid.canAttack(e) && maid.isWithinRestriction(e.blockPosition()) && this.checkAttack(maid,e)));
    }
    @Override
    public @NotNull List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(@NotNull EntityMaid maid) {
        return Lists.newArrayList(Pair.of("assault_weapon", this::hasCapWeapon));
    }
    public boolean checkAttack(EntityMaid maid, @NotNull LivingEntity target) {
        LivingEntity LastAttacker = maid.getLastAttacker();
        if (maid.getOwner() != null) {
            LivingEntity LastHurtByOwner = maid.getOwner().getLastHurtMob();
            LivingEntity LastAttackerByOwner = maid.getOwner().getLastAttacker();
            if (target instanceof Player) {
                return this.checkTarget(target, LastAttacker, LastHurtByOwner, LastAttackerByOwner);
            }
            if (target instanceof EntityMaid) {
                return this.checkTarget(target, LastAttacker, LastHurtByOwner, LastAttackerByOwner);
            }
            if (target.getType().getCategory().isFriendly()) {
                return this.checkTarget(target, LastAttacker, LastHurtByOwner, LastAttackerByOwner);
            }
            return !maid.isAlliedTo(target);
        }
        return !maid.isAlliedTo(target);
    }
    public boolean checkTarget(LivingEntity target,LivingEntity LastAttacker,LivingEntity LastHurtByOwner,LivingEntity LastAttackerByOwner) {
        if (LastAttacker != null) {
            return target.equals(LastAttacker);
        } else if (LastHurtByOwner != null) {
            return target.equals(LastHurtByOwner);
        } else if (LastAttackerByOwner != null) {
            return target.equals(LastAttackerByOwner);
        }
        return false;
    }
    public boolean isWeaponCap(ItemStack stack) {
        return EpicFightCapabilities.getItemCapability(stack).isPresent();
    }
    public boolean hasCapWeapon(EntityMaid maid) {
        return this.isWeaponCap(maid.getMainHandItem());
    }
    public boolean farAway(LivingEntity target, EntityMaid maid) {
        if (!target.isAlive()) {
            return true;
        } else {
            boolean enable = maid.isHomeModeEnable();
            float radius = maid.getRestrictRadius();
            if (!enable && maid.getOwner() != null) {
                return maid.getOwner().distanceTo(target) > radius;
            } else {
                return maid.distanceTo(target) > radius;
            }
        }
    }
}
