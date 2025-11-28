package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * @author Kzjy<br>
 * 天使的加护附魔<br>
 * 赋予穿戴者天使庇佑效果，提供飞行能力与亡灵减伤
 */
public class AngelicProtectionEnchantment extends DaedalusBaseEnchantment {
    public AngelicProtectionEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.CHEST, EquipmentSlot.LEGS},
                EnchantmentTheme.DIVINE, false);
    }

    @Override
    public boolean isEnabled() {
        return DaedalusConfig.COMMON.angelicProtectionEnabled.get();
    }

    @Override
    public int getMaxLevel() {
        return isEnabled() ? DaedalusConfig.COMMON.angelicProtectionMaxLevel.get() : 0;
    }

    @Override
    public boolean isTradeable() {
        return isEnabled() && DaedalusConfig.COMMON.angelicProtectionTradeable.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return !isEnabled() || DaedalusConfig.COMMON.angelicProtectionTreasure.get();
    }

    @Override
    public boolean canEnchant(net.minecraft.world.item.ItemStack stack) {
        net.minecraft.world.item.Item item = stack.getItem();
        return canEnchant(item);
    }

    public boolean canEnchant(net.minecraft.world.item.Item item) {
        if(item instanceof net.minecraft.world.item.ArmorItem armorItem) {
            return armorItem.getEquipmentSlot() == EquipmentSlot.CHEST || armorItem.getEquipmentSlot() == EquipmentSlot.LEGS;
        }
        return false;
    }
}
