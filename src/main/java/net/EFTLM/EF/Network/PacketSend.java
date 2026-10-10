package net.EFTLM.EF.Network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class PacketSend {
    public static void sendToPlayer(CustomPacketPayload message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static void sendToArea(CustomPacketPayload message, double x, double y, double z, double radius, ResourceKey<Level> dimension) {
        ServerLevel level = resolveLevel(dimension);
        if (level == null) {
            return;
        }
        PacketDistributor.sendToPlayersNear(level, null, x, y, z, radius, message);
    }

    public static void sendToAll(CustomPacketPayload message) {
        PacketDistributor.sendToAllPlayers(message);
    }

    public static void sendToServer(CustomPacketPayload message) {
        PacketDistributor.sendToServer(message);
    }

    public static void sendToLevel(CustomPacketPayload message, ResourceKey<Level> dimension) {
        ServerLevel level = resolveLevel(dimension);
        if (level == null) {
            return;
        }
        PacketDistributor.sendToPlayersInDimension(level, message);
    }

    private static ServerLevel resolveLevel(ResourceKey<Level> dimension) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server == null ? null : server.getLevel(dimension);
    }
}
