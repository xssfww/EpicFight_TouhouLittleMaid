## 🛠️ For Developers（开发者须知）
### Custom Maid Attack Logic （自定义女仆攻击逻辑）
如果你需要为女仆添加自定义的攻击逻辑，请使用 **`CombatBehaviorsEvent`** 事件。
监听该事件并注册你的攻击行为即可，无需修改核心代码即可扩展女仆的战斗方式。

**Example usage（使用实例）:**
```java
@SubscribeEvent
public static void onCombatBehavior(CombatBehaviorsEvent event) {
    // Your custom attack logic here （在这里添加你的自定义攻击逻辑）
    EFNCompat.trySetWeaponMotions(event.getItemAttackMotions(), event.getItemStyleAttackMotions(), event.getItemArmatures());
}
```
### Custom Maid Skill （自定义女仆技能）
如果你需要为女仆添加自定义的技能，请使用 **`MaidSkillBuildEvent`** 事件。
该事件属于模组总线事件（`IModBusEvent`），由 `MaidSkillManager.MaidSkillBuild()` 在 `FMLCommonSetupEvent` 阶段派发，
监听该事件并注册你的技能即可，无需修改核心代码即可扩展女仆的技能。

事件提供 4 个 `build` 重载，按你需要匹配的武器范围选择：

```java
// 1. 普通技能（仅注册进技能总表）
public <S extends MaidSkill, B extends MaidSkillBuilder<?>> void build(ResourceLocation RegisterName, Function<B, S> constructor, B builder){}

// 2. 武器固有技能 —— 按物品匹配（可变参数，可一次绑定多个物品）
public <S extends WeaponInnateSkill, B extends MaidSkillBuilder<?>> void build(ResourceLocation RegisterName, Function<B, S> constructor, B builder, Item... item){}

// 3. 武器固有技能 —— 按 物品 + 架势 匹配
public <S extends WeaponInnateSkill, B extends MaidSkillBuilder<?>> void build(ResourceLocation RegisterName, Function<B, S> constructor, B builder, Item item, Style style){}

// 4. 武器固有技能 —— 按 武器类别 + 架势 匹配（对整类武器生效）
public <S extends WeaponInnateSkill, B extends MaidSkillBuilder<?>> void build(ResourceLocation RegisterName, Function<B, S> constructor, B builder, WeaponCategory category, Style style){}
```

| 重载 | 写入的注册表 | 匹配范围 |
| --- | --- | --- |
| `build(name, ctor, builder)` | `MaidSkillRegister` | 普通技能 |
| `build(name, ctor, builder, Item...)` | `WeaponInnateRegister` + `MaidSkillRegister` | 指定物品 |
| `build(name, ctor, builder, Item, Style)` | `ItemStyleInnateRegister` + `MaidSkillRegister` | 指定物品的指定架势 |
| `build(name, ctor, builder, WeaponCategory, Style)` | `CategoryStyleInnateRegister` + `MaidSkillRegister` | 指定武器类别的指定架势 |

所有重载的内部流程完全一致（统一由 `createSkill` 处理）：
`builder.setRegistryName(RegisterName)` → 派发子事件 **`SkillCreateEvent`** → 由 `constructor` 构造技能实例 → 按上表写入注册表。
因此无论走哪条注册路径，都能用 `SkillCreateEvent` 扩展构建函数。

> 注意：`Item` / `Style` / `WeaponCategory` 为 `null` 时，重载 3、4 只会把技能登记进技能总表，不会写入对应的匹配表。

**Example usage（使用实例）:**
```java
@SubscribeEvent
public static void onSkillBuild(MaidSkillBuildEvent event) {
    // 普通技能（Your custom skill here / 在这里添加你的自定义技能）
    event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "blade_clash"), BladeClash::new,
            BladeClash.createBuilder().setCreativeTab(EFTLM_Tab.SKILL.get()));
    event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "step"), Step::new,
            Step.createStepBuilder().setCreativeTab(EFTLM_Tab.SKILL.get()));

    // 武器固有技能：按物品匹配（可变参数，一次绑定多把刀）
    event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "yamato_innate"), YamatoSkill::new,
            MaidSkill.createBuilder(), EFNItem.YAMATO_DMC_IN_SHEATH.get(), EFNItem.YAMATO_DMC4_IN_SHEATH.get());

    // 武器固有技能：按 物品 + 架势 匹配（YourWeaponSkill 为示例占位类名，下同）
    event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "exsilium_one_hand"), YourWeaponSkill::new,
            MaidSkill.createBuilder(), EFNItem.EXSILIUMGLADIUS.get(), CapabilityItem.Styles.ONE_HAND);

    // 武器固有技能：按 武器类别 + 架势 匹配（对整类武器生效，无需逐个物品注册）
    // WeaponCategory 可来自 WeaponCategory.ENUM_MANAGER，或取自物品的 CapabilityItem#getWeaponCategory()
    event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "dagger_innate"), YourCategorySkill::new,
            MaidSkill.createBuilder(), WeaponCategory.ENUM_MANAGER.getOrThrow("dagger"), CapabilityItem.Styles.COMMON);

    EFNCompat.tryBuildSkills(event);
}
```

