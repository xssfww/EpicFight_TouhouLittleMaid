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
监听该事件并注册你的技能即可，无需修改核心代码即可扩展女仆的技能。

**Example usage（使用实例）:**
```java
@SubscribeEvent
public static void onSkillBuild(MaidSkillBuildEvent event) {
    // Your custom skill here （在这里添加你的自定义技能）
    event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"blade_clash"), BladeClash::new, BladeClash.createBuilder().setCreativeTab(EFTLM_Tab.SKILL.get()));
    event.build(ResourceLocation.fromNamespaceAndPath(EFTLM.MODID,"step"), Step::new, Step.createStepBuilder().setCreativeTab(EFTLM_Tab.SKILL.get()));
    EFNCompat.tryBuildSkills(event);
}
```
### Custom Maid Skill Create（自定义女仆技能构建函数）
如果你需要为女仆的技能扩展构建函数，请使用 **`MaidSkillBuildEvent`** 事件的子类**`SkillCreateEvent`**。
监听该事件并处理你的扩展逻辑即可，无需修改核心代码即可扩展女仆技能的构建函数。

**Example usage（使用实例）:**
```java
@SubscribeEvent
public static void onSkillCreate(MaidSkillBuildEvent.SkillCreateEvent event) {
    // Your custom skill builder here （在这里添加你的自定义技能构建函数）
    EFNCompat.tryCreateSkills(event);
}
```