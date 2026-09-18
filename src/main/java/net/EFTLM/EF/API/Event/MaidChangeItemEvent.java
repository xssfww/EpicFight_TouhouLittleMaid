package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.API.AbstractMaidEvent;
import net.EFTLM.EF.Capability.MaidPatch;
public class MaidChangeItemEvent extends AbstractMaidEvent {
    public MaidChangeItemEvent(MaidPatch<?> MaidPatch) {
        super(MaidPatch);
    }
}

