package net.EFTLM.EF.Skill.WeaponInnate.EFN;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.hm.efn.item.custom.BroadBladeItem;
import com.hm.efn.registries.EFNMobEffectRegistry;
import net.EFTLM.EF.API.Event.MaidChangeItemEvent;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
public class BroadBladeSkill extends WeaponInnateSkill {
    public BroadBladeSkill(MaidSkillBuilder builder) {
        super(builder);
    }
    @Override
    public void MaidKillTarget(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof EntityMaid Maid) {
            if (event.getSource().getEntity().level() instanceof ServerLevel) {
                Maid.addEffect(new MobEffectInstance(EFNMobEffectRegistry.GRADUAL_HEAL.get(), 40, 4));
                Maid.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
                ItemStack mainHand = Maid.getMainHandItem();
                if (mainHand.getItem() instanceof BroadBladeItem) {
                    CompoundTag tag = mainHand.getOrCreateTag();
                    int currentCount = BroadBladeItem.getKillCount(mainHand);
                    tag.putInt("KillCount", currentCount + 1);
                }
            }
        }
    }
    @Override
    public void onRemove(MaidChangeItemEvent event) {
        super.onRemove(event);
    }
}
