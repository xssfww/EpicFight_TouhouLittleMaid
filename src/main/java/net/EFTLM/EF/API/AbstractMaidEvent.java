package net.EFTLM.EF.API;

import net.EFTLM.EF.Capability.MaidPatch;
import net.minecraftforge.eventbus.api.Event;
public class AbstractMaidEvent extends Event {
    private final MaidPatch<?> MaidPatch;
    public AbstractMaidEvent(MaidPatch<?> MaidPatch) {
        this.MaidPatch = MaidPatch;
    }
    public MaidPatch<?> getMaidPatch() {
        return this.MaidPatch;
    }
}
