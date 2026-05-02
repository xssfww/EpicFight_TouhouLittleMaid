package net.EFTLM.EF.Animation.CombatBehavior;

import net.EFTLM.EF.Capability.MaidPatch;
import net.minecraft.server.level.ServerLevel;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
public class BehaviorsBuild {
    public static boolean canUseSkill(LivingEntityPatch<?> Patch) {
        if (Patch instanceof MaidPatch<?> MaidPatch) {
            return MaidPatch.canUseSkill();
        }
        return false;
    }
    public static void setCoolDown(LivingEntityPatch<?> Patch, int tick) {
        if (Patch instanceof MaidPatch<?> MaidPatch) {
            if (MaidPatch.getOriginal().level() instanceof ServerLevel) {
                MaidPatch.setCoolDown(tick);
            }
        }
    }
}
