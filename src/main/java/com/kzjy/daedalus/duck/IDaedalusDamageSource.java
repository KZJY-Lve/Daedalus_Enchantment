package com.kzjy.daedalus.duck;

/**
 * @author Kzjy<br>
 * 鸭子接口，用于扩展 DamageSource 的功能<br>
 * 允许伤害源携带特殊标记，如穿透所有防御或虚空破壁
 */
public interface IDaedalusDamageSource {
    void daedalus$setBypassAll(boolean bypass);
    boolean daedalus$isBypassAll();

    void daedalus$setVoidBreach(boolean breach);
    boolean daedalus$isVoidBreach();

    // 对应 reference 中的 giveSpecialTag/hasTag，用于通用标记系统
    void daedalus$giveSpecialTag(byte tag);
    void daedalus$cleanSpecialTag(byte tag);
    boolean daedalus$hasTag(byte tag);
}
