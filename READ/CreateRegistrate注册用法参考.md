# CreateRegistrate 注册用法参考

基于 Create 6.0（1.21.1 NeoForge），适用于附属模组开发。

---

## 一、初始化

在 `IntegratedIndustry.java` 中声明：

```java
public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
```

在构造方法中注册事件：

```java
REGISTRATE.registerEventListeners(modEventBus);
```

---

## 二、注册方块 Block

```java
public static final BlockEntry<RotatedPillarBlock> XXX = REGISTRATE
    .block("block_name", 方块类::new)

    // ── 属性 ──
    .initialProperties(() -> Blocks.OAK_WOOD)       // 继承已有方块的属性
    .properties(p -> p                             // 覆盖/补充属性
        .mapColor(DyeColor.BROWN)                  // 地图颜色
        .sound(SoundType.WOOD)                     // 声音
        .strength(1.5f)                            // 硬度
        .strength(1.5f, 6.0f)                      // 硬度和抗炸
        .lightLevel(state -> 15)                   // 亮度 0~15
        .requiresCorrectToolForDrops()              // 需要正确工具
        .noOcclusion()                              // 非完整方块
        .noCollission()                             // 无碰撞箱
        .friction(0.8f)                            // 摩擦系数
        .speedFactor(0.5f)                         // 行走速度
        .jumpFactor(0.5f)                          // 跳跃高度
        .randomTicks()                             // 启用随机刻
        .instabreak()                              // 瞬间破坏
        .dynamicShape()                            // 动态形状
    )

    // ── BlockState（模型方向） ──
    // ★ 以下三种方式选一

    // 方式 A：标准 axisBlock（无需预存模型文件）
    .blockstate((ctx, prov) -> prov.axisBlock(ctx.get(),
        prov.models().cubeColumn("block_name",
            prov.modLoc("block/block_name_side"),
            prov.modLoc("block/block_name_top")),
        prov.models().cubeColumnHorizontal("block_name_horizontal",
            prov.modLoc("block/block_name_side"),
            prov.modLoc("block/block_name_top"))))

    // 方式 B：Create BlockStateGen（需要预存模型文件）
    .blockstate(BlockStateGen.axisBlockProvider(true))
    // 需在 src/main/resources/ 预存：
    //   models/block/block_name/block.json
    //   models/block/block_name/block_horizontal.json

    // 方式 C：简单方块（六个面相同）
    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get()))

    // ── 工具和Tag ──
    .transform(TagGen.axeOnly())                   // 斧可挖
    .transform(TagGen.pickaxeOnly())               // 镐可挖
    .transform(TagGen.axeOrPickaxe())              // 斧或镐可挖
    .transform(TagGen.shovelOnly())                // 锹可挖
    .transform(TagGen.hoeOnly())                   // 锄可挖
    .transform(TagGen.needsTool())                 // 需要正确工具

    .tag(BlockTags.NEEDS_STONE_TOOL)               // 需要石工具
    .tag(BlockTags.NEEDS_IRON_TOOL)                // 需要铁工具
    .tag(BlockTags.NEEDS_DIAMOND_TOOL)             // 需要钻石工具
    .tag(BlockTags.WOOL)                           // 羊毛标签
    .tag(BlockTags.LOGS)                           // 原木标签
    .tag(AllTags.AllBlockTags.VALVE_HANDLES.tag)   // Create: 阀门把手可吸附
    .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)  // Create: 风扇可吹透
    .tag(AllTags.AllBlockTags.COPYCAT_ALLOW.tag)    // Create: 仿形板素材允许
    .tag(AllTags.AllBlockTags.BRITTLE.tag)          // Create: 移动时易碎

    // ── 名称 ──
    .lang("English Name")                          // 英文名（自动生成 en_us.json）

    // ── 战利品表 ──
    .loot((p, b) -> p.dropSelf(b))                 // 掉落自身
    .loot((p, b) -> p.dropOther(b, Items.XXX))     // 掉落指定物品
    .loot((p, b) -> p.droppingWithSilkTouch(b,     // 精准采集掉落
        Items.XXX.getDefaultInstance()))

    // ── 物品 ──
    .simpleItem()                                  // 自动生成方块物品
    // 或自定义：
    // .items(Item::new).properties(p -> ...).register()

    .register();                                   // ← 必须调用，返回 BlockEntry<>
```

---

## 三、注册物品 Item

