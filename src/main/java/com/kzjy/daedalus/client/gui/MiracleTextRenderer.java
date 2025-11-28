package com.kzjy.daedalus.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.awt.Color;

/**
 * @author Kzjy<br>
 * 奇迹附魔文本渲染器<br>
 * 实现彩虹色渐变与动态抖动效果
 */
public class MiracleTextRenderer implements ClientTooltipComponent {
    private final FormattedCharSequence text;
    private final String rawString;

    // 构造函数接收数据类
    public MiracleTextRenderer(MiracleTooltipData data) {
        this.text = data.text().getVisualOrderText();
        this.rawString = data.text().getString();
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public int getWidth(Font font) {
        return font.width(text);
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
        float time = (System.currentTimeMillis() % 10000) / 50.0f;
        float currentX = x;

        for (int i = 0; i < rawString.length(); i++) {
            char c = rawString.charAt(i);
            String s = String.valueOf(c);
            int charWidth = font.width(s);

            if (c == ' ') {
                currentX += charWidth;
                continue;
            }

            // Moonstone 风格抖动算法
            float angle = (time * 5.0f + i * 15.0f) * ((float) Math.PI / 180.0f);
            float offsetX = Mth.sin(angle) * 1.0f;
            float offsetY = Mth.cos(angle) * 1.0f;

            // 奇迹彩虹色
            float hue = (time * 2.0f + i * 10.0f) % 360.0f / 360.0f;
            int color = Color.HSBtoRGB(hue, 0.7f, 1.0f);

            font.drawInBatch(s,
                    currentX + offsetX,
                    y + offsetY,
                    color,
                    true,
                    matrix,
                    bufferSource,
                    Font.DisplayMode.NORMAL,
                    0,
                    15728880);

            currentX += charWidth;
        }
    }
}
