package com.kzjy.daedalus.mixin;

import com.kzjy.daedalus.duck.IDaedalusDamageSource;
import com.kzjy.daedalus.registry.DaedalusRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Kzjy<br>
 * 混入 LivingEntity 类<br>
 * 处理图腾拦截逻辑与天使加护的飞行能力
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    public MixinLivingEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow public abstract boolean hasEffect(net.minecraft.world.effect.MobEffect p_21024_);
    @Shadow public abstract MobEffectInstance getEffect(net.minecraft.world.effect.MobEffect p_21025_);

    /**
     * 拦截图腾触发<br>
     * 当伤害源具有 BypassAll 属性（如耀星之噬、虚空破壁）时，阻止不死图腾生效
     */
    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void daedalus$blockTotems(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (source instanceof IDaedalusDamageSource ds && ds.daedalus$isBypassAll()) {
            cir.setReturnValue(false);
        }
    }

    /**
     * 天使的加护：创造飞行逻辑<br>
     * 在生物 tick 更新时，若玩家拥有足够等级的天使庇佑，赋予其飞行能力
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void daedalus$angelFlight(CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self instanceof net.minecraft.world.entity.player.Player player) {
            if (player.hasEffect(DaedalusRegistries.ANGELIC_PROTECTION.get())) {
                int amp = player.getEffect(DaedalusRegistries.ANGELIC_PROTECTION.get()).getAmplifier();
                if ((amp + 1) >= 4) {
                    if (!player.getAbilities().mayfly) {
                        player.getAbilities().mayfly = true;
                        player.onUpdateAbilities();
                    }
                }
            }
        }
    }
}
