package net.EFTLM.EF.Network.Packet.Client;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Network.Packet.BasePacket;
import net.EFTLM.EF.Render.Gui.MaidSkillMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

public class SyncMaidSkillsPacket implements BasePacket {
    private final int MaidId;
    private final CompoundTag PatchNbt;
    public SyncMaidSkillsPacket(int MaidId, CompoundTag PatchNbt) {
        this.MaidId = MaidId;
        this.PatchNbt = PatchNbt;
    }
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.MaidId);
        buf.writeNbt(this.PatchNbt);
    }
    public static SyncMaidSkillsPacket decode(FriendlyByteBuf buf) {
        return new SyncMaidSkillsPacket(buf.readVarInt(), buf.readNbt());
    }
    @Override
    public void execute() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientAction(this));
    }
    protected static void ClientAction(SyncMaidSkillsPacket message) {
        if (message.PatchNbt == null) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;
        Entity entity = minecraft.level.getEntity(message.MaidId);
        if (!(entity instanceof EntityMaid maid)) return;
        MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        if (MaidPatch != null) {
            MaidPatch.deserializeNBT(message.PatchNbt);
        }
        if (minecraft.screen instanceof MaidSkillMenuScreen menu) {
            menu.refreshSkillList(message.PatchNbt);
        }
    }
}
