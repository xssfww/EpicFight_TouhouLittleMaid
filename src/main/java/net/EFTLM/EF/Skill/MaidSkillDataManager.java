package net.EFTLM.EF.Skill;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;
import java.util.Map;

public class MaidSkillDataManager {
    public static class SkillDataKey<T> {
        protected static final Map<ResourceLocation, SkillDataKey<?>> BY_ID = Maps.newHashMap();
        protected final ResourceLocation id;
        protected final ValueType<T> valueType;
        protected final T defaultValue;
        public static <V> SkillDataKey<V> createDataKey(ResourceLocation id, ValueType<V> valueType) {
            return createDataKey(id, valueType, valueType.get(null));
        }
        public static <V> SkillDataKey<V> createDataKey(ResourceLocation id, ValueType<V> valueType, V defaultValue) {
            SkillDataKey<V> key = new SkillDataKey<>(id, valueType, defaultValue);
            SkillDataKey<?> previous = BY_ID.putIfAbsent(id, key);
            if (previous != null) {
                throw new IllegalStateException("Duplicate MaidSkillDataManager key id: " + id);
            }
            return key;
        }
        @Nullable
        public static SkillDataKey<?> byId(ResourceLocation id) {
            return BY_ID.get(id);
        }
        protected SkillDataKey(ResourceLocation id, ValueType<T> valueType, T defaultValue) {
            this.id = id;
            this.valueType = valueType;
            this.defaultValue = defaultValue;
        }
        public ResourceLocation getId() {
            return this.id;
        }
        public ValueType<T> getValueType() {
            return this.valueType;
        }
        public T getDefaultValue() {
            return this.defaultValue;
        }
    }
    public abstract static class ValueType<T> {
        public static ValueType<Integer> integer() {
            return new IntegerType();
        }
        public static ValueType<Float> floatType() {
            return new FloatType();
        }
        public static ValueType<Boolean> booleanType() {
            return new BooleanType();
        }
        public abstract Data create();
        public abstract void set(Data var1, Object var2);
        public abstract T get(Data var1);
        public abstract void write(CompoundTag tag, String name, Data data);
        public abstract void read(CompoundTag tag, String name, Data data);
        protected static class IntegerType extends ValueType<Integer> {
            private IntegerType() {
            }
            public Data.IntegerData create() {
                return new Data.IntegerData();
            }
            public void set(Data data, Object value) {
                ((Data.IntegerData) data).data = (Integer) value;
            }
            public Integer get(Data data) {
                return data != null ? ((Data.IntegerData) data).data : 0;
            }
            public void write(CompoundTag tag, String name, Data data) {
                tag.putInt(name, data instanceof Data.IntegerData integerData ? integerData.data : 0);
            }
            public void read(CompoundTag tag, String name, Data data) {
                if (data instanceof Data.IntegerData integerData && tag.contains(name, Tag.TAG_INT)) {
                    integerData.data = tag.getInt(name);
                }
            }
        }
        protected static class FloatType extends ValueType<Float> {
            private FloatType() {
            }
            public Data.FloatData create() {
                return new Data.FloatData();
            }
            public void set(Data data, Object value) {
                ((Data.FloatData) data).data = (Float) value;
            }
            public Float get(Data data) {
                return data != null ? ((Data.FloatData) data).data : 0.0F;
            }
            public void write(CompoundTag tag, String name, Data data) {
                tag.putFloat(name, data instanceof Data.FloatData floatData ? floatData.data : 0.0F);
            }
            public void read(CompoundTag tag, String name, Data data) {
                if (data instanceof Data.FloatData floatData && tag.contains(name, Tag.TAG_FLOAT)) {
                    floatData.data = tag.getFloat(name);
                }
            }
        }
        protected static class BooleanType extends ValueType<Boolean> {
            private BooleanType() {
            }
            public Data.BooleanData create() {
                return new Data.BooleanData();
            }
            public void set(Data data, Object value) {
                ((Data.BooleanData) data).data = (Boolean) value;
            }
            public Boolean get(Data data) {
                return data != null && ((Data.BooleanData) data).data;
            }
            public void write(CompoundTag tag, String name, Data data) {
                tag.putBoolean(name, data instanceof Data.BooleanData booleanData && booleanData.data);
            }
            public void read(CompoundTag tag, String name, Data data) {
                if (data instanceof Data.BooleanData booleanData && tag.contains(name, Tag.TAG_BYTE)) {
                    booleanData.data = tag.getBoolean(name);
                }
            }
        }
    }
    public static class Data {
        protected Data() {
        }
        protected static class FloatData extends Data {
            float data;
            FloatData() {
            }
        }
        protected static class BooleanData extends Data {
            boolean data;
            BooleanData() {
            }
        }
        protected static class IntegerData extends Data {
            int data;
            IntegerData() {
            }
        }
    }
}
