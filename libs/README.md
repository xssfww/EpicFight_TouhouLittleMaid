# libs/ — 1.21.1 依赖 jar 放置说明

这个 PR 的 `build.gradle` 沿用 1.20.1 分支的约定：依赖 mod 的 jar 放在 `libs/`（`.gitignore` 里明确说
`libs/*.jar` 是**故意跟踪**的）。1.21.1 需要下面这些 jar，文件名要和 `build.gradle` 里一致：

| 放进 libs/ 的文件名 | 来源 jar | 必需性 |
| --- | --- | --- |
| `epicfight.jar` | `[史诗战斗] epic-fight-21.17.3.1-mc1.21.1-neoforge.jar` | 编译 + 运行必需 |
| `touhoulittlemaid.jar` | `[车万女仆] touhoulittlemaid-1.5.3-neoforge+mc1.21.1.jar` | 编译 + 运行必需 |
| `epic_fight_avalon.jar` | **NightFall 3.4.0 内部 jarJar 的那份** `epic_fight_avalon-21.12.6.2.jar`（669,936 字节，SHA-256 `3FFCA60ED07BA758599CD2521C92C6DBCF77CC48D083D471BB5E9689306AD08C`） | 编译 + 运行必需 |
| `nightfall.jar` | `[史诗战斗：夜幕] NightFall-1.21.1-Neoforge-3.4.0.jar` | 可选（缺失时 NightFall 相关技能不生效） |
| `invincible.jar` | `[无坚不摧] invincible-21.15.8.2-mc1.21.1-neoforge.jar` | NightFall 的强制运行依赖 |
| `geckolib.jar` | `geckolib-neoforge-1.21.1-4.9.3.jar` | NightFall 的强制运行依赖（≥4.9.1） |

⚠️ **Avalon 注意**：市面上流传的独立版 `epic_fight_avalon-neoforge1.21.1-21.12.6.2.jar`（667,048 字节）
是针对更早的 EpicFight 编译的，仍引用已被删除的 `yesman.epicfight.api.client.neoevent` 包，
在 EF 21.17.3.1 下启动时会因 mixin 应用失败而崩溃。请用 NightFall 内置的那份（669,936 字节）。

如果维护者更希望走 maven 坐标而不是 `libs/`，`build.gradle` 底部已经留了三行注释示例，
把 `curse.maven` 的文件 id / `maven.modrinth` 的版本号填上即可。