```java
public static final ItemEntry<Item> XXX = REGISTRATE
    .item("item_name", Item::new)
    .properties(p -> p
        .stacksTo(1)                               // 最大堆叠
        .durability(500)                           // 耐久
        .food(foodProperties)                      // 食物属性
        .fireResistant()                           // 防火
    )
    .lang("English Name")
    .model((ctx, prov) -> prov.generated(ctx::getEntry))
    .tag(AllTags.AllItemTags.XXX.tag)
    .register();
```

---

## 四、注册方块实体 BlockEntity

```java
public static final BlockEntityEntry<XXXBlockEntity> XXX = REGISTRATE
    .blockEntity("be_name", XXXBlockEntity::new)
    .validBlocks(ModBlocks.SOME_BLOCK)             // 绑定到哪个方块
    .renderer(() -> XXXRenderer::new)              // 客户端渲染器
    .register();
```

---

## 五、BlockStateGen 方式 vs prov.axisBlock 方式

| | BlockStateGen.axisBlockProvider(true) | prov.axisBlock() |
|---|---|---|
| 模型文件位置 | `block/xxx/block.json` | `block/xxx.json` |
| 需预存 JSON | ✅ 需要手动放 | ❌ 不需要 |
| 轴效果 | XYZ 三轴 | XYZ 三轴 |
| 适用 | 想用 Create 风格的模型结构 | 更简单直接 |

---

## 六、数据生成

运行 `./gradlew runData` 生成到 `src/generated/resources/`。

如果同时在 `src/main/resources/` 和 `src/generated/resources/` 有同名文件，会冲突。注意删掉 `main/resources` 下对应的旧文件。

---

## 七、资源文件结构总览

```
src/main/resources/assets/integratedindustry/
├── blockstates/                     ← 方块状态 JSON（控制方向变体）
│   └── thermal_energy_generator.json
├── models/
│   ├── block/                       ← 方块模型 JSON
│   │   ├── thermal_energy_generator.json
│   │   └── thermal_energy_generator/
│   │       ├── block.json
│   │       └── block_horizontal.json
│   └── item/                        ← 物品模型 JSON
│       └── thermal_energy_generator.json
├── textures/
│   └── block/                       ← 贴图 PNG（16x16 或 32x32 等）
│       ├── thermal_energy_generator_side.png
│       └── thermal_energy_generator_top.png
└── lang/
    └── en_us.json                   ← 英文翻译（runData 自动生成）
    └── zh_cn.json                   ← 中文翻译（手动写）
```

---

## 八、模型 JSON 详解

### 8.1 模型 JSON 各字段含义

```json
{
  "parent": "minecraft:block/cube_column",       ← 继承哪个模型模板
  "textures": {                                   ← 指定贴图
    "end": "integratedindustry:block/yuan_stone", ← 顶/底面用哪张贴图
    "side": "integratedindustry:block/yuan_stone"  ← 侧面用哪张贴图
  }
}
```

| 字段 | 必填 | 说明 |
|---|---|---|
| `parent` | ✅ | 继承的父模型。模型会复制父模型的结构，然后用自己的 `textures` 覆盖 |
| `textures` | 看 parent | 父模型需要的纹理变量名。不填则沿用父模型的默认纹理 |
| `ambientocclusion` | ❌ | `true` / `false`，是否启用环境光遮蔽，默认 true |
| `display` | ❌ | 不同视角（第三人称、GUI 等）的变换参数 |
| `elements` | ❌ | 自定义方块元素（不依赖 parent 时用） |

### 8.2 各字段详解

**`parent`**——继承机制：
```
minecraft:block/cube_column  →  它已经有了 elements（6个面）
  └── 你只填 textures 即可      →  纹理填入父模型的变量位置
```

**`textures`**——贴图路径规则：
```
贴图文件位置                          →  JSON 中引用
assets/模组id/textures/block/xxx.png  →  模组id:block/xxx
assets/minecraft/textures/block/stone →  minecraft:block/stone

# 引用本模组贴图：
"integratedindustry:block/yuan_stone"

# 引用原版贴图：
"minecraft:block/oak_log"
"minecraft:block/stone"
```

**`display`**——不同视角的偏移/缩放：
```json
{
  "display": {
    "thirdperson_righthand": {  ← 第三人称右手
      "rotation": [0, 0, 0],
      "translation": [0, 0, 0],
      "scale": [0.5, 0.5, 0.5]
    },
    "gui": {                    ← 背包 GUI 中
      "rotation": [30, 135, 0],
      "translation": [0, 0, 0],
      "scale": [0.5, 0.5, 0.5]
    }
  }
}
```

