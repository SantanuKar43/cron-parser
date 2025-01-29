package org.cronparser.parser;

import org.cronparser.model.Output;
import org.cronparser.model.StringOutputField;
import org.cronparser.parser.field.*;

import java.util.ArrayList;
import java.util.List;

public class ExpressionParser {

    private final List<RangeFieldParser> parsers;

    public ExpressionParser() {
        parsers = new ArrayList<>();
        parsers.add(new MinuteParser());
        parsers.add(new HourParser());
        parsers.add(new DayOfMonthParser());
        parsers.add(new MonthParser());
        parsers.add(new DayOfWeekParser());
    }

    public Output parse(String expression) {
        validate(expression);
        Output output = new Output();
        String[] spaceSplitted = expression.split("\\s+");
        if (spaceSplitted.length < 6) {
            throw new IllegalArgumentException("expression contains less than 6 arguments: " + expression);
        }
        for (int i = 0; i < 5; i++) {
            output.addOutputField(parsers.get(i).parseExpression(spaceSplitted[i]));
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
