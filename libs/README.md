# libs/ — 构建依赖与 jarJar 内置件

本目录的 jar **保持跟踪**（`.gitignore` 有意不忽略它们），原因与 1.20.1 分支相同：
`build.gradle` 通过 `flatDir { dirs 'libs' }` + `implementation files(...)` 把它们当作编译/运行依赖。

## 1.21.1 分支使用的 jar（本 PR 已提交）

| 文件 | 对应发布的 jar | 大小 | SHA-256（前 16 位） | 用途 |
| --- | --- | --- | --- | --- |
| `epicfight.jar` | `epic-fight-21.17.3.1-mc1.21.1-neoforge.jar` | 8,578,288 | `8B882554CF100863` | 编译 + 运行必需 |
| `touhoulittlemaid.jar` | `touhoulittlemaid-1.5.3-neoforge+mc1.21.1.jar` | 24,408,776 | `F6DB04195820C850` | 编译 + 运行必需 |
| `epic_fight_avalon.jar` | **NightFall 3.4.0 内部 jarJar 的那份** `epic_fight_avalon-21.12.6.2.jar` | 669,936 | `3FFCA60ED07BA758` | 编译 + 运行必需 |
| `nightfall.jar` | `NightFall-1.21.1-Neoforge-3.4.0.jar` | 52,764,179 | `EC66E0B42A0F8561` | 编译（EFN 相关代码）+ 运行可选 |
| `invincible.jar` | `invincible-21.15.8.2-mc1.21.1-neoforge.jar` | 245,153 | `59ECDE959881F679` | NightFall 的强制运行依赖（`invincible [21.15,)`） |
| `geckolib.jar` | `geckolib-neoforge-1.21.1-4.9.3.jar` | 634,531 | `20A1995E4074F387` | NightFall 的强制运行依赖（`geckolib [4.9.1,)`） |

`invincible` / `geckolib` 不是编译期依赖（`build.gradle` 里声明为 `runtimeOnly`），但 `runClient` 必须能
加载它们，否则 NightFall 会因为缺少强制依赖而拒绝加载。

⚠️ **Avalon 的坑（重要）**：独立发布的 `epic_fight_avalon-neoforge1.21.1-21.12.6.2.jar`（667,048 字节）
是针对更早的 EpicFight 编译的，仍然引用已被删除的 `yesman.epicfight.api.client.neoevent` 包，
在 EpicFight 21.17.3.1 下启动时会因 mixin 应用失败直接崩溃。请使用 NightFall 3.4.0 内置的那份
（669,936 字节），即本目录提交的文件。

## 1.20.1 分支遗留、本分支未使用

`EpicFight-Nightfall.jar`、`Nightfall-Enhance.jar`、`indestructible.jar`、`YSM_GEO_Compat.jar`
是 1.20.1 Forge 构建，`build.gradle` 已不再引用它们。其中：

- `Nightfall-Enhance.jar`（`efn_enhance`）在 1.21.1 **没有对应版本**，相关功能已在移植中移除；
- `YSM_GEO_Compat.jar`（`ysm_geo_compat`）是 1.20.1 的 TLM GEO / YSM 模型桥接件，
  **1.21.1 版本尚未纳入本分支**，因此自定义女仆模型在战斗模式下会回落到 Epic Fight 默认模型；
- 是否删除这些遗留 jar，交由维护者决定（本 PR 未删除任何既有文件）。

`ef_skin.jar` / `ef_anim_phy.jar` 本身已是 1.21.1 NeoForge 构建，可按需作为可选依赖使用。
