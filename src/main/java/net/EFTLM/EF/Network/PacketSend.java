package net.EFTLM.EF.Network;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
public class PacketSend extends PacketHandler {
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
    public static <MSG> void sendToArea(MSG message, double x, double y, double z, double radius, ResourceKey<Level> dimension) {
        INSTANCE.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(x, y, z, radius, dimension)), message);
    }
    public static <MSG> void sendToAll(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
    public static <MSG> void sendToLevel(MSG message, ResourceKey<Level> dimension) {
        INSTANCE.send(PacketDistributor.DIMENSION.with(() -> dimension), message);
    }
}