### 8.3 常用 parent 类型

| parent | 需要的 textures 变量 | 说明 |
|---|---|---|
| `minecraft:block/cube_all` | `all` | 六面同纹理 |
| `minecraft:block/cube_column` | `end`, `side` | 柱状，有顶/侧面区别 |
| `minecraft:block/cube_column_horizontal` | `end`, `side` | 柱状水平方向（RotatedPillarBlock 横放时用） |
| `minecraft:block/cube_bottom_top` | `bottom`, `top`, `side` | 顶底侧面各不相同 |
| `minecraft:block/orientable` | `front`, `side`, `top` | 有正面朝向（熔炉） |
| `minecraft:block/cross` | `cross` | 十字形（花） |
| `minecraft:block/crop` | `crop` | 农作物（生长阶段） |
| `minecraft:block/slab` | `bottom`, `top`, `side` | 台阶 |
| `minecraft:block/stairs` | `bottom`, `top`, `side` | 楼梯 |
| `item/generated` | `layer0` | 平面物品图标 |
| `item/handheld` | `layer0` | 手持工具 |

### 8.4 BlockState JSON 各字段含义

```json
{
  "variants": {
    "axis=y":  {                                     ← 条件：当 axis=y 时
      "model": "integratedindustry:block/xxx"        ← 使用的模型
    },
    "axis=x":  {
      "model": "integratedindustry:block/xxx_horizontal",
      "x": 90,                                       ← 绕 X 轴旋转 90°
      "y": 90                                        ← 绕 Y 轴旋转 90°
    },
    "axis=z":  {
      "model": "integratedindustry:block/xxx_horizontal",
      "x": 90
    }
  }
}
```

| 字段 | 说明 |
|---|---|
| `model` | 引用的模型路径（`模组id:block/文件名`） |
| `x` / `y` | 旋转角度（0 / 90 / 180 / 270） |
| `uvlock` | `true` 时 UV 随旋转锁定，防止纹理偏移 |
| `weight` | 随机权重，多个模型时按权重随机选 |

### 8.5 三者的关系

```
textures/xxx.png          ← 像素贴图（原材料）
       ↓ 被模型引用
models/block/xxx.json     ← 模型文件（决定6个面分别用哪张贴图）
       ↓ 被 blockstate 引用
blockstates/xxx.json      ← 方块状态（决定什么方向显示哪个模型）
```

即：**texture → model → blockstate**。

---

## 九、中文翻译（zh_cn.json）

手动创建 `src/main/resources/assets/integratedindustry/lang/zh_cn.json`：

```json
{
  "block.integratedindustry.thermal_energy_generator": "热能发电机",
  "item.integratedindustry.some_item": "某物品",
  "itemGroup.integratedindustry": "集成工业"
}
```

键名规则：

| 类型 | 键名格式 |
|---|---|
| 方块 | `block.模组id.注册名` |
| 物品 | `item.模组id.注册名` |
| 创造模式标签页 | `itemGroup.模组id` 或 `creativetab.模组id.xxx` |
| 提示 | `tooltip.模组id.xxx` |
| 配置界面 | `模组id.configuration.xxx` |

---

## 十、自定义物品模型

```json
// assets/integratedindustry/models/items/wrench.json
{
  "parent": "items/handheld",
  "textures": {
    "layer0": "integratedindustry:items/wrench"
  }
}
```

常用 parent：

| parent | 说明 |
|---|---|
| `item/generated` | 普通平面物品（食物、材料） |
| `item/handheld` | 工具类（镐、剑） |
| `block/xxx` | 方块物品，直接引用方块模型 |

---

## 十一、自定义方块类

不满足于 `RotatedPillarBlock` 时需自己写方块类：

```java
// 继承 RotatedPillarBlock 加自定义行为
public class ThermalGeneratorBlock extends RotatedPillarBlock {
    public ThermalGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level,
            BlockPos pos, Player player, BlockHitResult hitResult) {
        // 右键交互逻辑
        return InteractionResult.sucess(level.isClientSide);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos,
            BlockState newState, boolean movedByPiston) {
        // 方块被破坏时的逻辑
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
```

注册时把 `RotatedPillarBlock::new` 换成自己的类：

```java
.block("thermal_energy_generator", ThermalGeneratorBlock::new)
```

---

## 十二、配方注册 Recipe

