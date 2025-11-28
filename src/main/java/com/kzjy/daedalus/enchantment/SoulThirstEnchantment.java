package com.kzjy.daedalus.enchantment;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.enchantment.util.EnchantmentTheme;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Kzjy<br>
 * 灵魂饥渴附魔<br>
 * 击杀生物收集灵魂，满魂状态下造成巨额爆发伤害
 */
public class SoulThirstEnchantment extends DaedalusBaseEnchantment {
    public SoulThirstEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND},
                EnchantmentTheme.SIN, false);
    }

    @Override
    public boolean isEnabled() { return DaedalusConfig.COMMON.soulThirstEnabled.get(); }
    @Override
    public int getMaxLevel() { return isEnabled() ? DaedalusConfig.COMMON.soulThirstMaxLevel.get() : 0; }
    @Override
    public boolean isTradeable() { return isEnabled() && DaedalusConfig.COMMON.soulThirstTradeable.get(); }
    @Override
    public boolean isTreasureOnly() { return !isEnabled() || DaedalusConfig.COMMON.soulThirstTreasure.get(); }

    /**
     * 在物品提示框中显示当前收集的灵魂数量
     */
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (stack.hasTag() && stack.getTag().contains("DaedalusSouls")) {
            int souls = stack.getTag().getInt("DaedalusSouls");
            int max = DaedalusConfig.COMMON.soulThirstMaxThreshold.get();
            tooltip.add(Component.literal("Souls: " + souls + "/" + max).withStyle(ChatFormatting.DARK_RED));
        }
    }
}
