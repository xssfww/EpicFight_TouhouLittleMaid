package net.EFTLM.EF.Network.Packet.Server;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.API.Event.MaidSkillRemoveEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Network.Packet.BasePacket;
import net.EFTLM.EF.Network.Packet.Client.SyncMaidSkillsPacket;
import net.EFTLM.EF.Network.PacketSend;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.EFTLM.EFTLM;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

public class ForgetMaidSkillPacket implements BasePacket {
    public static final CustomPacketPayload.Type<ForgetMaidSkillPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "forget_maid_skill"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ForgetMaidSkillPacket> STREAM_CODEC =
            StreamCodec.of((buf, message) -> message.encode(buf), ForgetMaidSkillPacket::decode);

    private final int MaidId;
    private final ResourceLocation SkillName;

    public ForgetMaidSkillPacket(int MaidId, ResourceLocation SkillName) {
        this.MaidId = MaidId;
        this.SkillName = SkillName;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.MaidId);
        buf.writeResourceLocation(this.SkillName);
    }

    public static ForgetMaidSkillPacket decode(FriendlyByteBuf buf) {
        return new ForgetMaidSkillPacket(buf.readVarInt(), buf.readResourceLocation());
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ForgetMaidSkillPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                Action(message, player);
            }
        });
    }

    protected static void Action(ForgetMaidSkillPacket message, ServerPlayer player) {
        if (player == null) return;
        Entity entity = player.level().getEntity(message.MaidId);
        if (!(entity instanceof EntityMaid maid)) return;
        if (!OpenMaidSkillScreenPacket.stillValid(player, maid)) return;
        MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        if (MaidPatch == null) return;
        MaidSkill Skill = MaidSkillManager.getSkillFor(message.SkillName);
        if (Skill == null || !MaidPatch.hasLearnedSkill(message.SkillName)) return;
        MaidSkillRemoveEvent event = new MaidSkillRemoveEvent(MaidPatch, Skill);
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return;
        }
        PacketSend.sendToPlayer(new SyncMaidSkillsPacket(maid.getId(), MaidPatch.serializeNBT()), player);
        player.sendSystemMessage(Component.translatable("message.eftlm.forget_skill_success", Skill.getTitle().getString()));
    }
}
