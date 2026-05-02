package net.EFTLM.EF.Render;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.EFTLM.EF.Capability.MaidPatch;
import net.EFTLM.EF.Model.EFTLM_Meshes;
import net.EFTLM.EF.Model.Mesh.MaidMesh;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;
public class PatchedLivingMaidRenderer extends PatchedMaidRenderer<EntityMaid,MaidPatch<EntityMaid>, BedrockModel<EntityMaid>, LivingEntityRenderer<EntityMaid,BedrockModel<EntityMaid>>,MaidMesh> {
    public PatchedLivingMaidRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType, EFTLM_Meshes.WineFox);
        this.addCustomLayer(new PatchedItemInHandLayer<>());
    }
    @Override
    public AssetAccessor<MaidMesh> getMeshProvider(MaidPatch<EntityMaid> MaidPatch) {
        Meshes.MeshAccessor<MaidMesh> Mesh = EFTLM_Meshes.getMesh(MaidPatch.getModelID());
        return Mesh != null ? Mesh : this.getDefaultMesh();
    }
}
