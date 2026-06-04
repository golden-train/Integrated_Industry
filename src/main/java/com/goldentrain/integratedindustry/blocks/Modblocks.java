package com.goldentrain.integratedindustry.blocks;


import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;

import static com.goldentrain.integratedindustry.IntegratedIndustry.REGISTRATE;
public class Modblocks {
    public static final BlockEntry<RotatedPillarBlock> THERMAL_ENERGY_GENERATOR = REGISTRATE
            .block("thermal_energy_generator",RotatedPillarBlock::new)
            .initialProperties(()-> Blocks.OAK_WOOD)//继承方块的属性
            .properties(p -> p.sound(SoundType.HARD_CROP))//覆盖已有属性，比如这里把声音替换成了硬作物
            .blockstate(BlockStateGen.axisBlockProvider(true))//机械动力的朝向写法
            .transform(TagGen.axeOrPickaxe())
            .tag(BlockTags.NEEDS_STONE_TOOL)//挖掘的相关tags
            .tag(AllTags.AllBlockTags.VALVE_HANDLES.tag)//机械动力相关标签,这个是需要阀门把手
            .lang("Thremal energy generator")//
            .loot((p, b) -> p.dropOther(b, Items.BIRCH_WOOD))//战利品，掉落的挖掘物
            .simpleItem()
            .register();

    public static final BlockEntry<RotatedPillarBlock> YUAN_STONE = REGISTRATE
            .block("yuan_stone",RotatedPillarBlock::new)
            .initialProperties(()-> Blocks.STONE)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get()))
            .transform(TagGen.pickaxeOnly())
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .loot(RegistrateBlockLootTables::dropSelf)
            .lang("yuan stone")
            .simpleItem()
            .register();

    public static void register() {
    }
}
