package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * @author Kzjy<br>
 * 生命虹吸附魔<br>
 * 攻击时基于造成的伤害回复自身生命值
 */
public class LifeSiphonEnchantment extends DaedalusBaseEnchantment {
    public LifeSiphonEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND},
                EnchantmentTheme.NATURE, false);
    }

    @Override
    public boolean isEnabled() {
        return DaedalusConfig.COMMON.lifeSiphonEnabled.get();
    }

    @Override
    public int getMaxLevel() {
        return isEnabled() ? DaedalusConfig.COMMON.lifeSiphonMaxLevel.get() : 0;
    }

    @Override
    public boolean isTradeable() {
        return isEnabled() && DaedalusConfig.COMMON.lifeSiphonTradeable.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return !isEnabled() || DaedalusConfig.COMMON.lifeSiphonTreasure.get();
    }
}
