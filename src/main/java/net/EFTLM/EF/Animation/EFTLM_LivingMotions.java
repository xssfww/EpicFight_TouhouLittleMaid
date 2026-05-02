package net.EFTLM.EF.Animation;

import yesman.epicfight.api.animation.LivingMotion;
public enum EFTLM_LivingMotions implements LivingMotion {
    HUG,
    HUG_WALK,
    HUG_RUN,
    HUG_KNEEL,
    HUG_SNEAK;
    final int id;
    EFTLM_LivingMotions() {
        this.id = LivingMotion.ENUM_MANAGER.assign(this);
    }
    @Override
    public int universalOrdinal() {
        return this.id;
    }
}
