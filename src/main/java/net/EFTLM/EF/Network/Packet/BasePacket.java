package net.EFTLM.EF.Network.Packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface BasePacket extends CustomPacketPayload {
    void encode(FriendlyByteBuf buf);
}
