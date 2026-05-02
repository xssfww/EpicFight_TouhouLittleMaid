package net.EFTLM.EF.Model;

import com.google.common.collect.Maps;
import net.EFTLM.EF.Model.Mesh.MaidMesh;
import net.EFTLM.EFTLM;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.asset.JsonAssetLoader;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.ClientEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
public class EFTLM_Meshes implements ResourceManagerReloadListener {
    public static Map<String, Meshes.MeshAccessor<MaidMesh>> MaidMeshes = Maps.newHashMap();
    public static final Meshes.MeshAccessor<MaidMesh> WineFox = Meshes.MeshAccessor.create(EFTLM.MODID, "entity/winefox", (jsonModelLoader) -> jsonModelLoader.loadSkinnedMesh(MaidMesh::new));
    @Override
    public void onResourceManagerReload(@NotNull ResourceManager RM) {
        ReLoad(RM);
    }
    public static void ReLoad(ResourceManager RM) {
        EFTLM_Meshes.Load(RM);
        LocalPlayer Player = ClientEngine.getInstance().minecraft.player;
        if (Player != null) {
            Player.sendSystemMessage(Component.translatable("message.eftlm.reload_mesh", MaidMeshes.keySet().size()));
        }
    }
    public static void Load(ResourceManager RM) {
        EFTLM_Meshes.clear();
        Map<ResourceLocation, Resource> MR = RM.listResources("animmodels/entity",
                rl -> rl.getNamespace().equals(EFTLM.MODID) && rl.getPath().endsWith(".json")
        );
        List<ResourceLocation> MRList = new ArrayList<>(MR.keySet());
        for (ResourceLocation MeshRL : MRList) {
            String path = MeshRL.getPath();
            String FileId = path.substring(path.lastIndexOf('/') + 1, path.length() - 5);
            Function<JsonAssetLoader, MaidMesh> factory = jsonAssetLoader -> jsonAssetLoader.loadSkinnedMesh(MaidMesh::new);
            Meshes.MeshAccessor<MaidMesh> Accessor = Meshes.MeshAccessor.create(MeshRL.getNamespace(), MeshRL.getPath(), factory);
            MaidMeshes.put(FileId, Accessor);
        }
    }
    public static void clear() {
        MaidMeshes.clear();
    }
    public static Meshes.MeshAccessor<MaidMesh> getMesh(String MeshID) {
        return MaidMeshes.get(MeshID);
    }
}
