package net.EFTLM.EF.Event;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.MaidContainerGuiEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.EFTLM.EF.Model.EFTLM_Meshes;
import net.EFTLM.EF.Network.Packet.Server.OpenMaidSkillScreenPacket;
import net.EFTLM.EF.Network.PacketSend;
import net.EFTLM.EF.Register.EFTLM_Menu;
import net.EFTLM.EF.Render.MaidHealthBar;
import net.EFTLM.EF.Render.Gui.MaidSkillMenuScreen;
import net.EFTLM.EF.Render.Gui.Widget.MaidSkillTabButton;
import net.EFTLM.EF.Render.PatchedLivingMaidRenderer;
import net.EFTLM.EFTLM;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import yesman.epicfight.api.client.event.EpicFightClientEventHooks;
import yesman.epicfight.api.client.event.types.registry.RegisterPatchedRenderersEvent;
import yesman.epicfight.client.gui.EntityUI;

public class ClientEventBus {
    @EventBusSubscriber(
            modid = EFTLM.MODID,
            bus = EventBusSubscriber.Bus.GAME,
            value = {Dist.CLIENT}
    )
    public static class ForgeEvents {
        @SubscribeEvent
        public static void MaidGuiInit(MaidContainerGuiEvent.Init event) {
            String ID = ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"skill_tab").toString();
            EntityMaid maid = event.getGui().getMaid();
            if (event.getGui() instanceof MaidSkillMenuScreen) {
                return;
            }
            if (maid != null) {
                event.addButton(ID, new MaidSkillTabButton(event.getLeftPos(), event.getTopPos(), true, button -> PacketSend.sendToServer(new OpenMaidSkillScreenPacket(maid.getId()))));
            }
        }
    }

    @EventBusSubscriber(
            modid = EFTLM.MODID,
            bus = EventBusSubscriber.Bus.MOD,
            value = {Dist.CLIENT}
    )
    public static class ModEvents {
        // Epic Fight 21.x posts this through its own event registry instead of the mod bus.
        public static void RegisterPatchedRenderer(RegisterPatchedRenderersEvent.AddEntity event) {
            event.addPatchedEntityRenderer(InitEntities.MAID.get(), entityType -> new PatchedLivingMaidRenderer(event.getContext(), entityType));
        }
        @SubscribeEvent
        public static void RegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(new EFTLM_Meshes());
        }
        // 1.21.1: MenuScreens.register is private now, screens are registered through this event.
        @SubscribeEvent
        public static void RegisterMenuScreens(RegisterMenuScreensEvent event) {
            event.register(EFTLM_Menu.MaidSkillMenu.get(), MaidSkillMenuScreen::new);
        }
        @SubscribeEvent
        public static void ClientSetup(FMLClientSetupEvent event) {
            EpicFightClientEventHooks.Registry.ADD_PATCHED_ENTITY.registerEvent(ModEvents::RegisterPatchedRenderer);
            EntityUI.ENTITY_UI_LIST.add(MaidHealthBar.Instance);
            EFTLM_Meshes.Load(Minecraft.getInstance().getResourceManager());
        }
    }
}
