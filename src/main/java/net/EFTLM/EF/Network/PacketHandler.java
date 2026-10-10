package net.EFTLM.EF.Network;

import net.EFTLM.EF.Network.Packet.Client.SyncMaidSkillsPacket;
import net.EFTLM.EF.Network.Packet.Server.ForgetMaidSkillPacket;
import net.EFTLM.EF.Network.Packet.Server.OpenMaidSkillScreenPacket;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * NeoForge 1.21.1 payload based networking (replaces the old SimpleChannel based handler).
 */
public class PacketHandler {
    public static final String PROTOCOL_VERSION = "1";

    public static void RegisterManager(IEventBus modBus) {
        modBus.addListener(PacketHandler::register);
    }

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(OpenMaidSkillScreenPacket.TYPE, OpenMaidSkillScreenPacket.STREAM_CODEC,
                OpenMaidSkillScreenPacket::handle);
        registrar.playToServer(ForgetMaidSkillPacket.TYPE, ForgetMaidSkillPacket.STREAM_CODEC,
                ForgetMaidSkillPacket::handle);
        registrar.playToClient(SyncMaidSkillsPacket.TYPE, SyncMaidSkillsPacket.STREAM_CODEC,
                SyncMaidSkillsPacket::handle);
    }
}
