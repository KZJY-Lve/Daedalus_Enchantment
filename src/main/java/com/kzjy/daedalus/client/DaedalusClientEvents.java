package com.kzjy.daedalus.client;

import com.kzjy.daedalus.Daedalus;
import com.kzjy.daedalus.client.gui.DaedalusTextRenderer;
import com.kzjy.daedalus.client.gui.DaedalusTooltipData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author Kzjy<br>
 * 客户端事件注册类<br>
 * 注册自定义 Tooltip 组件工厂
 */
@Mod.EventBusSubscriber(modid = Daedalus.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DaedalusClientEvents {

    @SubscribeEvent
    public static void registerTooltipFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(DaedalusTooltipData.class, DaedalusTextRenderer::new);
    }
}
