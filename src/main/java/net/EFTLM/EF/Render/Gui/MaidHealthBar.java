package net.EFTLM.EF.Render.Gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EFTLM;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import yesman.epicfight.client.gui.EntityUI;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import javax.annotation.Nullable;
@OnlyIn(Dist.CLIENT)
public class MaidHealthBar extends EntityUI {
    public static final MaidHealthBar Instance = new MaidHealthBar();
    public static final ResourceLocation StaminaBar = ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "textures/gui/bar.png");
    public MaidHealthBar() {
    }
    public boolean shouldDraw(LivingEntity entity, @Nullable LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch, float partialTicks) {
        if (entitypatch instanceof MaidPatch<?> MaidPatch) {
            return MaidPatch.getStamina() != MaidPatch.getMaxStamina();
        }
        return false;
    }
    public void draw(LivingEntity entity, @Nullable LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch, PoseStack poseStack, MultiBufferSource buffers, float partialTicks) {
        if (entitypatch instanceof MaidPatch<?> Maid) {
            Matrix4f modelViewMatrix = super.getModelViewMatrixAlignedToCamera(poseStack, entity, 0.0F, entity.getBbHeight() + 0.2F, 0.0F, true, partialTicks);
            Matrix4f staminaMatrix = (new Matrix4f(modelViewMatrix)).scale(0.82F, 1.0F, 1.0F);
            drawUIAsLevelModel(staminaMatrix, StaminaBar, buffers, -0.5F, -0.05F, 0.5F, 0.0F, 0.09375F, 0.0F, 0.921875F, 0.109375F);
            float staminaRatio = Mth.clamp(Maid.getStamina() / Maid.getMaxStamina(), 0.0F, 1.0F);
            int staminaTextureRatio = (int) (59.0F * staminaRatio);
            drawUIAsLevelModel(staminaMatrix, StaminaBar, buffers, -0.5F, -0.05F, -0.5F + staminaRatio, 0.0F, 0.09375F, 0.125F, (float) staminaTextureRatio / 64.0F, 0.234375F);
        }
    }
}
