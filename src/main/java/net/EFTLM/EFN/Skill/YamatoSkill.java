package net.EFTLM.EFN.Skill;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.hm.efn.EFN;
import com.hm.efn.gameasset.EFNEnchantment;
import com.hm.efn.item.custom.YamatoItem;
import net.EFTLM.EF.API.Event.MaidHurtTargetEvent;
import net.EFTLM.EF.API.Event.MaidKilledEvent;
import net.EFTLM.EF.API.Event.MaidSkillInitEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Compat.EFNCompat;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillBuilder;
import net.EFTLM.EF.Skill.MaidSkillDataKeys;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
public class YamatoSkill extends WeaponInnateSkill {
    private static final String PARAM_REINFORCE_CHANCE = "reinforce_chance";
    private static final String PARAM_REQUIRE_HEAVY_RAIN_ENCHANTMENT = "require_heavy_rain_enchantment";
    private static final float DEFAULT_REINFORCE_CHANCE = 0.15F;
    private static final boolean DEFAULT_REQUIRE_HEAVY_RAIN_ENCHANTMENT = false;
    private static final int BLAST_COST = 1;
    private static final int BLAST_COOLDOWN = 20;
    private static final int BLAST_WEIGHT = 35;
    private static final int SWORD_RAIN_COST = 1;
    private static final int SWORD_RAIN_COOLDOWN = 40;
    private static final int SWORD_RAIN_WEIGHT = 25;
    private static final int HEAVY_RAIN_COST = 3;
    private static final int HEAVY_RAIN_COOLDOWN = 60;
    private static final int HEAVY_RAIN_WEIGHT = 25;
    private static final int FORMATION_COST = 5;
    private static final int FORMATION_WEIGHT = 15;
    private float reinforceChance = DEFAULT_REINFORCE_CHANCE;
    private boolean requireHeavyRainEnchantment = DEFAULT_REQUIRE_HEAVY_RAIN_ENCHANTMENT;
    public YamatoSkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    public void setParams(CompoundTag parameters) {
        super.setParams(parameters);
        if (parameters.contains(PARAM_REINFORCE_CHANCE)) {
            this.reinforceChance = Mth.clamp(parameters.getFloat(PARAM_REINFORCE_CHANCE), 0.0F, 1.0F);
        }
        if (parameters.contains(PARAM_REQUIRE_HEAVY_RAIN_ENCHANTMENT)) {
            this.requireHeavyRainEnchantment = parameters.getBoolean(PARAM_REQUIRE_HEAVY_RAIN_ENCHANTMENT);
        }
    }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        super.onInit(event);
        event.registerData(this, MaidSkillDataKeys.YAMATO_FORMATION_TIME);
        event.registerData(this, MaidSkillDataKeys.YAMATO_BLAST_COOLDOWN);
        event.registerData(this, MaidSkillDataKeys.YAMATO_SWORD_RAIN_COOLDOWN);
        event.registerData(this, MaidSkillDataKeys.YAMATO_HEAVY_RAIN_COOLDOWN);
    }
    @Override
    public void onMaidTick(MaidTickEvent event,MaidPatch<?> patch) {
        super.onMaidTick(event,patch);
        if (patch == null) return;
        tickCooldown(patch, MaidSkillDataKeys.YAMATO_FORMATION_TIME);
        tickCooldown(patch, MaidSkillDataKeys.YAMATO_BLAST_COOLDOWN);
        tickCooldown(patch, MaidSkillDataKeys.YAMATO_SWORD_RAIN_COOLDOWN);
        tickCooldown(patch, MaidSkillDataKeys.YAMATO_HEAVY_RAIN_COOLDOWN);
    }
    @Override
    public void onHurtTargetPost(MaidHurtTargetEvent.Post event) {
        super.onHurtTargetPost(event);
        MaidPatch<?> MaidPatch = event.getMaidPatch();
        if (MaidPatch == null) return;
        EntityMaid Maid = MaidPatch.getOriginal();
        if (!(Maid.level() instanceof ServerLevel)) return;
        ItemStack item = Maid.getMainHandItem();
        if (!(item.getItem() instanceof YamatoItem)) return;
        CompoundTag tag = item.getOrCreateTag();
        float totalDamage = YamatoItem.getTotalDamage(item);
        tag.putFloat("TotalDamage",totalDamage + event.getAmount());
        tryReinforceSword(MaidPatch, item, event.getTarget());
    }
    @Override
    public void onKillTarget(MaidKilledEvent event) {
        MaidPatch<?> MaidPatch = event.getMaidPatch();
        EntityMaid Maid = MaidPatch.getOriginal();
        ItemStack item = Maid.getMainHandItem();
        if (item.getItem() instanceof YamatoItem) {
            CompoundTag tag = item.getOrCreateTag();
            int currentCount = YamatoItem.getKillCount(item);
            tag.putInt("KillCount", currentCount + 1);
        }
    }
    private void tryReinforceSword(MaidPatch<?> patch, ItemStack item, LivingEntity target) {
        EntityMaid maid = patch.getOriginal();
        if (maid.getRandom().nextFloat() >= this.reinforceChance) return;
        Integer stackValue = patch.getDataValue(this, MaidSkillDataKeys.STACK);
        int stack = stackValue == null ? 0 : stackValue;
        if (stack <= 0) return;
        boolean hasTarget = target != null && target.isAlive();
        boolean canBlast = hasTarget && isCooldownReady(patch, MaidSkillDataKeys.YAMATO_BLAST_COOLDOWN);
        boolean canSwordRain = hasTarget && isCooldownReady(patch, MaidSkillDataKeys.YAMATO_SWORD_RAIN_COOLDOWN);
        boolean canHeavyRain = hasTarget && stack >= HEAVY_RAIN_COST && isCooldownReady(patch, MaidSkillDataKeys.YAMATO_HEAVY_RAIN_COOLDOWN)
                && (!this.requireHeavyRainEnchantment || item.getEnchantmentLevel(EFNEnchantment.YAMATO_HEAVY_RAIN.get()) > 0);
        boolean canFormation = stack >= FORMATION_COST && EFNCompat.canSummonAtWaist(patch);
        int totalWeight = (canBlast ? BLAST_WEIGHT : 0) + (canSwordRain ? SWORD_RAIN_WEIGHT : 0)
                + (canHeavyRain ? HEAVY_RAIN_WEIGHT : 0) + (canFormation ? FORMATION_WEIGHT : 0);
        if (totalWeight <= 0) return;
        int roll = maid.getRandom().nextInt(totalWeight);
        if (canBlast) {
            if (roll < BLAST_WEIGHT) {
                EFNCompat.summonBlastSword(patch);
                consumeStack(patch, stack, BLAST_COST);
                setCooldown(patch, MaidSkillDataKeys.YAMATO_BLAST_COOLDOWN, BLAST_COOLDOWN);
                return;
            }
            roll -= BLAST_WEIGHT;
        }
        if (canSwordRain) {
            if (roll < SWORD_RAIN_WEIGHT) {
                EFNCompat.summonSwordRain(patch);
                consumeStack(patch, stack, SWORD_RAIN_COST);
                setCooldown(patch, MaidSkillDataKeys.YAMATO_SWORD_RAIN_COOLDOWN, SWORD_RAIN_COOLDOWN);
                return;
            }
            roll -= SWORD_RAIN_WEIGHT;
        }
        if (canHeavyRain) {
            if (roll < HEAVY_RAIN_WEIGHT) {
                EFNCompat.summonHeavyRain(patch);
                consumeStack(patch, stack, HEAVY_RAIN_COST);
                setCooldown(patch, MaidSkillDataKeys.YAMATO_HEAVY_RAIN_COOLDOWN, HEAVY_RAIN_COOLDOWN);
                return;
            }
        }
        if (canFormation) {
            EFNCompat.summonAtWaist(patch);
            consumeStack(patch, stack, FORMATION_COST);
        }
    }
    private void consumeStack(MaidPatch<?> patch, int currentStack, int cost) {
        patch.setData(this, MaidSkillDataKeys.STACK, Math.max(currentStack - cost, 0));
    }
    private void setCooldown(MaidPatch<?> patch, MaidSkillDataManager.SkillDataKey<Integer> key, int ticks) {
        patch.setData(this, key, ticks);
    }
    private boolean isCooldownReady(MaidPatch<?> patch, MaidSkillDataManager.SkillDataKey<Integer> key) {
        Integer time = patch.getDataValue(this, key);
        return time == null || time <= 0;
    }
    private void tickCooldown(MaidPatch<?> patch, MaidSkillDataManager.SkillDataKey<Integer> key) {
        Integer time = patch.getDataValue(this, key);
        if (time == null) return;
        if (time > 0) {
            patch.setData(this, key, time - 1);
        }
    }
    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(EFN.MODID, "textures/item/yamato_dmc4.png");
    }
}
