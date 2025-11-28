package com.kzjy.daedalus;

import com.kzjy.daedalus.client.model.DaedalusModelLoader;
import com.kzjy.daedalus.enchantment.DaedalusBaseEnchantment;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

/**
 * @author Kzjy<br>
 * 客户端初始化设置<br>
 * 处理物品属性覆写与模型加载器注册
 */
@Mod.EventBusSubscriber(modid = Daedalus.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    /**
     * 注册物品属性覆写<br>
     * 用于检测附魔书是否包含代达罗斯附魔，从而改变其模型
     */
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 只有当这个属性返回 1.0 时，JSON 里的 overrides 才会生效
            ItemProperties.register(Items.ENCHANTED_BOOK,
                    new ResourceLocation(Daedalus.MODID, "enchanted_book"),
                    (stack, world, entity, seed) -> {
                        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                        if (enchantments.isEmpty()) return 0.0f;

                        // 遍历书上所有附魔，只要包含任何一个代达罗斯附魔，就触发模型替换
                        for (Enchantment ench : enchantments.keySet()) {
                            if (ench instanceof DaedalusBaseEnchantment) {
                                return 1.0f; // 触发 override
                            }
                        }

                        return 0.0f; // 保持原版模型
                    });
        });
    }

    /**
     * 注册自定义几何模型加载器<br>
     * 这一步至关重要，否则 JSON 中的 "loader": "daedalus:cosmic" 将无法识别
     */
    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("cosmic", new DaedalusModelLoader());
    }
}
