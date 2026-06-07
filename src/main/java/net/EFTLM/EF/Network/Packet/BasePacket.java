package net.EFTLM.EF.Network.Packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
public interface BasePacket {
    void encode(FriendlyByteBuf var1);
    default boolean handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(this::execute);
        return true;
    }
    void execute();
}