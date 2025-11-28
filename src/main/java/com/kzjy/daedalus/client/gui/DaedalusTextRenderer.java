package com.kzjy.daedalus.client.gui;

import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.awt.Color;

public class DaedalusTextRenderer implements ClientTooltipComponent {
    private final FormattedCharSequence text;
    private final String rawString;
    private final EnchantmentTheme theme;

    public DaedalusTextRenderer(DaedalusTooltipData data) {
        this.text = data.text().getVisualOrderText();
        this.rawString = data.text().getString();
        this.theme = data.theme();
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
        double time = System.currentTimeMillis() / 1000.0;
        float currentX = x;

        for (int i = 0; i < rawString.length(); i++) {
            char c = rawString.charAt(i);
            String s = String.valueOf(c);
            int charWidth = font.width(s);

            if (c == ' ') {
                currentX += charWidth;
                continue;
            }

            // 动画参数
            float angle = (float) ((time * 3.0 + i * 0.3) % (Math.PI * 2));
            float offsetX = Mth.sin(angle) * 0.65f;
            float offsetY = Mth.cos(angle) * 0.65f;

            // 颜色计算
            int color;
            if (theme == EnchantmentTheme.MIRACLE) {
                float hue = (float) ((time * 0.2 + i * 0.05) % 1.0);
                color = Color.HSBtoRGB(hue, 0.6f, 1.0f);
            } else {
                double colorT = (Math.sin(time * 2.0 - i * 0.1) + 1.0) / 2.0;
                color = lerpColor(theme.getPrimaryColor(), theme.getSecondaryColor(), (float) colorT);
            }

            // 奇迹附魔：重影效果
            if (theme == EnchantmentTheme.MIRACLE) {
                int ghostColor = (color & 0x00FFFFFF) | 0x44000000;
                font.drawInBatch(s, currentX + offsetX + 0.5f, y + offsetY + 0.5f, ghostColor, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            }

            // 主体文字
            font.drawInBatch(s, currentX + offsetX, y + offsetY, color, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

            currentX += charWidth;
        }
    }

    private int lerpColor(int color1, int color2, float t) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;
        int r = (int) (r1 + (r2 - r1) * t);
        int g = (int) (g1 + (g2 - g1) * t);
        int b = (int) (b1 + (b2 - b1) * t);
        return (r << 16) | (g << 8) | b;
    }
}
