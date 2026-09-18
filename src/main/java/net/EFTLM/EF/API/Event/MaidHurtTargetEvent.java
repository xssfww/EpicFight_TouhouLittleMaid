package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.API.AbstractMaidEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
public class MaidHurtTargetEvent extends AbstractMaidEvent {
    private final LivingEntity target;
    private final DamageSource source;
    public MaidHurtTargetEvent(MaidPatch<?> MaidPatch, LivingEntity target, DamageSource source) {
        super(MaidPatch);
        this.target = target;
        this.source = source;
    }
    public LivingEntity getTarget() {
        return this.target;
    }
    public DamageSource getSource() {
        return this.source;
    }
    public static class Post extends MaidHurtTargetEvent {
        private final float amount;
        public Post(MaidPatch<?> MaidPatch, LivingEntity target, DamageSource source, float amount) {
            super(MaidPatch, target, source);
            this.amount = amount;
        }
        public float getAmount() {
            return amount;
        }
    }
    public static class Pre extends MaidHurtTargetEvent {
        public Pre(MaidPatch<?> MaidPatch, LivingEntity target, DamageSource source) {
            super(MaidPatch, target, source);
        }
    }
}
