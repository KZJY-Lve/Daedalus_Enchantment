package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * @author Kzjy<br>
 * 虚空破壁附魔<br>
 * 赋予攻击无视无敌帧与伤害上限的能力
 */
public class VoidBreachEnchantment extends DaedalusBaseEnchantment {
    public VoidBreachEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND},
                EnchantmentTheme.VOID, false);
    }

    @Override
    public boolean isEnabled() {
        return DaedalusConfig.COMMON.voidBreachEnabled.get();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isTradeable() {
        return isEnabled() && DaedalusConfig.COMMON.voidBreachTradeable.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return !isEnabled() || DaedalusConfig.COMMON.voidBreachTreasure.get();
    }
}
