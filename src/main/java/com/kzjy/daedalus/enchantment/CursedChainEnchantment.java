package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * @author Kzjy<br>
 * 诅咒锁链附魔<br>
 * 降低自身造成的伤害，但附加基于原始伤害百分比的诅咒伤害
 */
public class CursedChainEnchantment extends DaedalusBaseEnchantment {
    public CursedChainEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND},
                EnchantmentTheme.CURSE, false); // 不是真正的诅咒附魔(指isCurse=false)，只是主题是诅咒
    }

    @Override
    public boolean isEnabled() {
        return DaedalusConfig.COMMON.cursedChainEnabled.get();
    }

    @Override
    public int getMaxLevel() {
        return isEnabled() ? DaedalusConfig.COMMON.cursedChainMaxLevel.get() : 0;
    }

    @Override
    public boolean isTradeable() {
        return isEnabled() && DaedalusConfig.COMMON.cursedChainTradeable.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return !isEnabled() || DaedalusConfig.COMMON.cursedChainTreasure.get();
    }
}