CreateRegistrate 自带配方数据生成：

```java
static {
    REGISTRATE.addDataGenerator(ProviderType.RECIPE, provider -> {
        // 有序合成
        provider.shaped(RecipeCategory.MISC, ModBlocks.THERMAL_ENERGY_GENERATOR.get())
            .pattern("ABA")
            .pattern("BCB")
            .pattern("ABA")
            .define('A', Items.IRON_INGOT)
            .define('B', Items.COBBLESTONE)
            .define('C', Items.REDSTONE)
            .unlockedBy("has_iron", provider.hasItem(Items.IRON_INGOT))
            .save(provider);

        // 无序合成
        provider.shapeless(RecipeCategory.MISC, Items.DIAMOND)
            .requires(Items.COBBLESTONE, 9)
            .unlockedBy("has_cobble", provider.hasItem(Items.COBBLESTONE))
            .save(provider);

        // 熔炉配方
        provider.smelting(RecipeCategory.MISC, Items.IRON_INGOT, Items.COBBLESTONE, 0.1f);
    });
}
```

---

## 十三、创造模式标签页

用 REGISTRATE 设置默认标签页：

```java
static {
    REGISTRATE.defaultCreativeTab(CreativeModeTabs.COMBAT);
}
```

或者自定义标签页：

```java
public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MY_TAB =
    CREATIVE_MODE_TABS.register("my_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.integratedindustry"))
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .icon(() -> ModItems.SOME_ITEM.get().getDefaultInstance())
        .displayItems((params, output) -> {
            output.accept(ModBlocks.THERMAL_ENERGY_GENERATOR.get());
            // 手动添加其他物品
        })
        .build());
```

被 `REGISTRATE.defaultCreativeTab()` 设为默认时，通过 `.simpleItem()` 注册的物品会自动加入该标签页。

---

## 十四、数据生成总览

### 14.1 运行方式

```bash
./gradlew runData
```

生成到 `src/generated/resources/`，**不要手动编辑**该目录下的文件。

### 14.2 方法 → 生成文件对照表

每个 REGISTRATE 链式方法会生成什么文件：

| 链式方法 | 生成的数据类型 | 生成路径 |
|---|---|---|
| `.blockstate(...)` | blockstate JSON | `assets/模组id/blockstates/方块名.json` |
| `.simpleItem()` | 物品模型 JSON | `assets/模组id/models/item/方块名.json` |
| `.model(...)` | 物品模型 JSON | `assets/模组id/models/item/物品名.json` |
| `.lang("名称")` | 语言文件条目 | `assets/模组id/lang/en_us.json` |
| `.loot(...)` | 战利品表 JSON | `data/模组id/loot_table/blocks/方块名.json` |
| `.tag(BlockTags.XXX)` | 方块标签 JSON | `data/minecraft/tags/block/xxx.json` |
| `.tag(AllTags.AllBlockTags.XXX.tag)` | Create 标签 JSON | `data/create/tags/block/xxx.json` |
| `.transform(TagGen.xxx())` | 挖掘标签 JSON | `data/minecraft/tags/block/mineable/xxx.json` |

### 14.3 runData 执行后完整的输出结构

以 `thermal_energy_generator` 为例：

```
src/generated/resources/
├── assets/integratedindustry/
│   ├── blockstates/
│   │   └── thermal_energy_generator.json          ← .blockstate()
│   ├── models/
│   │   └── item/
│   │       └── thermal_energy_generator.json       ← .simpleItem()
│   └── lang/
│       ├── en_us.json                              ← .lang()
│       └── en_ud.json                              ← 自动反转测试用
├── data/
│   ├── integratedindustry/
│   │   └── loot_table/
│   │       └── blocks/
│   │           └── thermal_energy_generator.json   ← .loot()
│   ├── create/tags/block/
│   │   └── valve_handles.json                      ← .tag(Create标签)
│   └── minecraft/tags/block/
│       ├── mineable/
│       │   └── axe.json                            ← .transform(TagGen)
│       └── needs_stone_tool.json                   ← .tag(BlockTags)
└── .cache/                                         ← 缓存，不用管
```

### 14.4 每类数据生成详解

#### BlockState（`assets/.../blockstates/方块名.json`）

由 `.blockstate()` 生成。描述方块的每个状态变体对应哪个模型文件。

