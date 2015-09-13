package com.cumulativeminds.zeus.util;

import java.io.Serializable;
import java.util.Objects;

public final class ObjectReference<T> implements Serializable {
    private static final long serialVersionUID = 4529968112344971048L;

    private T referent;

    public ObjectReference(T referent) {
        this.referent = referent;
    }

    public ObjectReference() {
    }

    public T getReferent() {
        return referent;
    }

    public void setReferent(T referent) {
        if (this == referent)
            throw new IllegalArgumentException("referent can't be itself");
        this.referent = referent;
    }

    @Override
    public int hashCode() {
        return referent == null ? 0 : referent.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ObjectReference) {
            return Objects.equals(this.referent, ((ObjectReference<?>) obj).referent);
        }

        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(referent);
    }
}
