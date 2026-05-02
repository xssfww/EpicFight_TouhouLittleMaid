package net.EFTLM.EF.Capability;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
public class ClientMaidPatch extends MaidPatch<EntityMaid>{
    @Override
    public boolean flashTargetIndicator(LocalPlayerPatch playerpatch) {
        return false;
    }
    @Override
    public boolean overrideRender() {
        if (this.isHugByOwner() || this.isSleep() || this.isSit()) {
            return false;
        }
        return this.isFightMode();
    }
    @Override
    public void updateMotion(boolean considerInaction) {
        if ((this.original).getHealth() <= 0.0F) {
            this.currentLivingMotion = LivingMotions.DEATH;
        } else if (this.state.inaction() && considerInaction) {
            this.currentLivingMotion = LivingMotions.INACTION;
        } else if ((this.original).getVehicle() != null) {
            this.currentLivingMotion = LivingMotions.MOUNT;
        } else if (!((this.original).getDeltaMovement().y < -0.550000011920929) && !this.isAirborneState()) {
            if ((this.original).walkAnimation.speed() > 0.2F) {
                if (this.original.isAggressive()) {
                    this.currentLivingMotion = LivingMotions.RUN;
                } else {
                    this.currentLivingMotion = LivingMotions.WALK;
                }
            } else {
                this.currentLivingMotion = LivingMotions.IDLE;
            }
        } else {
            this.currentLivingMotion = LivingMotions.FALL;
        }
        this.currentCompositeMotion = this.currentLivingMotion;
    }
}
