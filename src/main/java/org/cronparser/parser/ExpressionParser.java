package org.cronparser.parser;

import org.cronparser.model.Output;
import org.cronparser.model.StringOutputField;
import org.cronparser.parser.field.*;

import java.util.ArrayList;
import java.util.List;

public class ExpressionParser {

    private final List<RangeFieldParser> fieldParsers;

    public ExpressionParser() {
        this.fieldParsers = new ArrayList<>();
        this.fieldParsers.add(new RangeFieldParser(0, 59, "minute"));
        this.fieldParsers.add(new RangeFieldParser(0, 23, "hour"));
        this.fieldParsers.add(new RangeFieldParser(1, 31, "day of month"));
        this.fieldParsers.add(new RangeFieldParser(1, 12, "month"));
        this.fieldParsers.add(new RangeFieldParser(0, 6, "day of week"));
    }

    public Output parse(String expression) {
        validate(expression);
        Output output = new Output();
        String[] spaceSplitted = expression.split("\\s+");
        if (spaceSplitted.length < 6) {
            throw new IllegalArgumentException("expression contains less than 6 arguments: " + expression);
        }
        for (int i = 0; i < 5; i++) {
            output.addOutputField(this.fieldParsers.get(i).parseExpression(spaceSplitted[i]));
        }
        StringBuilder command = new StringBuilder();
        for (int i = 5; i < spaceSplitted.length; i++) {
            command.append(spaceSplitted[i]);
            if (i != spaceSplitted.length - 1) {
                command.append(" ");
            }
        }
        output.addOutputField(new StringOutputField("command", command.toString()));
        return output;
    }

    private void validate(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("Expression can't be empty");
        }
    }
}
