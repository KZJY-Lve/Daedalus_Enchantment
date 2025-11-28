package com.kzjy.daedalus.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class MarkOfSinEffect extends MobEffect {
    public MarkOfSinEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B0000);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return;
        float maxHealth = entity.getMaxHealth();
        float basePercent = 0.05f;
        float damageAmount = maxHealth * (basePercent + (amplifier * 0.01f));

        // 随机伤害类型兼容
        DamageSource source = entity.damageSources().magic(); // 默认魔法，可扩展随机逻辑
        entity.hurt(source, damageAmount);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
