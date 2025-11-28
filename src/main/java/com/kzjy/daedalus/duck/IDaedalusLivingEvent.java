package com.kzjy.daedalus.duck;

/**
 * @author Kzjy<br>
 * 鸭子接口，用于扩展 LivingEvent 的功能<br>
 * 允许设置事件为不可取消，或锁定数值只能增长
 */
public interface IDaedalusLivingEvent {
    void daedalus$setUncancelable(boolean uncancelable);
    boolean daedalus$isUncancelable();

    void daedalus$setOnlyAmountUp(boolean onlyUp);
    boolean daedalus$isOnlyAmountUp();
}
