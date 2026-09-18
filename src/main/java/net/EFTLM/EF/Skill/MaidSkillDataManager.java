package net.EFTLM.EF.Skill;

public class MaidSkillDataManager {
    public static class SkillDataKey<T> {
        public static final ValueType<Integer> INTEGER = new ValueType.IntegerType();
        public static final ValueType<Float> FLOAT = new ValueType.FloatType();
        public static final ValueType<Boolean> BOOLEAN = new ValueType.BooleanType();
        protected final ValueType<T> valueType;
        public static <V> SkillDataKey<V> createDataKey(ValueType<V> valueType) {
            return new SkillDataKey<>(valueType);
        }
        protected SkillDataKey(ValueType<T> valueType) {
            this.valueType = valueType;
        }
        public ValueType<T> getValueType() {
            return this.valueType;
        }
    }
    public abstract static class ValueType<T> {
        public ValueType() {
        }
        public abstract Data create();
        public abstract void set(Data var1, Object var2);
        public abstract T get(Data var1);
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
        }
    }
    public static class Data {
        Data() {
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
