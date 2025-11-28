package com.kzjy.daedalus.client;

import com.kzjy.daedalus.Daedalus;
import com.kzjy.daedalus.client.gui.DaedalusTooltipData;
import com.kzjy.daedalus.enchantment.DaedalusBaseEnchantment;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
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

/**
 * @author Kzjy<br>
 * 客户端 Tooltip 渲染事件处理<br>
 * 拦截附魔书的 Tooltip，将其替换为自定义的动态渲染组件
 */
@Mod.EventBusSubscriber(modid = Daedalus.MODID, value = Dist.CLIENT)
public class DaedalusForgeClientEvents {

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

        for (Enchantment ench : enchantments.keySet()) {
            if (ench instanceof DaedalusBaseEnchantment daedalusEnch) {
                // 1. 获取附魔的基础名称 (不带等级，例如 "虚空撕裂")
                String baseName = Component.translatable(ench.getDescriptionId()).getString();

                // 2. 获取语言文件中的完整描述文本
                String descKey = ench.getDescriptionId() + ".desc";
                String fullDesc = "";
                if (I18n.exists(descKey)) {
                    fullDesc = I18n.get(descKey);
                }

                for (int i = 0; i < event.getTooltipElements().size(); i++) {
                    Either<FormattedText, TooltipComponent> element = event.getTooltipElements().get(i);

                    if (element.left().isPresent()) {
                        FormattedText text = element.left().get();
                        String lineText = text.getString();

                        // [判定逻辑 A]：是附魔标题
                        // 只要包含附魔的基础名称（如 "虚空撕裂"），就认为是标题
                        // 这能覆盖 "虚空撕裂 I", "虚空撕裂 V" 等情况
                        boolean isTitle = lineText.contains(baseName);

                        // [判定逻辑 B]：是附魔描述
                        // 1. 描述不为空
                        // 2. 这行字不是标题
                        // 3. 完整的描述文本包含这行字 (处理自动换行)
                        // 4. 这行字长度大于 4 (防止匹配到 "Shift", "Ctrl" 或空行)
                        boolean isDescription = !fullDesc.isEmpty()
                                && !isTitle
                                && fullDesc.contains(lineText.trim())
                                && lineText.trim().length() > 4;

                        if (isTitle || isDescription) {
                            // 转换为自定义渲染组件
                            Component fullComponent = Component.literal(lineText);
                            event.getTooltipElements().set(i, Either.right(new DaedalusTooltipData(fullComponent, daedalusEnch.getTheme())));
                        }
                    }
                }
            }
        }
    }
}
