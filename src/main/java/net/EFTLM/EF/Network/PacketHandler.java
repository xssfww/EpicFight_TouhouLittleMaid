package net.EFTLM.EF.Network;

import net.EFTLM.EF.Network.Packet.BasePacket;
import net.EFTLM.EF.Network.Packet.Server.OpenMaidSkillScreenPacket;
import net.EFTLM.EFTLM;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
public class PacketHandler {
    protected static final String PROTOCOL_VERSION = "1";
    protected static int index;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );
    public static synchronized void RegisterManager() {
        ServerRegister(OpenMaidSkillScreenPacket.class, OpenMaidSkillScreenPacket::encode, OpenMaidSkillScreenPacket::decode, OpenMaidSkillScreenPacket::handle);
    }
    protected static <MSG extends BasePacket> void ClientRegister(final Class<MSG> packet, Function<FriendlyByteBuf, MSG> decoder) {
        INSTANCE.messageBuilder(packet, index++).encoder(BasePacket::encode).decoder(decoder).consumerMainThread(BasePacket::handle).add();
    }
    protected static <MSG> void ServerRegister(final Class<MSG> packet, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> handle) {
        INSTANCE.messageBuilder(packet, index++).encoder(encoder).decoder(decoder).consumerMainThread(handle).add();
    }
}
