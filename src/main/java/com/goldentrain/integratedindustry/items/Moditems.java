package com.goldentrain.integratedindustry.items;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static com.goldentrain.integratedindustry.IntegratedIndustry.REGISTRATE;
public class Moditems
{
    public static final ItemEntry<Item> FIRST_STONE = REGISTRATE
            .item("first_stone", Item::new)
            .properties(p -> p
                    .stacksTo(16)//堆叠限制
                    .fireResistant())//火焰抗性
            .model((ctx,prov) -> prov.generated(ctx::getEntry))//物品模型，直接用生成的
            .lang("first stone")//设置物品名称
            .register();

    public static final ItemEntry<Item> SECOND_STONE = REGISTRATE
            .item("second_stone", Item::new)
            .properties(p -> p
                    .stacksTo(88)//堆叠限制
                    .fireResistant())//火焰抗性
            .model((ctx,prov) -> prov.generated(ctx::getEntry))//物品模型，直接用生成的
            .lang("second stone")//设置物品名称
            .register();

    public static void register() {}
}