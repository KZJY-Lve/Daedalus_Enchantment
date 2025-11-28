package com.kzjy.daedalus.mixin;

import com.kzjy.daedalus.duck.IDaedalusLivingEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author Kzjy<br>
 * 混入 LivingEvent 类以扩展事件控制能力<br>
 * 实现了 IDaedalusLivingEvent 接口，允许强制事件不可取消或锁定数值增长
 */
@Mixin(LivingEvent.class)
public abstract class MixinLivingEvent extends EntityEvent implements IDaedalusLivingEvent {
    @Unique private boolean daedalus$uncancelable = false;
    @Unique private boolean daedalus$onlyAmountUp = false;

    public MixinLivingEvent(Entity entity) {
        super(entity);
    }

    @Override
    public void daedalus$setUncancelable(boolean uncancelable) {
        this.daedalus$uncancelable = uncancelable;
    }

    @Override
    public boolean daedalus$isUncancelable() {
        return this.daedalus$uncancelable;
    }

    @Override
    public void daedalus$setOnlyAmountUp(boolean onlyUp) {
        this.daedalus$onlyAmountUp = onlyUp;
    }

    @Override
    public boolean daedalus$isOnlyAmountUp() {
        return this.daedalus$onlyAmountUp;
    }

    /**
     * 重写 setCanceled 方法<br>
     * 当 daedalus$uncancelable 为 true 时，拒绝任何取消操作
     */
    @Override
    public void setCanceled(boolean cancel) {
        if (this.daedalus$uncancelable && cancel) {
            return; // 拒绝取消
        }
        super.setCanceled(cancel);
    }

    /**
     * 重写 isCanceled 方法<br>
     * 当 daedalus$uncancelable 为 true 时，强制返回 false
     */
    @Override
    public boolean isCanceled() {
        if (this.daedalus$uncancelable) {
            return false; // 强制伪造未取消状态
        }
        return super.isCanceled();
    }
}
