package net.EFTLM.TLM;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import net.EFTLM.TLM.Task.FightModeTask;
@LittleMaidExtension
public class EFTLM_Compat implements ILittleMaid {
    public EFTLM_Compat() {
    }
    @Override
    public void addMaidTask(TaskManager manager) {
        manager.add(new FightModeTask());
    }
}
