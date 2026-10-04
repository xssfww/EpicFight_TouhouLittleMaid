package net.EFTLM.EF.Skill;

import net.EFTLM.EFTLM;
import net.minecraft.resources.ResourceLocation;

public class MaidSkillDataKeys {
    public static final MaidSkillDataManager.ValueType<Integer> INTEGER = MaidSkillDataManager.ValueType.integer();
    public static final MaidSkillDataManager.ValueType<Float> FLOAT = MaidSkillDataManager.ValueType.floatType();
    public static final MaidSkillDataManager.ValueType<Boolean> BOOLEAN = MaidSkillDataManager.ValueType.booleanType();
    public static final MaidSkillDataManager.SkillDataKey<Integer> STACK = create("stack", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Float> ENERGY = create("energy", FLOAT);
    public static final MaidSkillDataManager.SkillDataKey<Integer> CLAW_TIME = create("claw_time", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Boolean> MURASAMA_ZANSETSU = create("murasama_zansetsu", BOOLEAN);
    public static final MaidSkillDataManager.SkillDataKey<Integer> MURASAMA_ZANSETSU_DELAY = create("murasama_zansetsu_delay", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Integer> MURASAMA_ZANSETSU_INDEX = create("murasama_zansetsu_index", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Boolean> HF_BLADE_ZANSETSU = create("hf_blade_zansetsu", BOOLEAN);
    public static final MaidSkillDataManager.SkillDataKey<Integer> HF_BLADE_ZANSETSU_DELAY = create("hf_blade_zansetsu_delay", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Integer> HF_BLADE_ZANSETSU_INDEX = create("hf_blade_zansetsu_index", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Integer> MEEN_CHARGING_TIME = create("meen_charging_time", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Integer> YAMATO_FORMATION_TIME = create("yamato_formation_time", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Integer> YAMATO_BLAST_COOLDOWN = create("yamato_blast_cooldown", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Integer> YAMATO_SWORD_RAIN_COOLDOWN = create("yamato_sword_rain_cooldown", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Integer> YAMATO_HEAVY_RAIN_COOLDOWN = create("yamato_heavy_rain_cooldown", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Float> BLADE_CLASH_PENALTY = create("blade_clash_penalty", FLOAT);
    public static final MaidSkillDataManager.SkillDataKey<Integer> BLADE_CLASH_RESTORE_COUNTER = create("blade_clash_restore_counter", INTEGER);
    public static final MaidSkillDataManager.SkillDataKey<Integer> STEP_RESTORE_COUNTER = create("step_restore_counter", INTEGER);
    public static <V> MaidSkillDataManager.SkillDataKey<V> create(String namespace, String path, MaidSkillDataManager.ValueType<V> valueType) {
        return create(ResourceLocation.fromNamespaceAndPath(namespace, path), valueType);
    }
    public static <V> MaidSkillDataManager.SkillDataKey<V> create(ResourceLocation id, MaidSkillDataManager.ValueType<V> valueType) {
        return MaidSkillDataManager.SkillDataKey.createDataKey(id, valueType);
    }
    public static <V> MaidSkillDataManager.SkillDataKey<V> create(ResourceLocation id, MaidSkillDataManager.ValueType<V> valueType, V defaultValue) {
        return MaidSkillDataManager.SkillDataKey.createDataKey(id, valueType, defaultValue);
    }
    private static <V> MaidSkillDataManager.SkillDataKey<V> create(String path, MaidSkillDataManager.ValueType<V> valueType) {
        return create(EFTLM.MODID, path, valueType);
    }
}
