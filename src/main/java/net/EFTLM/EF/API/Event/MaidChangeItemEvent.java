package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.Capability.MaidPatch;
import net.minecraftforge.eventbus.api.Event;
public class MaidChangeItemEvent extends Event {
    private final MaidPatch<?> MaidPatch;
    public MaidChangeItemEvent(MaidPatch<?> MaidPatch) {
        this.MaidPatch = MaidPatch;
    }
    public MaidPatch<?> getMaidPatch() {
        return this.MaidPatch;
    }
}

