package com.kzjy.daedalus.client;

import com.kzjy.daedalus.Daedalus;
import com.kzjy.daedalus.client.gui.DaedalusTooltipData;
import com.kzjy.daedalus.enchantment.DaedalusBaseEnchantment;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Daedalus.MODID, value = Dist.CLIENT)
public class DaedalusForgeClientEvents {

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

        for (Enchantment ench : enchantments.keySet()) {
            if (ench instanceof DaedalusBaseEnchantment daedalusEnch) {
                // [修改] 不再限制为 MIRACLE，所有 Daedalus 附魔都应用
                String enchName = Component.translatable(ench.getDescriptionId()).getString();

                for (int i = 0; i < event.getTooltipElements().size(); i++) {
                    Either<FormattedText, TooltipComponent> element = event.getTooltipElements().get(i);

                    if (element.left().isPresent()) {
                        FormattedText text = element.left().get();
                        String stringText = text.getString();

                        if (stringText.contains(enchName)) {
                            Component fullComponent = Component.literal(stringText);
                            // 传入 Theme 以便渲染器知道用什么颜色
                            event.getTooltipElements().set(i, Either.right(new DaedalusTooltipData(fullComponent, daedalusEnch.getTheme())));
                        }
                    }
                }
            }
        }
    }
}
