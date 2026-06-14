package net.EFTLM.EF.API;

import net.EFTLM.EF.Capability.MaidPatch;
import net.minecraftforge.eventbus.api.Event;
public class AbstractMaidEvent<T extends MaidPatch<?>> extends Event {
    private final T MaidPatch;
    public AbstractMaidEvent(T MaidPatch) {
        this.MaidPatch = MaidPatch;
    }
    public T getMaidPatch() {
        return this.MaidPatch;
    }
}
