package org.cronparser.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Output {

    private final List<OutputField> outputFields;

    public Output() {
        this.outputFields = new ArrayList<>();
    }

    public void addOutputField(OutputField outputField) {
        this.outputFields.add(outputField);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Output output)) return false;
        return Objects.equals(outputFields, output.outputFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputFields);
    }

    @Override
    public String toString() {
        return String.join("\n", outputFields.stream().map(String::valueOf).toList());
    }
}
