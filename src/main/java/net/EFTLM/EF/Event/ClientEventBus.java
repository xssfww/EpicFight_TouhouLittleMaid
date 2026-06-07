package net.EFTLM.EF.Event;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.MaidContainerGuiEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.EFTLM.EF.Model.EFTLM_Meshes;
import net.EFTLM.EF.Network.Packet.Server.OpenMaidSkillScreenPacket;
import net.EFTLM.EF.Network.PacketSend;
import net.EFTLM.EF.Register.EFTLM_Menu;
import net.EFTLM.EF.Render.Gui.MaidHealthBar;
import net.EFTLM.EF.Render.Gui.MaidSkillMenuScreen;
import net.EFTLM.EF.Render.Gui.Widget.MaidSkillTabButton;
import net.EFTLM.EF.Render.PatchedLivingMaidRenderer;
import net.EFTLM.EFTLM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.client.gui.EntityUI;
public class ClientEventBus {
    @Mod.EventBusSubscriber(
            modid = EFTLM.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE,
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
        public static void RegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(new EFTLM_Meshes());
        }
        @SubscribeEvent
        public static void ClientSetup(FMLClientSetupEvent event) {
            EntityUI.ENTITY_UI_LIST.add(MaidHealthBar.Instance);
            EFTLM_Meshes.Load(Minecraft.getInstance().getResourceManager());
            MenuScreens.register(EFTLM_Menu.MaidSkillMenu.get(), MaidSkillMenuScreen::new);
        }
    }
}
