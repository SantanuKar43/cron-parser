package org.cronparser.model;

import java.util.Objects;
import java.util.SortedSet;

public class RangeOutputField extends OutputField {
    private final SortedSet<Integer> range;

    public RangeOutputField(String name, SortedSet<Integer> range) {
        super(name);
        this.range = range;
    }

    public SortedSet<Integer> getRange() {
        return range;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof RangeOutputField that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(range, that.range);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), range);
    }

    @Override
    public String toString() {
        return super.toString()
                + String.join(" ", range.stream().map(String::valueOf).toList());
    }
}
