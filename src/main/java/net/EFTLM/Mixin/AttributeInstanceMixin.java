package net.EFTLM.Mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import javax.annotation.Nullable;

@Mixin(AttributeInstance.class)
public abstract class AttributeInstanceMixin {
    // 1.21.1: AttributeModifier is a record identified by a ResourceLocation instead of a UUID.
    @Shadow @Nullable public abstract AttributeModifier getModifier(ResourceLocation id);
    @Shadow public abstract boolean removeModifier(ResourceLocation id);

    @Inject(method = "addTransientModifier", at = @At("HEAD"))
    private void beforeAddTransientModifier(AttributeModifier modifier, CallbackInfo ci) {
        if (this.getModifier(modifier.id()) != null) {
            this.removeModifier(modifier.id());
        }
    }
}
