package net.EFTLM.EF.Network.Packet.Server;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Inventory.MaidSkillContainer;
import net.EFTLM.EF.Network.Packet.BasePacket;
import net.EFTLM.EFTLM;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

public class OpenMaidSkillScreenPacket implements BasePacket {
    public static final CustomPacketPayload.Type<OpenMaidSkillScreenPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "open_maid_skill_screen"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenMaidSkillScreenPacket> STREAM_CODEC =
            StreamCodec.of((buf, message) -> message.encode(buf), OpenMaidSkillScreenPacket::decode);

    private final int MaidId;

    public OpenMaidSkillScreenPacket(int MaidId) {
        this.MaidId = MaidId;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.MaidId);
    }

    public static OpenMaidSkillScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenMaidSkillScreenPacket(buf.readVarInt());
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenMaidSkillScreenPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                Action(message, player);
            }
        });
    }

    protected static void Action(OpenMaidSkillScreenPacket message, ServerPlayer player) {
        if (player != null) {
            Entity entity = player.level().getEntity(message.MaidId);
            if (entity instanceof EntityMaid maid && stillValid(player, maid)) {
                MaidPatch<?> DataPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
                if (DataPatch != null) {
                    player.openMenu(MaidSkillContainer.create(maid.getId(), DataPatch.serializeNBT()), buf -> {
                        buf.writeVarInt(maid.getId());
                        buf.writeNbt(DataPatch.serializeNBT());
                    });
                }
            }
        }
    }

    protected static boolean stillValid(ServerPlayer player, EntityMaid maid) {
        return maid.isOwnedBy(player) && !maid.isSleeping() && maid.isAlive()
                && player.distanceToSqr(maid) <= 9.0D && player.hasLineOfSight(maid);
    }
}
