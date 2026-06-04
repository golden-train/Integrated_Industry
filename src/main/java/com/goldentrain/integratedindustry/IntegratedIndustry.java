package com.goldentrain.integratedindustry;

import com.goldentrain.integratedindustry.blocks.Modblocks;
import com.goldentrain.integratedindustry.items.ModCreativeTabs;
import com.goldentrain.integratedindustry.items.Moditems;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(IntegratedIndustry.MODID)
public class IntegratedIndustry {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "integratedindustry";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);//声明，准备使用Registrate系统
    static {
        REGISTRATE.defaultCreativeTab(CreativeModeTabs.COMBAT);
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }//这里是增加机械动力附属信息注册的，不想写就可以不要
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "integratedindustry" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public IntegratedIndustry(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModCreativeTabs.register(modEventBus);
        REGISTRATE.registerEventListeners(modEventBus);
        Moditems.register();//注册物品
        Modblocks.register();//注册方块
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Integrated Industry server starting");
    }
}
