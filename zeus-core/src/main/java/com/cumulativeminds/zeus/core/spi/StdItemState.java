package com.cumulativeminds.zeus.core.spi;

public enum StdItemState implements ItemState {
    PENDING, INDEX_FAILED, INDEX, STORED_FAILED, STORED, ERROR, IGNORED;

    public boolean isMiminumQualfifiedState(ItemState targetState) {
        StdItemState current = this;
        StdItemState target = (StdItemState) targetState;

        boolean yes = current.ordinal() == target.ordinal() - 2 ||
                current.ordinal() == target.ordinal() - 1;
        return yes;
    }

    public boolean isItemStateGood(ItemState state) {
        return state.ordinal() == this.ordinal() - 1 ||
                state.ordinal() == this.ordinal() - 2;
    }
}
