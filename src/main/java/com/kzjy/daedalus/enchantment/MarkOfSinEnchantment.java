package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * @author Kzjy<br>
 * 罪孽印记附魔<br>
 * 攻击时施加基于最大生命值百分比扣血的负面效果
 */
public class MarkOfSinEnchantment extends DaedalusBaseEnchantment {
    public MarkOfSinEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND},
                EnchantmentTheme.SIN, false);
    }

    @Override
    public boolean isEnabled() {
        return DaedalusConfig.COMMON.markOfSinEnabled.get();
    }

    @Override
    public int getMaxLevel() {
        return isEnabled() ? DaedalusConfig.COMMON.markOfSinMaxLevel.get() : 0;
    }

    @Override
    public boolean isTradeable() {
        return isEnabled() && DaedalusConfig.COMMON.markOfSinTradeable.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return !isEnabled() || DaedalusConfig.COMMON.markOfSinTreasure.get();
    }
}
