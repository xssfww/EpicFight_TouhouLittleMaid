package net.EFTLM.EF.Event;

import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.EFTLM.EF.Model.EFTLM_Meshes;
import net.EFTLM.EF.Render.PatchedLivingMaidRenderer;
import net.EFTLM.EFTLM;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.client.ClientEngine;
public class ClientEventBus {
    @Mod.EventBusSubscriber(
            modid = EFTLM.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE,
            value = {Dist.CLIENT}
    )
    public static class ForgeEvents {
    }
    @Mod.EventBusSubscriber(
            modid = EFTLM.MODID,
            bus = Mod.EventBusSubscriber.Bus.MOD,
            value = {Dist.CLIENT}
    )
    public static class ModEvents {
        @SubscribeEvent
        public static void RegisterPatchedRenderer(PatchedRenderersEvent.Add event) {
            event.addPatchedEntityRenderer(InitEntities.MAID.get(), entityType -> new PatchedLivingMaidRenderer(event.getContext(), entityType));
        }
        @SubscribeEvent
        public static void RegisterMesh(FMLClientSetupEvent event) {
            Minecraft Client = ClientEngine.getInstance().minecraft;
            EFTLM_Meshes.Load(Client.getResourceManager());
        }
        @SubscribeEvent
        public static void RegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(new EFTLM_Meshes());
        }
    }
}
