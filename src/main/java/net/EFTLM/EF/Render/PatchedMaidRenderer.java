package net.EFTLM.EF.Render;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Model.Mesh.MaidMesh;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;
@OnlyIn(Dist.CLIENT)
public abstract class PatchedMaidRenderer <E extends EntityMaid, T extends MaidPatch<E>, M extends BedrockModel<E>, R extends LivingEntityRenderer<E,M>, AM extends MaidMesh> extends PatchedLivingEntityRenderer<E, T, M, R, AM> {
    protected final AssetAccessor<AM> Mesh;
    public PatchedMaidRenderer(EntityRendererProvider.Context context, EntityType<?> entityType, AssetAccessor<AM> AM) {
        super(context, entityType);
        Mesh = AM;
    }
    @Override
    public void render(E Maid, T MaidPatch, R renderer, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        if (Maid == null || MaidPatch == null) return;
        super.render(Maid, MaidPatch, renderer, buffer, poseStack, packedLight, partialTicks);
    }
    @Override
    public AssetAccessor<AM> getDefaultMesh() {
        return Mesh;
    }
}