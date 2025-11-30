package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * @author Kzjy<br>
 * 深渊凝视附魔<br>
 * 效果: 视线接触施加深渊诅咒<br>
 */
public class AbyssalGazeEnchantment extends DaedalusBaseEnchantment {
    public AbyssalGazeEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD},
                EnchantmentTheme.ABYSS, false);
    }

    @Override
    public boolean isEnabled() {
        return DaedalusConfig.COMMON.abyssalGazeEnabled.get();
    }

    @Override
    public int getMaxLevel() {
        return isEnabled() ? DaedalusConfig.COMMON.abyssalGazeMaxLevel.get() : 0;
    }

    @Override
    public boolean isTradeable() {
        return isEnabled() && DaedalusConfig.COMMON.abyssalGazeTradeable.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return !isEnabled() || DaedalusConfig.COMMON.abyssalGazeTreasure.get();
    }
}
