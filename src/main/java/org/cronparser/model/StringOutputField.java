package org.cronparser.model;

import java.util.Objects;

public class StringOutputField extends OutputField {
    private final String value;
    public StringOutputField(String name, String value) {
        super(name);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof StringOutputField that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    @Override
    public String toString() {
        return super.toString() + value;
    }
}
