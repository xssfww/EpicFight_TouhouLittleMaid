package net.EFTLM.EF.API.Event;

import net.EFTLM.EF.API.AbstractMaidEvent;
import net.EFTLM.EF.Capability.MaidPatch;
public class MaidSkillInitEvent extends AbstractMaidEvent<MaidPatch<?>> {
    public MaidSkillInitEvent(MaidPatch<?> MaidPatch) {
        super(MaidPatch);
    }
}
