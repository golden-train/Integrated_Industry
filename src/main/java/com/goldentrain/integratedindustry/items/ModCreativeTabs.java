package com.goldentrain.integratedindustry.items;

import com.goldentrain.integratedindustry.IntegratedIndustry;
import com.goldentrain.integratedindustry.blocks.Modblocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB,IntegratedIndustry.MODID);

    public static final Supplier<CreativeModeTab> INTEGRATED_INDUSTRY_TAB =
            CREATIVE_MODE_TABS.register("integrated_industry_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(Modblocks.YUAN_STONE.get()))
                    .title(Component.translatable("itemgroup.integrated_industry_tab"))
                    .displayItems((context, output) -> {
                        output.accept(Modblocks.YUAN_STONE);
                        output.accept(Modblocks.THERMAL_ENERGY_GENERATOR);
                        output.accept(Moditems.FIRST_STONE);
                        output.accept(Moditems.SECOND_STONE);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
