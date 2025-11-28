package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * @author Kzjy<br>
 * 代达罗斯之弓附魔<br>
 * 显著提升弓箭、弩及三叉戟的蓄力速度
 */
public class DaedalusBowEnchantment extends DaedalusBaseEnchantment {
    public DaedalusBowEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND},
                EnchantmentTheme.DAEDALUS, false);
    }

    @Override
    public boolean isEnabled() {
        return DaedalusConfig.COMMON.daedalusBowEnabled.get();
    }

    @Override
    public int getMaxLevel() {
        return isEnabled() ? DaedalusConfig.COMMON.daedalusBowMaxLevel.get() : 0;
    }

    @Override
    public boolean isTradeable() {
        return isEnabled() && DaedalusConfig.COMMON.daedalusBowTradeable.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return !isEnabled() || DaedalusConfig.COMMON.daedalusBowTreasure.get();
    }
}
