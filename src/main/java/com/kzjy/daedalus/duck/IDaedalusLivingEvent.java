package com.kzjy.daedalus.duck;

public interface IDaedalusLivingEvent {
    void daedalus$setUncancelable(boolean uncancelable);
    boolean daedalus$isUncancelable();

    void daedalus$setOnlyAmountUp(boolean onlyUp);
    boolean daedalus$isOnlyAmountUp();
}
