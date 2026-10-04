package net.EFTLM.EF.Network.Packet.Server;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.API.Event.MaidSkillRemoveEvent;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Network.Packet.Client.SyncMaidSkillsPacket;
import net.EFTLM.EF.Network.PacketSend;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ForgetMaidSkillPacket {
    private final int MaidId;
    private final ResourceLocation SkillName;
    public ForgetMaidSkillPacket(int MaidId, ResourceLocation SkillName) {
        this.MaidId = MaidId;
        this.SkillName = SkillName;
    }
    public static void encode(ForgetMaidSkillPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.MaidId);
        buf.writeResourceLocation(message.SkillName);
    }
    public static ForgetMaidSkillPacket decode(FriendlyByteBuf buf) {
        return new ForgetMaidSkillPacket(buf.readVarInt(), buf.readResourceLocation());
    }
    public static void handle(ForgetMaidSkillPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> Action(message, context.getSender()));
        context.setPacketHandled(true);
    }
    protected static void Action(ForgetMaidSkillPacket message, @Nullable ServerPlayer player) {
        if (player == null) return;
        Entity entity = player.level().getEntity(message.MaidId);
        if (!(entity instanceof EntityMaid maid)) return;
        if (!OpenMaidSkillScreenPacket.stillValid(player, maid)) return;
        MaidPatch<?> MaidPatch = EpicFightCapabilities.getEntityPatch(maid, MaidPatch.class);
        if (MaidPatch == null) return;
        MaidSkill Skill = MaidSkillManager.getSkillFor(message.SkillName);
        if (Skill == null || !MaidPatch.hasLearnedSkill(message.SkillName)) return;
        MaidSkillRemoveEvent event = new MaidSkillRemoveEvent(MaidPatch, Skill);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return;
        }
        PacketSend.sendToPlayer(new SyncMaidSkillsPacket(maid.getId(), MaidPatch.serializeNBT()), player);
        player.sendSystemMessage(Component.translatable("message.eftlm.forget_skill_success", Skill.getTitle().getString()));
    }
}
