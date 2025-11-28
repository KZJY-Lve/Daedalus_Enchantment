package com.kzjy.daedalus.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

/**
 * 这是一个数据载体，用于在服务端/逻辑端传递 Tooltip 信息。
 * 它实现了 TooltipComponent 接口，所以可以被放入 GatherComponents 事件的列表中。
 */
public record MiracleTooltipData(Component text) implements TooltipComponent {
}
