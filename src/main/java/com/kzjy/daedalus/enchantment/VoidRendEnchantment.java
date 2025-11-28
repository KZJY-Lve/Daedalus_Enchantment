package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * @author Kzjy<br>
 * 虚空撕裂附魔<br>
 * 造成基于攻击力的真实伤害，并有几率触发诸神黄昏斩杀低血量目标
 */
public class VoidRendEnchantment extends DaedalusBaseEnchantment {
    public VoidRendEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND},
                EnchantmentTheme.VOID, false);
    }

    @Override
    public boolean isEnabled() {
        return DaedalusConfig.COMMON.voidRendEnabled.get();
    }

    @Override
    public int getMaxLevel() {
        return isEnabled() ? DaedalusConfig.COMMON.voidRendMaxLevel.get() : 0;
    }

    @Override
    public boolean isTradeable() {
        return isEnabled() && DaedalusConfig.COMMON.voidRendTradeable.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return !isEnabled() || DaedalusConfig.COMMON.voidRendTreasure.get();
    }
}
