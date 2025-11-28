package com.kzjy.daedalus.duck;

public interface IDaedalusDamageSource {
    void daedalus$setBypassAll(boolean bypass);
    boolean daedalus$isBypassAll();

    void daedalus$setVoidBreach(boolean breach);
    boolean daedalus$isVoidBreach();

    // 新增：对应 reference 中的 giveSpecialTag/hasTag
    void daedalus$giveSpecialTag(byte tag);
    void daedalus$cleanSpecialTag(byte tag);
    boolean daedalus$hasTag(byte tag);
}
