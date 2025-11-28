package com.kzjy.daedalus.mixin;

import com.kzjy.daedalus.duck.IDaedalusLivingEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kzjy<br>
 * 混入 LivingDamageEvent 类<br>
 * 用于拦截并强制执行“只能增伤”逻辑，在最终伤害结算阶段保护数值
 */
@Mixin(value = LivingDamageEvent.class, remap = false)
public abstract class MixinLivingDamageEvent {
    @Shadow(remap = false) public abstract float getAmount();

    @Inject(method = "setAmount", at = @At("HEAD"), cancellable = true, remap = false)
    private void daedalus$interceptSetAmount(float amount, CallbackInfo ci) {
        if (((IDaedalusLivingEvent) (Object) this).daedalus$isOnlyAmountUp()) {
            if (amount < this.getAmount()) {
                ci.cancel(); // 拒绝减小数值
            }
        }
    }
}
