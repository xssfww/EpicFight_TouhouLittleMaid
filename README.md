## 🛠️ For Developers（开发者须知）
### Custom Maid Attack Logic （自定义女仆攻击逻辑）
If you want to add custom attack logic for maids, use the **`CombatBehaviorsEvent`** event.
Subscribe to this event and register your own attack behavior there. This allows you to extend how maids fight without touching the core code.

如果你需要为女仆添加自定义的攻击逻辑，请使用 **`CombatBehaviorsEvent`** 事件。
监听该事件并注册你的攻击行为即可，无需修改核心代码即可扩展女仆的战斗方式。

**Example usage（使用实例）:**
```java
@SubscribeEvent
public static void onCombatBehavior(CombatBehaviorsEvent event) {
    // Your custom attack logic here （在这里添加你的自定义攻击逻辑）
    EFNCompat.trySetWeaponMotions(event.getItemAttackMotions(), event.getItemStyleAttackMotions(), event.getItemArmatures());
}
