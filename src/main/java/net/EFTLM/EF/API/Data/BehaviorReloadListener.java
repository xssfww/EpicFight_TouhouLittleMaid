package net.EFTLM.EF.API.Data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.data.conditions.Condition;
import yesman.epicfight.data.conditions.EpicFightConditions;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
public class BehaviorReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();
    public static final String DIRECTORY = "maid_behavior";
    public static final Map<Item, CombatBehaviors.Builder<HumanoidMobPatch<?>>> ITEM_ATTACK_MOTIONS = Maps.newHashMap();
    public static final Map<Item, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> ITEM_STYLE_MOTIONS = Maps.newHashMap();
    public static final Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> WEAPON_STYLE_MOTIONS = Maps.newHashMap();
    public static final Map<Item, HumanoidArmature> ITEM_ARMATURES = Maps.newHashMap();
    public BehaviorReloadListener() {
        super(GSON, DIRECTORY);
    }
    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> objects, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        ITEM_ATTACK_MOTIONS.clear();
        ITEM_STYLE_MOTIONS.clear();
        WEAPON_STYLE_MOTIONS.clear();
        ITEM_ARMATURES.clear();
        List<String> errors = new ArrayList<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objects.entrySet()) {
            try {
                CompoundTag tag = TagParser.parseTag(entry.getValue().toString());
                processData(tag);
            } catch (Exception e) {
                Component errorMsg = Component.translatable("message.eftlm.reload_data_error", entry.getKey(), e.getMessage());
                errors.add("§c" + errorMsg.getString());
                EpicFightMod.LOGGER.error(errorMsg);
            }
        }
        if (!errors.isEmpty()) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                Component header = Component.translatable("message.eftlm.reload_data_error.header");
                List<MutableComponent> messages = errors.stream()
                        .map(Component::literal)
                        .toList();
                server.getPlayerList().getPlayers().stream()
                        .filter(player -> player.hasPermissions(2))
                        .forEach(player -> {
                            player.sendSystemMessage(header);
                            messages.forEach(player::sendSystemMessage);
                        });
            }
        }
    }
    private void processData(CompoundTag tag) throws Exception {
        if (!tag.contains("combat_behaviors", Tag.TAG_LIST)) return;
        CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = deserializeCombatBehaviorsBuilder(tag.getList("combat_behaviors", Tag.TAG_COMPOUND));
        HumanoidArmature armature = null;
        if (tag.contains("armature")) {
            ResourceLocation armId = ResourceLocation.parse(tag.getString("armature"));
            armature = (HumanoidArmature) Armatures.get(armId);
        }
        Style style = tag.contains("style") ? Style.ENUM_MANAGER.getOrThrow(tag.getString("style")) : null;
        List<Item> items = Lists.newArrayList();
        if (tag.contains("items", Tag.TAG_LIST)) {
            ListTag itemsTag = tag.getList("items", Tag.TAG_STRING);
            for (int i = 0; i < itemsTag.size(); i++) {
                Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(itemsTag.getString(i)));
                if (item != null) items.add(item);
            }
        }
        List<WeaponCategory> categories = Lists.newArrayList();
        if (tag.contains("weapon_categories", Tag.TAG_LIST)) {
            ListTag catTag = tag.getList("weapon_categories", Tag.TAG_STRING);
            for (int i = 0; i < catTag.size(); i++) {
                categories.add(WeaponCategory.ENUM_MANAGER.getOrThrow(catTag.getString(i)));
            }
        }
        for (Item item : items) {
            if (style != null) {
                ITEM_STYLE_MOTIONS.computeIfAbsent(item, k -> Maps.newHashMap()).put(style, builder);
            } else {
                ITEM_ATTACK_MOTIONS.put(item, builder);
            }
            if (armature != null) {
                ITEM_ARMATURES.put(item, armature);
            }
        }
        if (!categories.isEmpty() && style != null) {
            for (WeaponCategory cat : categories) {
                WEAPON_STYLE_MOTIONS.computeIfAbsent(cat, k -> Maps.newHashMap()).put(style, builder);
            }
        }
        if (!categories.isEmpty() && style == null) {
            throw new Exception(Component.translatable("message.eftlm.reload_data_error.1").getString());
        }
        if (style != null && items.isEmpty() && categories.isEmpty()) {
            throw new Exception(Component.translatable("message.eftlm.reload_data_error.2").getString());
        }
        if (items.isEmpty() && categories.isEmpty()) {
            throw new Exception(Component.translatable("message.eftlm.reload_data_error.3").getString());
        }
    }
    private static CombatBehaviors.Builder<HumanoidMobPatch<?>> deserializeCombatBehaviorsBuilder(ListTag seriesList) throws Exception {
        CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = CombatBehaviors.builder();
        for (int i = 0; i < seriesList.size(); i++) {
            CompoundTag seriesTag = seriesList.getCompound(i);
            float weight = (float) seriesTag.getDouble("weight");
            int cooldown = seriesTag.contains("cooldown") ? seriesTag.getInt("cooldown") : 0;
            boolean interruptible = seriesTag.contains("canBeInterrupted") && seriesTag.getBoolean("canBeInterrupted");
            boolean looping = seriesTag.contains("looping") && seriesTag.getBoolean("looping");
            CombatBehaviors.BehaviorSeries.Builder<HumanoidMobPatch<?>> seriesBuilder = CombatBehaviors.BehaviorSeries.builder();
            seriesBuilder.weight(weight).cooldown(cooldown).canBeInterrupted(interruptible).looping(looping);
            ListTag behaviors = seriesTag.getList("behaviors", Tag.TAG_COMPOUND);
            for (int j = 0; j < behaviors.size(); j++) {
                CompoundTag behaviorTag = behaviors.getCompound(j);
                String animName = behaviorTag.getString("animation");
                AnimationManager.AnimationAccessor<? extends StaticAnimation> anim = AnimationManager.byKey(animName);
                if (anim == null) throw new Exception("Unknown animation: " + animName);
                CombatBehaviors.Behavior.Builder<HumanoidMobPatch<?>> behaviorBuilder = CombatBehaviors.Behavior.builder();
                behaviorBuilder.animationBehavior(anim);
                if (behaviorTag.contains("conditions", Tag.TAG_LIST)) {
                    ListTag condList = behaviorTag.getList("conditions", Tag.TAG_COMPOUND);
                    for (int k = 0; k < condList.size(); k++) {
                        CompoundTag condTag = condList.getCompound(k);
                        Condition<HumanoidMobPatch<?>> condition = deserializeCondition(condTag.getString("predicate"), condTag);
                        behaviorBuilder.predicate(condition);
                    }
                }
                seriesBuilder.nextBehavior(behaviorBuilder);
            }
            builder.newBehaviorSeries(seriesBuilder);
        }
        return builder;
    }
    @SuppressWarnings("unchecked")
    private static Condition<HumanoidMobPatch<?>> deserializeCondition(String type, CompoundTag args) throws Exception {
        ResourceLocation rl = type.contains(":") ? ResourceLocation.parse(type) : EpicFightMod.identifier(type);
        Supplier<Condition<?>> provider = EpicFightConditions.getConditionOrNull(rl);
        if (provider == null) throw new Exception("Unknown condition predicate: " + type);
        Condition<HumanoidMobPatch<?>> cond = (Condition<HumanoidMobPatch<?>>) provider.get();
        cond.read(args);
        return cond;
    }
}