#### Weapon Innate Skill Lookup（武器固有技能匹配优先级）
女仆手持物品获取固有技能时调用 `MaidSkillManager.getWeaponSkillFor(item, category, style)`，按以下顺序返回**第一个**命中项：

1. `WeaponInnateRegister` —— 物品精确匹配（重载 2）
2. `ItemStyleInnateRegister` —— 物品 + 架势匹配（重载 3）
3. `CategoryStyleInnateRegister` —— 武器类别 + 架势匹配（重载 4）
4. 都没有时返回 `null`（该物品没有固有技能）

第 2、3 步中，传入的架势没有登记时会退回该物品 / 类别的 **`CapabilityItem.Styles.COMMON`** 条目；
也就是说，用 `CapabilityItem.Styles.COMMON` 注册即可作为该物品 / 类别下所有架势的兜底。
（若 `style` 本身为 `null`，该层匹配直接跳过，不会查询 `COMMON`。）

### Custom Maid Skill Create（自定义女仆技能构建函数）
如果你需要为女仆的技能扩展构建函数，请使用 **`MaidSkillBuildEvent`** 事件的子类 **`SkillCreateEvent`**。
它在 `MaidSkillBuildEvent` 的每个 `build` 重载中构造技能实例之前派发，泛型参数为**当前构建器的运行时类型**。
监听该事件并处理你的扩展逻辑即可，无需修改核心代码即可扩展女仆技能的构建函数。

**Example usage（使用实例）:**
```java
@SubscribeEvent
public static void onSkillCreate(MaidSkillBuildEvent.SkillCreateEvent<?> event) {
    // Your custom skill builder here （在这里添加你的自定义技能构建函数）
    // 泛型参数为构建器运行时类型，因此需要自行判断具体构建器
    if (event.getSkillBuilder() instanceof Step.Builder builder) {
        // 扩展构建器参数
    }
    EFNCompat.tryCreateSkills(event);
}
```
### Skill Lifecycle Events（技能生命周期事件）
除构建阶段外，技能运行时还可以接入以下事件（均继承 `AbstractMaidEvent`，可用 `event.getMaidPatch()` 取得女仆的 `MaidPatch<?>`）：

| 事件 | 总线 | 派发时机 | 可取消 |
| --- | --- | --- | --- |
| `MaidSkillInitEvent` | `Bus.FORGE` | 女仆新学会技能时（携带该技能名）；女仆加入世界时（不携带技能名） | 否 |
| `MaidSkillRemoveEvent` | `Bus.FORGE` | 玩家在技能书界面遗忘技能时 | 是（`@Cancelable`） |
| `MaidChangeItemEvent` | `Bus.FORGE` | 女仆主/副手物品、持有架势或状态变化，以及加入世界时 | 否 |

#### MaidSkillInitEvent（技能数据初始化）
`MaidSkillInitEvent` 有两种形态：

- 携带技能名（`getSkillName() != null`）：女仆**新学会**该技能时派发（由 `MaidPatch#addLearnedSkill` 触发）。
- 不携带技能名（`getSkillName() == null`）：女仆**加入世界**时派发，作用于该女仆**全部已学技能**。

本模组默认只在**服务端**处理，并把事件转发给对应技能的 `onInit(event)`。
它是注册技能数据的唯一时机，提供的两个方法都会直接写入 `MaidPatch`：

```java
public <V> void registerData(MaidSkill skill, MaidSkillDataManager.SkillDataKey<V> key) {}             // 使用键的默认值
public <V> void registerData(MaidSkill skill, MaidSkillDataManager.SkillDataKey<V> key, V data) {}      // 指定初始值
```

