package org.cronparser.parser.field;

import org.cronparser.model.RangeOutputField;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Base class for parsing a field in a cron expression.
 * Implementations are specific to the field,
 * and provide first and last values for the range of values allowed for the field.
 * */
public class RangeFieldParser {

    private final int first;
    private final int last;
    private final String fieldName;

    public RangeFieldParser(int first, int last, String fieldName) {
        this.first = first;
        this.last = last;
        this.fieldName = fieldName;
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }

    public String getFieldName() {
        return fieldName;
    }

    private static final Pattern VALID_CHARACTER_REGEX = Pattern.compile("[\\d,\\-\\/\\*]+");

    public RangeOutputField parseExpression(String expr) {
        SortedSet<Integer> result = parse(expr);
        return new RangeOutputField(getFieldName(), result);
    }

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
            throw new IllegalArgumentException("expression can't be empty");
        }
        if (!VALID_CHARACTER_REGEX.matcher(expr).matches()) {
            throw new IllegalArgumentException("expression: " + expr +" doesn't match regex: " + VALID_CHARACTER_REGEX);
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
            throw new IllegalArgumentException("invalid hyphen expression, less than 2 arguments: " + expr);
        }
        int left = Integer.parseInt(hyphenSplitted[0]);
        int right = Integer.parseInt(hyphenSplitted[1]);
        if (left > right) {
            throw new IllegalArgumentException("invalid range for hyphen expression: " + expr);
        }
        if (isInRangeOrThrowException(left)
                && isInRangeOrThrowException(right)) {
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
            throw new IllegalArgumentException("invalid slash expression, less than 2 arguments: " + expr);
        }
        SortedSet<Integer> numeratorValues = parse(slashSplitted[0]);
        int start = numeratorValues.getFirst();
        int end = !slashSplitted[0].contains("-") && numeratorValues.size() == 1 ?
                getLast() : numeratorValues.getLast();
        int denominator = Integer.parseInt(slashSplitted[1]);
        if (denominator < 1) {
            throw new IllegalArgumentException("invalid slash expression, denominator < 1: " + expr);
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
