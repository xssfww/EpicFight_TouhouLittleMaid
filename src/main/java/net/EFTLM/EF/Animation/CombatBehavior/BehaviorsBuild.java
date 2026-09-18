package net.EFTLM.EF.Animation.CombatBehavior;

import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillDataManager;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
public class BehaviorsBuild {
    @SuppressWarnings("unchecked")
    public static <T extends WeaponInnateSkill> T getWeaponInnateSkill(LivingEntityPatch<?> patch, Class<T> skillClass) {
        if (patch instanceof MaidPatch<?> maid) {
            for (ResourceLocation rl : maid.getLearnedSkills()) {
                MaidSkill skill = MaidSkillManager.getSkillFor(rl);
                if (skillClass.isInstance(skill)) {
                    return (T) skill;
                }
            }
        }
        return null;
    }
    private static WeaponInnateSkill getWeaponInnateSkill(LivingEntityPatch<?> patch) {
        if (patch instanceof MaidPatch<?> maid) {
            for (ResourceLocation rl : maid.getLearnedSkills()) {
                MaidSkill skill = MaidSkillManager.getSkillFor(rl);
                if (skill instanceof WeaponInnateSkill weaponInnateSkill) {
                    return weaponInnateSkill;
                }
            }
        }
        return null;
    }
    public static <V> void setData(LivingEntityPatch<?> patch,MaidSkill skill, MaidSkillDataManager.SkillDataKey<V> key, V data) {
        if (patch instanceof MaidPatch<?> maid) {
            maid.setData(skill, key, data);
        }
    }
    public static <V> V getDataValue(LivingEntityPatch<?> patch,MaidSkill skill, MaidSkillDataManager.SkillDataKey<V> key) {
        if (patch instanceof MaidPatch<?> maid) {
           return maid.getDataValue(skill, key);
        }
        return null;
    }
    public static boolean hasStack(LivingEntityPatch<?> Patch,int amount) {
        int value = BehaviorsBuild.getStack(Patch);
        return value >= amount;
    }
    public static int getStack(LivingEntityPatch<?> patch) {
        WeaponInnateSkill skill = getWeaponInnateSkill(patch);
        if (skill == null) return 0;
        Integer stack = getDataValue(patch,skill,WeaponInnateSkill.STACK);
        return stack == null ? 0 : stack;
    }
    public static int getMaxStack(LivingEntityPatch<?> patch) {
        WeaponInnateSkill skill = getWeaponInnateSkill(patch);
        if (skill == null) return 0;
        return skill.getMaxStack();
    }
    public static void setStack(LivingEntityPatch<?> patch,int amount) {
        WeaponInnateSkill skill = getWeaponInnateSkill(patch);
        if (skill == null) return;
        if (patch instanceof MaidPatch<?> maid) {
            maid.setData(skill, WeaponInnateSkill.STACK, amount);
        }
    }
}
