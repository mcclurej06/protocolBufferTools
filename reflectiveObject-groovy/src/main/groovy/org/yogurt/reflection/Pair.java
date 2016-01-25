package org.yogurt.reflection;

import java.util.Objects;

public class Pair<L, R> {
    private final L l;
    private final R r;

    public Pair(L l, R r) {
        this.l = l;
        this.r = r;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(l, pair.l) &&
                Objects.equals(r, pair.r);
    }

    @Override
    public int hashCode() {
        return Objects.hash(l, r);
    }
}
