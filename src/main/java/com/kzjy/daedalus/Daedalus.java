package com.kzjy.daedalus;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.registry.DaedalusRegistries;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * @author Kzjy<br>
 * 代达罗斯模组主类<br>
 * 负责模组初始化、注册表加载及事件总线注册
 */
@Mod(Daedalus.MODID)
public class Daedalus {
    public static final String MODID = "daedalus";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Daedalus() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        DaedalusRegistries.ITEMS.register(modEventBus);
        DaedalusRegistries.ENCHANTMENTS.register(modEventBus);
        DaedalusRegistries.MOB_EFFECTS.register(modEventBus);
        DaedalusRegistries.CREATIVE_MODE_TABS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DaedalusConfig.COMMON_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
