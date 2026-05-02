package net.EFTLM.EF.Animation;

import com.merlin204.avalon.epicfight.animations.AvalonMovementAnimation;
import net.EFTLM.EFTLM;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;
@Mod.EventBusSubscriber(modid = EFTLM.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EFTLM_Animations {
    public static AnimationManager.AnimationAccessor<StaticAnimation> Biped_Hug;
    public static AnimationManager.AnimationAccessor<StaticAnimation> Biped_Hug_Kneel;
    public static AnimationManager.AnimationAccessor<AvalonMovementAnimation> Biped_Hug_Walk;
    public static AnimationManager.AnimationAccessor<AvalonMovementAnimation> Biped_Hug_Run;
    public static AnimationManager.AnimationAccessor<AvalonMovementAnimation> Biped_Hug_Sneak;
    @SubscribeEvent
    public static void RegisterAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(EFTLM.MODID, EFTLM_Animations::BuildAnimation);
    }
    public static void BuildAnimation(AnimationManager.AnimationBuilder builder) {
        Armatures.ArmatureAccessor<HumanoidArmature> Biped = Armatures.BIPED;
        Biped_Hug = builder.nextAccessor("biped/living/hug", (accessor) ->
                new StaticAnimation(true, accessor, Biped)
        );
        Biped_Hug_Kneel = builder.nextAccessor("biped/living/hug_kneel", (accessor) ->
                new StaticAnimation(true, accessor, Biped)
        );
        Biped_Hug_Walk = builder.nextAccessor("biped/living/hug_walk", (accessor) ->
                new AvalonMovementAnimation(0.1F,true, accessor, Biped,1F)
        );
        Biped_Hug_Run = builder.nextAccessor("biped/living/hug_run", (accessor) ->
                new AvalonMovementAnimation(0.1F,true, accessor, Biped,1F)
        );
        Biped_Hug_Sneak =  builder.nextAccessor("biped/living/hug_sneak", (accessor) ->
                new AvalonMovementAnimation(0.1F,true, accessor, Biped,1F)
        );
    }
}
