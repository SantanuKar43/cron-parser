package org.cronparser.parser.field;

import org.cronparser.model.RangeOutputField;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Base class for parsing a field in a cron expression.
 * Implementations are specific to the field,
 * and provide first and last values for the range of values allowed for the field.
 * */
public abstract class RangeFieldParser {

    private static final Pattern REGEX = Pattern.compile("[\\d,\\-\\/\\*]+");

    public RangeOutputField parseExpression(String expr) {
        SortedSet<Integer> result = parse(expr);
        return new RangeOutputField(getFieldName(), result);
    }

    protected abstract Integer getFirst();
    protected abstract Integer getLast();
    protected abstract String getFieldName();

    /**
     * Recursively parses a field expression and returns set of valid values.
     * */
    private SortedSet<Integer> parse(String expr) {
        validate(expr);
        SortedSet<Integer> result = new TreeSet<>();
        if (expr.contains(",")) {
            result.addAll(parseComma(expr));
        } else if (expr.contains("/")) {
            result.addAll(parseSlash(expr));
        } else if (expr.contains("-")) {
            result.addAll(parseHyphen(expr));
        }  else if (expr.contains("*")) {
            result.addAll(parseAsterisk());
        } else {
            result.addAll(parseInteger(expr));
        }
        return result;
    }

    private void validate(String expr) {
        if (expr == null || expr.isBlank()) {
            throw new IllegalArgumentException("Expression can't be empty");
        }
        if (!REGEX.matcher(expr).matches()) {
            throw new IllegalArgumentException("Expression: " + expr +" doesn't match regex: " + REGEX);
        }
    }

    private SortedSet<Integer> parseInteger(String expr) {
        SortedSet<Integer> result = new TreeSet<>();
        int value = Integer.parseInt(expr);
        if (isInRangeOrThrowException(value)) {
            result.add(value);
        }
        return result;
    }

    private SortedSet<Integer> parseAsterisk() {
        return getAllValues();
    }

    private SortedSet<Integer> parseHyphen(String expr) {
        SortedSet<Integer> result = new TreeSet<>();
        String[] hyphenSplitted = expr.split("-");
        if (hyphenSplitted.length != 2) {
            throw new IllegalArgumentException("invalid hyphen expression: " + expr);
        }
        int left = Integer.parseInt(hyphenSplitted[0]);
        int right = Integer.parseInt(hyphenSplitted[1]);
        if (left > right) {
            throw new IllegalArgumentException("invalid range: " + expr);
        }
        if (isInRangeOrThrowException(left) && isInRangeOrThrowException(right)) {
            for (int i = left; i <= right; i++) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Expressions like 1/2 are treated as 1-{last valid value}/2.
     * Ref: <a href="https://en.wikipedia.org/wiki/Cron#:~:text=%5B21%5D-,Slash%20(/),-In%20vixie%2Dcron">Wikipedia article on Cron</a>
     * */
    private SortedSet<Integer> parseSlash(String expr) {
        SortedSet<Integer> result = new TreeSet<>();
        String[] slashSplitted = expr.split("/");
        if (slashSplitted.length != 2) {
            throw new IllegalArgumentException("invalid slash expression: " + expr);
        }
        SortedSet<Integer> numeratorValues = parse(slashSplitted[0]);
        if (numeratorValues.isEmpty()) {
            throw new IllegalArgumentException("invalid slash expression: " + expr);
        }
        int start = numeratorValues.getFirst();
        int end = !slashSplitted[0].contains("-") && numeratorValues.size() == 1 ?
                getLast() : numeratorValues.getLast();
        int denominator = Integer.parseInt(slashSplitted[1]);
        if (denominator < 1) {
            throw new IllegalArgumentException("invalid slash expression: " + expr);
        }
        for (int i = start; i <= end; i += denominator) {
            result.add(i);
        }
        return result;
    }

    private SortedSet<Integer> parseComma(String expr) {
        SortedSet<Integer> result = new TreeSet<>();
        String[] commaSplitted = expr.split(",");
        for (String token : commaSplitted) {
            result.addAll(parse(token));
        }
        return result;
    }

    private SortedSet<Integer> getAllValues() {
        SortedSet<Integer> set = new TreeSet<>();
        for (int i = getFirst(); i <= getLast(); i++) {
            set.add(i);
        }
        return set;
    }

    private boolean isInRangeOrThrowException(int value) {
        if (getFirst() <= value && value <= getLast()) {
            return true;
        } else {
            throw new IllegalArgumentException("expression contains values outside permitted range:" + value);
        }
    }

}
