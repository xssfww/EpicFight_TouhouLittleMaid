package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.API.AbstractMaidEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
public class MaidHurtTargetEvent extends AbstractMaidEvent<MaidPatch<?>> {
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
        return source;
    }
    public static class Post extends MaidHurtTargetEvent {
        public Post(MaidPatch<?> maid, LivingEntity target, DamageSource source) {
            super(maid, target, source);
        }
    }
    @Cancelable
    public static class Pre extends MaidHurtTargetEvent {
        public Pre(MaidPatch<?> maid, LivingEntity target, DamageSource source) {
            super(maid, target, source);
        }
    }
}
