# libs/ — 构建依赖与 jarJar 内置件

本目录的 jar **必须保持跟踪**（`.gitignore` 有意不忽略它们），原因有两层：

1. `build.gradle` 通过 `flatDir { dir 'libs' }` + `implementation fg.deobf("libs:…:1.0.0")`
   把它们当作**编译依赖**；
2. 其中 `YSM_GEO_Compat.jar` 同时通过 `jarJar 'libs:YSM_GEO_Compat:[1.0.0,1.0.0]'`
   **内嵌进发布产物** `…-all.jar` 的 `META-INF/jarjar/`。

删掉任何一个都会直接导致构建失败或发布件缺少内置模组。

## 依赖清单

| 文件 | 说明 |
|---|---|
| `EpicFight-Nightfall.jar`、`Nightfall-Enhance.jar` | 夜幕（Nightfall）及其增强 |
| `indestructible.jar`、`ef_skin.jar`、`ef_anim_phy.jar` | 扩展依赖 |
| `YSM_GEO_Compat.jar` | **本项目内置的模组**，见下节 |

## YSM_GEO_Compat.jar（内置件）

| 项 | 值 |
|---|---|
| Mod ID | `ysm_geo_compat` |
| Mod 版本 | `1.0.3` |
| 许可 | MIT |
| 源码仓库 | https://github.com/HSZK2017/ysm_geo_compat |
| **源码提交** | `fed33c80eb4343a65650673556f00bbb38243f07`（"同步主项目更新进度"，分支 `main`，即当前 `main` 头） |
| 构件 SHA-256 | `484785F1E37843487F24BFCB93B8523B8617B4A1BFA19D622538E28131FCF2BA` |
| 大小 | 239593 字节 |

**作用**：把车万女仆的 TLM GEO 模型（`tlm_custom_pack` 目录包与 jar 内置 `maid_model.json`
条目）在运行时转换为 Epic Fight 的 `SkinnedMesh`，使自定义女仆模型可以使用 Epic Fight
的战斗动画；对使用 YSM 模型的女仆主动让位给 `YSM_EpicFight_Compat`。

**发布形态**：`jarJar` 把它原样内嵌（`metadata.json` 中 `isObfuscated: false`），
所以产物里的 `META-INF/jarjar/YSM_GEO_Compat.jar` 应与本目录文件**逐字节一致**。

### 更新流程

1. 在源码项目构建：`gradlew build`（**需要 JDK 21**，见文末"构建前置条件"）
2. 把 `build/libs/YSM_GEO_Compat-<mc版本>-<mod版本>.jar` 复制为 `libs/YSM_GEO_Compat.jar`
   —— **文件名必须保持不变**。flatDir 是按文件名解析坐标的，`build.gradle` 里的
   `libs:YSM_GEO_Compat:1.0.0` 只是坐标标签，与该 mod 自身的版本号（`1.0.3`）**解耦**，
   无需随版本修改；但改文件名会让依赖解析直接失败。
3. 更新本文件的「Mod 版本 / 源码提交 / 构件 SHA-256 / 大小」四行
4. 重新构建并校验内嵌件与 `libs/` 一致：

```powershell
gradlew build    # JDK 21
Add-Type -AssemblyName System.IO.Compression.FileSystem
$all = Get-ChildItem build\libs -Filter *-all.jar | Select-Object -First 1
$z = [System.IO.Compression.ZipFile]::OpenRead($all.FullName)
$e = $z.Entries | Where-Object FullName -eq "META-INF/jarjar/YSM_GEO_Compat.jar"
[System.IO.Compression.ZipFileExtensions]::ExtractToFile($e, "$env:TEMP\embedded.jar", $true)
$z.Dispose()
(Get-FileHash "$env:TEMP\embedded.jar").Hash -eq (Get-FileHash libs\YSM_GEO_Compat.jar).Hash   # 期望 True
```

### 依赖区间约定（重要）

内置件的 `META-INF/mods.toml` 里 `epicfight` 依赖区间**必须与本仓库自身的区间一致**
（当前两边都是 `[20.14,)`）。若内置件写得更窄（历史上一度是 `[20.14.17,20.15)`），
那些**能正常加载本仓库**的玩家（例如 EF 20.14.5）会撞上**内置模组的强制依赖报错**，
错误信息指向嵌套 mod，排查成本很高。

区间之外的兼容性由内置件的运行时失败容忍兜底（mixin 目标、反射的 `SkinnedMesh` 字段、
`ComputeShaderSetup` API 失败即回退到 Epic Fight 自带绘制路径）。Epic Fight **20.15**
出现时需重新校验这些点，并与本仓库同步放开区间。

## 构建前置条件（新克隆）

1. **Gradle 需运行在 JDK 21**。若 PATH 上的 `java` 过新（例如 26），Gradle 8.8 会直接报
   `Unsupported class file major version 70`。请设置 `JAVA_HOME`，或在 IDE 中指定
   Gradle JVM——不要在本仓库提交本机绝对路径。
2. **首次构建不要加 `--offline`**：6 个本地 `libs/*.jar` 需要 ForgeGradle 先做 deobf
   落盘（`bundled_deobf_repo` 一开始并不存在），联网跑过一次后才进缓存。

## 发布产物

| 产物 | 说明 |
|---|---|
| `…-1.3.3-all.jar` | **含** `META-INF/jarjar/` 内嵌件——这是分发给玩家的文件 |
| `…-1.3.3.jar` | 不含内嵌件，仅用于开发/调试 |
