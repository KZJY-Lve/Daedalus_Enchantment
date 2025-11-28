package com.kzjy.daedalus;

import com.kzjy.daedalus.client.model.DaedalusModelLoader; // 确保导入这个
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

@Mod.EventBusSubscriber(modid = Daedalus.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 注册物品属性覆写: daedalus:enchanted_book
            // 只有当这个属性返回 1.0 时，JSON 里的 overrides 才会生效
            ItemProperties.register(Items.ENCHANTED_BOOK,
                    new ResourceLocation(Daedalus.MODID, "enchanted_book"),
                    (stack, world, entity, seed) -> {
                        // 获取书上所有的附魔
                        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                        if (enchantments.isEmpty()) return 0.0f;

                        // [修复逻辑] 遍历书上所有附魔，只要包含任何一个代达罗斯附魔，就改变模型
                        for (Enchantment ench : enchantments.keySet()) {
                            if (ench instanceof DaedalusBaseEnchantment) {
                                return 1.0f; // 触发 override
                            }
                        }

                        return 0.0f; // 保持原版模型
                    });
        });
    }

    // [重要！！！] 这一步绝对不能少，少了这一步，JSON 里的 "loader": "daedalus:cosmic" 就是无效的
    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("cosmic", new DaedalusModelLoader());
    }
}
