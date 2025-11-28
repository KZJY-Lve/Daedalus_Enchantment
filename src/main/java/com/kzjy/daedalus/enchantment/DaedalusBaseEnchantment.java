package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import javax.annotation.Nonnull;

/**
 * @author Kzjy<br>
 * 代达罗斯附魔基类<br>
 * 统一处理附魔的主题颜色、名称显示及启用状态检查
 */
public abstract class DaedalusBaseEnchantment extends Enchantment {

    private final EnchantmentTheme theme;
    private final boolean isCurse;

    protected DaedalusBaseEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots,
                                      EnchantmentTheme theme, boolean isCurse) {
        super(rarity, category, slots);
        this.theme = theme;
        this.isCurse = isCurse;
    }

    public EnchantmentTheme getTheme() {
        return theme;
    }

    @Override
    public boolean isCurse() {
        return isCurse;
    }

    public abstract boolean isEnabled();

    @Override
    @Nonnull
    public Component getFullname(int level) {
        MutableComponent name = Component.translatable(this.getDescriptionId());

        if (!isEnabled()) {
            return name.withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.STRIKETHROUGH);
        }

        if (isCurse) {
            name.withStyle(ChatFormatting.RED);
        } else {
            // 恢复为简单的静态颜色，不再有流光和抖动
            name.withStyle(theme.getChatColor());
        }

        if (level != 1 || this.getMaxLevel() != 1) {
            name.append(" ").append(Component.translatable("enchantment.level." + level));
        }

        return name;
    }

    public Component getDescription() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }

    @Override
    public boolean checkCompatibility(@Nonnull Enchantment other) {
        if (this.isCurse() && other.isCurse()) {
            return false;
        }
        return super.checkCompatibility(other);
    }
}
