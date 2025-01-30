package org.cronparser.model;

import java.util.Objects;
import java.util.SortedSet;
import java.util.stream.Collectors;

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
        return String.format("%s%s",
                super.toString(),
                range.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(" ")));
    }
}
