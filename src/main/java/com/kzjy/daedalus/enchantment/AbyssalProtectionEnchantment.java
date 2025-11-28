package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * @author Kzjy<br>
 * 深渊的加护附魔<br>
 * 赋予穿戴者深渊庇护效果，提供减伤与闪避
 */
public class AbyssalProtectionEnchantment extends DaedalusBaseEnchantment {
    public AbyssalProtectionEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.CHEST},
                EnchantmentTheme.ABYSS, false);
    }

    @Override
    public boolean isEnabled() {
        return DaedalusConfig.COMMON.abyssalProtectionEnabled.get();
    }

    @Override
    public int getMaxLevel() {
        return isEnabled() ? DaedalusConfig.COMMON.abyssalProtectionMaxLevel.get() : 0;
    }

    @Override
    public boolean isTradeable() {
        return isEnabled() && DaedalusConfig.COMMON.abyssalProtectionTradeable.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return !isEnabled() || DaedalusConfig.COMMON.abyssalProtectionTreasure.get();
    }
}
