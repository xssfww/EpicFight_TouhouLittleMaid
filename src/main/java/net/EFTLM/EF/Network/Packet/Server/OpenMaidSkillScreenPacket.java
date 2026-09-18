package net.EFTLM.EF.Network.Packet.Server;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Inventory.MaidSkillContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import javax.annotation.Nullable;
import java.util.function.Supplier;
public class OpenMaidSkillScreenPacket {
    private final int MaidId;
    public OpenMaidSkillScreenPacket(int MaidId) {
        this.MaidId = MaidId;
    }
    public static void encode(OpenMaidSkillScreenPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.MaidId);
    }
    public static OpenMaidSkillScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenMaidSkillScreenPacket(buf.readVarInt());
    }
    public static void handle(OpenMaidSkillScreenPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> Action(message, context.getSender()));
        context.setPacketHandled(true);
    }
    protected static void Action(OpenMaidSkillScreenPacket message, @Nullable ServerPlayer player) {
        if (player != null) {
            Entity entity = player.level().getEntity(message.MaidId);
            if (entity instanceof EntityMaid maid && stillValid(player, maid)) {
                MaidPatch<?> DataPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
                if (DataPatch != null) {
                    NetworkHooks.openScreen(player, MaidSkillContainer.create(maid.getId(),DataPatch.serializeNBT()), buf -> {
                        buf.writeVarInt(maid.getId());
                        buf.writeNbt(DataPatch.serializeNBT());
                    });
                }
            }
        }
    }
    protected static boolean stillValid(ServerPlayer player, EntityMaid maid) {
        return maid.isOwnedBy(player) && !maid.isSleeping() && maid.isAlive() && player.canReach(maid, 3);
    }
}