**`BlockStateGen.axisBlockProvider(true)` 的输出示例**：
```json
{
  "variants": {
    "axis=y":  { "model": "integratedindustry:block/thermal_energy_generator/block" },
    "axis=x":  { "model": "integratedindustry:block/thermal_energy_generator/block", "x": 90, "y": 90 },
    "axis=z":  { "model": "integratedindustry:block/thermal_energy_generator/block", "x": 90 }
  }
}
```

**`prov.axisBlock(ctx.get(), ...)` 的输出示例**：
```json
{
  "variants": {
    "axis=y":  { "model": "integratedindustry:block/thermal_energy_generator" },
    "axis=x":  { "model": "integratedindustry:block/thermal_energy_generator_horizontal", "x": 90, "y": 90 },
    "axis=z":  { "model": "integratedindustry:block/thermal_energy_generator_horizontal", "x": 90 }
  }
}
```

#### 物品模型（`assets/.../models/item/方块名.json`）

由 `.simpleItem()` 生成。方块在背包中显示的模型。

```json
{
  "parent": "integratedindustry:block/thermal_energy_generator"
}
```

所以它要求 `block/thermal_energy_generator.json` 必须存在——要么手动放，要么由 `prov.axisBlock()` 自动生成。

#### 战利品表（`data/.../loot_table/blocks/方块名.json`）

由 `.loot()` 生成。控制方块被破坏时掉落什么。

```json
{
  "type": "minecraft:block",
  "pools": [{
    "rolls": 1.0,
    "entries": [{ "type": "minecraft:items", "name": "minecraft:birch_wood" }]
  }]
}
```

#### 语言文件（`assets/.../lang/en_us.json`）

由 `.lang()` 生成。所有 `.lang()` 调用合并到一个 `en_us.json` 中。

```json
{
  "block.integratedindustry.thermal_energy_generator": "Thremal energy generator"
}
```

#### 标签（`data/.../tags/block/xxx.json`）

由 `.tag()` 和 `.transform(TagGen)` 生成。

```json
// data/minecraft/tags/block/mineable/axe.json
{
  "replace": false,
  "values": [ "integratedindustry:thermal_energy_generator" ]
}
```

#### 配方（`data/.../recipe/xxx.json`）

由 `REGISTRATE.addDataGenerator(ProviderType.RECIPE, ...)` 生成。

```
src/generated/resources/data/integratedindustry/recipe/方块名.json
```

### 14.5 main/resources vs generated/resources 职责划分

| | `src/main/resources/` | `src/generated/resources/` |
|---|---|---|
| 由谁管理 | **你手动** | **runData 自动** |
| 存放内容 | 贴图 PNG、手动模型 JSON、zh_cn.json | blockstate、loot、lang、tag、配方等 |
| 能否被覆盖 | 不会（你要主动改） | **每次 runData 全部重写** |
| 运行时合并 | 两个目录最终合并到同一个 classpath 下 | |

**注意冲突**：如果同一个文件在两个目录中都存在，Gradle 会报错或取第一个。所以生成的文件（如 en_us.json）不要手动放在 `main/resources/` 里。

### 14.6 方块模型自动生成能力对比

| 使用的方法 | blockstate | 模型文件 | 是否需要手动放模型 |
|---|---|---|---|
| `BlockStateGen.axisBlockProvider(true)` | ✅ 生成到 generated | ❌ 不生成 | ✅ 需要放 `main/resources/` |
| `prov.axisBlock(ctx.get(), ...)` | ✅ 生成到 generated | ✅ 生成到 generated | ❌ 不需要 |

---

## 十五、常见问题

### 15.1 模型文件找不到
```
Model at xxx does not exist
```
→ 检查模型 JSON 路径是否正确，以及文件是否存在

### 15.2 纹理不显示/紫黑块
→ 纹理 PNG 路径错误或命名不对，检查 `textures/` 目录

### 15.3 runData 成功但 lang 文件没更新
→ 检查 `src/main/resources/lang/` 下是否有旧的同名文件，删掉

### 15.4 方块注册名报错
```
Non [a-z0-9/._-] character in path
```
→ 注册名只能用**小写字母**、数字、下划线、点、横线

### 15.5 方块在游戏里没有名字（显示 registry 名）
→ lang 文件缺失或没被加载，检查 en_us.json

### 15.6 generated 生成的文件 vs main/resources 冲突
```
Entry xxx is a duplicate but no duplicate handling strategy has been set
```
→ 删掉 `main/resources` 下对应的旧文件，保留 `generated/resources` 生成的版本