#### MaidSkillRemoveEvent（技能遗忘 / 移除）
当玩家在技能书界面遗忘技能时，客户端发送 `ForgetMaidSkillPacket`，服务端在 Forge 总线上派发该事件。

```java
public MaidSkill getSkill() {}            // 被移除的技能实例
public ResourceLocation getSkillName() {} // 技能注册名
public void removeData() {}               // 清除该技能在 MaidPatch 上的全部数据
```

本模组默认处理逻辑：

- 若技能是 `WeaponInnateSkill`，直接 `setCanceled(true)`——**武器固有技能无法被遗忘**（它们由手持武器自动装卸，详见下一节）。
- 其他技能则执行 `removeLearnedSkill(注册名)` 并调用 `event.removeData()`。

取消该事件即可**中止遗忘**：技能与数据都不会被移除，也不会向客户端同步、不会发送成功提示。

#### MaidChangeItemEvent（武器固有技能的自动装卸）
女仆主/副手物品、持有架势或状态（抱、睡、坐）发生变化时，服务端派发该事件。
本模组默认处理逻辑是：取当前手持武器匹配到的 `WeaponInnateSkill`，把**不再匹配**的固有技能逐个执行 `onRemove(event)`（默认实现会清除该技能数据）并 `removeLearnedSkill`，最后为当前武器 `addLearnedSkill`。

因此武器固有技能**不需要**自己注册、也不应依赖手动学习；`MaidSkill#onRemove(MaidChangeItemEvent)` 默认会清理数据，若你的技能还有额外状态需要还原，重写它并调用 `super.onRemove(event)`。

### Skill Hooks（MaidSkill 回调一览）
继承 `MaidSkill`（或 `WeaponInnateSkill`）后按需重写即可，全部回调都只对**已学会**的技能触发，且仅服务端生效：

| 回调 | 触发时机 |
| --- | --- |
| `onInit(MaidSkillInitEvent)` | 技能数据初始化（见上） |
| `onMaidTick(MaidTickEvent, MaidPatch<?>)` | 女仆每 tick |
| `onMaidAttack(MaidAttackEvent, MaidPatch<?>)` | 女仆攻击 |
| `onMaidHurt(MaidHurtEvent, MaidPatch<?>)` | 女仆受伤 |
| `onMaidDamage(MaidDamageEvent, MaidPatch<?>)` | 女仆结算伤害 |
| `onMaidDeath(MaidDeathEvent, MaidPatch<?>)` | 女仆死亡 |
| `onHurtTargetPre / onHurtTargetPost(MaidHurtTargetEvent.Pre/Post)` | 女仆命中目标前 / 后 |
| `onKillTarget(MaidKilledEvent)` | 女仆击杀目标 |
| `onRemove(MaidChangeItemEvent)` | 切换武器导致该固有技能被卸下 |
| `canExecute(MaidPatch<?>)` | 判断本轮是否允许执行该技能（默认要求处于战斗模式） |

除 `onInit` / `onRemove` / `onMaidDeath` 外，上述回调都会先经过 `canExecute` 过滤（返回 `false` 则该 tick 跳过）。

### Skill Data API（技能数据 API）
技能数据用于给每个技能在每只女仆身上保存独立状态（层数、能量、冷却、计时器等），随女仆持久化 NBT 保存并同步到客户端。

**1. 定义数据键**（`MaidSkillDataManager.SkillDataKey<V>`，`V` 为 `Integer` / `Float` / `Boolean`）：

```java
// 便捷写法：MaidSkillDataKeys.create(namespace, path, valueType)
public static final MaidSkillDataManager.SkillDataKey<Integer> MY_CHARGE =
        MaidSkillDataKeys.create("your_modid", "my_charge", MaidSkillDataManager.ValueType.integer());

// 等价的底层写法：可直接指定默认值
public static final MaidSkillDataManager.SkillDataKey<Float> MY_PENALTY =
        MaidSkillDataManager.SkillDataKey.createDataKey(
                ResourceLocation.fromNamespaceAndPath("your_modid", "my_penalty"),
                MaidSkillDataManager.ValueType.floatType(), 0.0F);
```

- 可用值类型：`MaidSkillDataManager.ValueType.integer()` / `floatType()` / `booleanType()`（`MaidSkillDataKeys` 中也预置了 `INTEGER` / `FLOAT` / `BOOLEAN`）。
- 不指定默认值时为该类型的零值（`0` / `0.0F` / `false`）。
- 键必须在全局唯一，重复 id 会在 `createDataKey` 时抛出 `IllegalStateException`。
- 键会登记进全局表并按 `ResourceLocation` 反序列化，因此请以**静态常量**形式定义，保证技能类加载时即已创建。

