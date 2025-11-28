package com.kzjy.daedalus.mixin;

import com.kzjy.daedalus.duck.IDaedalusLivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kzjy<br>
 * 混入 LivingHealEvent 类<br>
 * 用于拦截并强制执行“只能增疗”逻辑，防止其他模组削减特定情况下的治疗量
 */
@Mixin(value = LivingHealEvent.class, remap = false)
public abstract class MixinLivingHealEvent {

    @Shadow(remap = false)
    public abstract float getAmount();

    @Inject(method = "setAmount", at = @At("HEAD"), cancellable = true, remap = false)
    private void daedalus$interceptSetAmount(float amount, CallbackInfo ci) {
        // 核心逻辑：如果开启了“只能增伤/增疗”模式
        if (((IDaedalusLivingEvent) (Object) this).daedalus$isOnlyAmountUp()) {
            // 如果试图减少治疗量 (新值 < 旧值)
            if (amount < this.getAmount()) {
                // 拒绝执行
                ci.cancel();
            }
        }
    }
}
