package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * @author Kzjy<br>
 * 耀星之噬附魔<br>
 * 造成绝对真实伤害，无视一切减免、无敌与复活机制
 */
public class StellarEaterEnchantment extends DaedalusBaseEnchantment {
    public StellarEaterEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND},
                EnchantmentTheme.MIRACLE, false);
    }

    @Override
    public boolean isEnabled() { return DaedalusConfig.COMMON.stellarEaterEnabled.get(); }
    @Override
    public int getMaxLevel() { return 1; }
    @Override
    public boolean isTradeable() { return isEnabled() && DaedalusConfig.COMMON.stellarEaterTradeable.get(); }
    @Override
    public boolean isTreasureOnly() { return !isEnabled() || DaedalusConfig.COMMON.stellarEaterTreasure.get(); }
}