**2. 注册与读写**（在 `MaidSkillInitEvent` 注册，之后通过 `MaidPatch` 读写）：

| 方法 | 说明 |
| --- | --- |
| `MaidSkillInitEvent#registerData(skill, key[, data])` | 在 `onInit` 中注册，未注册的键不会被保存 |
| `MaidPatch#registerData(skill, key, data)` | 底层注册，创建容器并写入初值 |
| `MaidPatch#setData(skill, key, data)` | 写入；键未注册时**静默失败** |
| `MaidPatch#getDataValue(skill, key)` | 读取；键未注册时返回 `null` |
| `MaidPatch#hasData(skill, key)` | 判断是否已注册该键 |
| `MaidPatch#removeData(skill)` | 清除该技能的全部数据（遗忘 / 卸下技能时调用） |

**Example usage（使用实例）:**
```java
public class MySkill extends MaidSkill {
    public static final MaidSkillDataManager.SkillDataKey<Integer> MY_CHARGE =
            MaidSkillDataKeys.create("your_modid", "my_charge", MaidSkillDataManager.ValueType.integer());

    public MySkill(MaidSkillBuilder<? extends MaidSkill> builder) {
        super(builder);
    }
    @Override
    public void onInit(MaidSkillInitEvent event) {
        event.registerData(this, MY_CHARGE);                    // 使用键的默认值
        event.registerData(this, MaidSkillDataKeys.STACK, 3);   // 指定初始值
    }
    @Override
    public void onMaidTick(MaidTickEvent event, MaidPatch<?> patch) {
        Integer charge = patch.getDataValue(this, MY_CHARGE);
        if (charge == null) return;                             // 该键未注册
        patch.setData(this, MY_CHARGE, charge + 1);
    }
    @Override
    public void onRemove(MaidChangeItemEvent event) {
        super.onRemove(event);                                  // 默认清除该技能的全部数据
    }
}
```

> 若技能继承 `WeaponInnateSkill` 并重写 `onInit`，请先调用 `super.onInit(event)`——父类需要注册 `MaidSkillDataKeys.STACK` 与 `ENERGY`。

**3. 在战斗行为中读写**：`CombatBehaviors` 的条件 / 招式里通常只能拿到 `LivingEntityPatch<?>`，可使用 `BehaviorsBuild` 提供的静态工具：

```java
WeaponInnateSkill skill = BehaviorsBuild.getWeaponInnateSkill(patch, MySkill.class);
Integer charge = BehaviorsBuild.getDataValue(patch, skill, MySkill.MY_CHARGE);
BehaviorsBuild.setData(patch, skill, MySkill.MY_CHARGE, charge + 1);
int stack = BehaviorsBuild.getStack(patch);   // 当前层数 / 上限 / 设置层数
int max = BehaviorsBuild.getMaxStack(patch);
BehaviorsBuild.setStack(patch, 0);
```

`MaidSkillDataKeys` 中已内置本模组使用的键（`STACK`、`ENERGY`、`CLAW_TIME`、`MEEN_CHARGING_TIME`、`YAMATO_*`、`MURASAMA_ZANSETSU*`、`HF_BLADE_ZANSETSU*`、`BLADE_CLASH_*`、`STEP_RESTORE_COUNTER`），可直接复用，也可按上面的方式定义自己的键。

### Weapon Innate Skill Data（武器固有技能数值）
`WeaponInnateSkill` 的数值来自数据包：`data/<namespace>/maid_skill/<registry_name_path>.json`，
文件名必须与 `build` 时使用的 `ResourceLocation` 完全一致，加载后由 `WeaponInnateSkill#setParams` 读取：

```json
{
  "max_stacks": 12,
  "consumption": 30.0
}
```

如果缺少对应的数据文件，`consumption` 与 `max_stacks` 均保持为 `0`，充能与层数机制不会生效。
子类可重写 `setParams` 读取额外字段（先调用 `super.setParams(parameters)`），例如 `YamatoSkill` 读取的 `reinforce_chance`、`require_heavy_rain_enchantment`。

### Debug Command（调试命令）
需要 4 级权限，参数为技能注册名，可用于测试自定义技能：

```
/maid add <RegisterName>
/maid remove <RegisterName>
/maid clear
```
