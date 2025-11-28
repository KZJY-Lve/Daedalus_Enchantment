package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * @author Kzjy<br>
 * 神圣裁决附魔<br>
 * 对亡灵生物造成巨额伤害并有几率治愈僵尸村民
 */
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
        return 0.0f; // 实际伤害计算逻辑在 Event 中处理以实现百分比加成
    }
}
