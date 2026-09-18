package net.EFTLM.Mixin;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import javax.annotation.Nullable;
import java.util.UUID;
@Mixin(AttributeInstance.class)
public abstract class AttributeInstanceMixin {
    @Shadow @Nullable public abstract AttributeModifier getModifier(UUID uuid);
    @Shadow public abstract void removeModifier(UUID identifier);
    @Inject(method = "addTransientModifier", at = @At("HEAD"))
    private void beforeAddTransientModifier(AttributeModifier modifier, CallbackInfo ci) {
        if (this.getModifier(modifier.getId()) != null) {
            this.removeModifier(modifier.getId());
        }
    }
}