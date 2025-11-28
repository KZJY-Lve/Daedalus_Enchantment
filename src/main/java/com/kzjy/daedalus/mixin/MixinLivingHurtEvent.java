package com.kzjy.daedalus.mixin;

import com.kzjy.daedalus.duck.IDaedalusLivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kzjy<br>
 * 混入 LivingHurtEvent 类<br>
 * 用于拦截并强制执行“只能增伤”逻辑，保护真实伤害不被削减
 */
@Mixin(value = LivingHurtEvent.class, remap = false)
public abstract class MixinLivingHurtEvent {
    @Shadow(remap = false) public abstract float getAmount();

    @Inject(method = "setAmount", at = @At("HEAD"), cancellable = true, remap = false)
    private void daedalus$interceptSetAmount(float amount, CallbackInfo ci) {
        // 访问基类 LivingEvent 中的字段
        if (((IDaedalusLivingEvent) (Object) this).daedalus$isOnlyAmountUp()) {
            if (amount < this.getAmount()) {
                ci.cancel(); // 拒绝减小数值
            }
        }
    }
}
