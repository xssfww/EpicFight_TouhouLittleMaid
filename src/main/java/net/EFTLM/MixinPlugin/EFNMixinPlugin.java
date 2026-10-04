package net.EFTLM.MixinPlugin;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import java.util.List;
import java.util.Set;
public class EFNMixinPlugin implements IMixinConfigPlugin {
    private static final String EFN_MODID = "efn";
    @Override
    public void onLoad(String mixinPackage) {
    }
    @Override
    public String getRefMapperConfig() {
        return null;
    }
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return isEFNLoaded();
    }
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }
    @Override
    public List<String> getMixins() {
        return null;
    }
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
    private static boolean isEFNLoaded() {
        try {
            LoadingModList loading = LoadingModList.get();
            if (loading != null) {
                return loading.getModFileById(EFN_MODID) != null;
            }
            ModList modList = ModList.get();
            if (modList != null) {
                return modList.isLoaded(EFN_MODID);
            }
        } catch (Throwable ignored) {
        }
        return true;
    }
}
