package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.API.AbstractMaidEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
public class MaidKilledEvent extends AbstractMaidEvent<MaidPatch<?>> {
    private final LivingEntity killedEntity;
    private final DamageSource damagesource;
    public MaidKilledEvent(MaidPatch<?> maid,LivingEntity killedEntity, DamageSource damagesource) {
        super(maid);
        this.killedEntity = killedEntity;
        this.damagesource = damagesource;
    }
    public LivingEntity getKilledEntity() {
        return this.killedEntity;
    }
    public DamageSource getDamageSource() {
        return this.damagesource;
    }
}

