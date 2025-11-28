package com.kzjy.daedalus.client.gui;

import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

/**
 * @author Kzjy<br>
 * 代达罗斯通用 Tooltip 数据载体<br>
 * 包含文本内容与附魔主题信息
 */
public record DaedalusTooltipData(Component text, EnchantmentTheme theme) implements TooltipComponent {
}
