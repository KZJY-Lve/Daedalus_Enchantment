package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class DivineJudgmentEnchantment extends DaedalusBaseEnchantment {
    public DivineJudgmentEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND},
                EnchantmentTheme.DIVINE, false);
    }

    @Override
    public boolean isEnabled() {
        return DaedalusConfig.COMMON.divineJudgmentEnabled.get();
    }

    @Override
    public int getMaxLevel() {
        return isEnabled() ? DaedalusConfig.COMMON.divineJudgmentMaxLevel.get() : 0;
    }

    @Override
    public boolean isTradeable() {
        return isEnabled() && DaedalusConfig.COMMON.divineJudgmentTradeable.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return !isEnabled() || DaedalusConfig.COMMON.divineJudgmentTreasure.get();
    }

    @Override
    public float getDamageBonus(int level, MobType type) {
        return 0.0f; // 伤害逻辑在 Event 中处理
    }
}
