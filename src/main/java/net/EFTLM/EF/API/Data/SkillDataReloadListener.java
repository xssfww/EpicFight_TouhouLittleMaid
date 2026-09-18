package net.EFTLM.EF.API.Data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import net.EFTLM.EF.Skill.MaidSkill;
import net.EFTLM.EF.Skill.MaidSkillManager;
import net.EFTLM.EF.Skill.WeaponInnate.WeaponInnateSkill;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
public class SkillDataReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();
    public static final String DIRECTORY = "maid_skill";
    public SkillDataReloadListener() {
        super(GSON, DIRECTORY);
    }
    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> objects, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        objects.entrySet().stream().filter((entry) -> MaidSkillManager.getSkillRegisterName().contains(entry.getKey())).map(SkillDataReloadListener::parseParameters).forEach((pair) -> {
            MaidSkill maidSkill = MaidSkillManager.getSkillFor(pair.getFirst());
            if (maidSkill instanceof WeaponInnateSkill innateSkill) {
                innateSkill.setParams(pair.getSecond());
            }
        });
    }
    private static Pair<ResourceLocation, CompoundTag> parseParameters(Map.Entry<ResourceLocation, JsonElement> entry) {
        try {
            CompoundTag tag = TagParser.parseTag(entry.getValue().toString());
            tag.putString("id", entry.getKey().toString());
            return Pair.of(entry.getKey(), tag);
        } catch (CommandSyntaxException exception) {
            return Pair.of(entry.getKey(), new CompoundTag());
        }
    }
}
