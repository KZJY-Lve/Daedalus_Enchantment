package com.kzjy.daedalus.mixin;

import com.kzjy.daedalus.config.DaedalusConfig;
import com.kzjy.daedalus.duck.IDaedalusDamageSource;
import com.kzjy.daedalus.registry.DaedalusRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSources.class)
public class MixinDamageSources {

    @Inject(method = "playerAttack", at = @At("RETURN"))
    private void daedalus$modifyPlayerAttack(Player player, CallbackInfoReturnable<DamageSource> cir) {
        DamageSource source = cir.getReturnValue();
        if (source instanceof IDaedalusDamageSource ds) {
            ItemStack stack = player.getMainHandItem();

            // 耀星之噬：赋予 BypassAll (穿透 Cataclysm 的无敌和减伤)
            if (DaedalusConfig.COMMON.stellarEaterEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.STELLAR_EATER.get(), stack) > 0) {
                ds.daedalus$setBypassAll(true);
            }

            // 虚空破壁：赋予 VoidBreach
            if (DaedalusConfig.COMMON.voidBreachEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.VOID_BREACH.get(), stack) > 0) {
                ds.daedalus$setVoidBreach(true);
            }
        }
    }

    @Inject(method = "mobAttack", at = @At("RETURN"))
    private void daedalus$modifyMobAttack(LivingEntity attacker, CallbackInfoReturnable<DamageSource> cir) {
        DamageSource source = cir.getReturnValue();
        if (source instanceof IDaedalusDamageSource ds) {
            ItemStack stack = attacker.getMainHandItem();

            if (DaedalusConfig.COMMON.stellarEaterEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.STELLAR_EATER.get(), stack) > 0) {
                ds.daedalus$setBypassAll(true);
            }

            if (DaedalusConfig.COMMON.voidBreachEnabled.get() &&
                    EnchantmentHelper.getItemEnchantmentLevel(DaedalusRegistries.VOID_BREACH.get(), stack) > 0) {
                ds.daedalus$setVoidBreach(true);
            }
        }
    }
}
