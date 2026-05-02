package net.EFTLM.EF.Model;

import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.Maps;
import net.EFTLM.EFTLM;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class EFTLM_Armatures {
    public static Map<String, Armatures.ArmatureAccessor<HumanoidArmature>> MaidArmatures = Maps.newHashMap();
    public static Armatures.ArmatureAccessor<HumanoidArmature> BroadBlade = Armatures.ArmatureAccessor.create(EFTLM.MODID, "armature/broadblade", HumanoidArmature::new);
    public static Armatures.ArmatureAccessor<HumanoidArmature> Kusabimaru = Armatures.ArmatureAccessor.create(EFTLM.MODID, "armature/kusabimaru", HumanoidArmature::new);
    public static Armatures.ArmatureAccessor<HumanoidArmature> Claw = Armatures.ArmatureAccessor.create(EFTLM.MODID, "armature/claw", HumanoidArmature::new);
    public static Armatures.ArmatureAccessor<HumanoidArmature> Common = Armatures.ArmatureAccessor.create(EFTLM.MODID, "armature/common", HumanoidArmature::new);
    public static void RegisterArmatures(){
        Armatures.registerEntityTypeArmature(InitEntities.MAID.get(), Common);
    }
    public static void Load(ResourceManager RM) {
        EFTLM_Meshes.clear();
        Map<ResourceLocation, Resource> AR = RM.listResources("animmodels/entity",
                rl -> rl.getNamespace().equals(EFTLM.MODID) && rl.getPath().endsWith(".json")
        );
        List<ResourceLocation> ARList = new ArrayList<>(AR.keySet());
        for (ResourceLocation ArmatureRL : ARList) {
            String path = ArmatureRL.getPath();
            String FileId = path.substring(path.lastIndexOf('/') + 1, path.length() - 5);
            Armatures.ArmatureAccessor<HumanoidArmature> Accessor = Armatures.ArmatureAccessor.create(ArmatureRL.getNamespace(), ArmatureRL.getPath(), HumanoidArmature::new);
            MaidArmatures.put(FileId, Accessor);
        }
    }
}
