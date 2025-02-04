package org.cronparser.parser.field;

import org.cronparser.model.RangeOutputField;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Class for parsing a field in a cron expression.
 * Instances should provide first and last values for the range of values allowed for the field.
 * */
public class RangeFieldParser {

    private static final Pattern VALID_CHARACTER_REGEX = Pattern.compile("[\\d,\\-\\/\\*]+");
    private static final String EMPTY_EXPRESSION_MESSAGE = "expression can't be empty";
    private static final String INVALID_CHARACTERS_MESSAGE = "expression: %s contains invalid characters, should match regex: %s";
    private static final String INVALID_ARGUMENTS_HYPHEN_MESSAGE = "invalid hyphen expression, less than 2 arguments: %s";
    private static final String INVALID_RANGE_HYPHEN_MESSAGE = "invalid range for hyphen expression: %s";
    private static final String INVALID_SLASH_ARGUMENTS_MESSAGE = "invalid slash expression, less than 2 arguments: %s";
    private static final String INVALID_SLASH_DENOMINATOR_MESSAGE = "invalid slash expression, denominator < 1: %s";
    private static final String VALUES_OUTSIDE_RANGE_MESSAGE = "expression contains values: %s outside permitted range for field: %s";

    private final int first;
    private final int last;
    private final String fieldName;


    public RangeFieldParser(int first, int last, String fieldName) {
        this.first = first;
        this.last = last;
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

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
            result.addAll(getAllValues());
        } else {
            result.addAll(parseInteger(expr));
        }
        return result;
    }

    private void validate(String expr) {
        if (expr == null || expr.isBlank()) {
            throw new IllegalArgumentException(EMPTY_EXPRESSION_MESSAGE);
        }
        if (!VALID_CHARACTER_REGEX.matcher(expr).matches()) {
            throw new IllegalArgumentException(String.format(INVALID_CHARACTERS_MESSAGE, expr, VALID_CHARACTER_REGEX));
        }
    }

    private SortedSet<Integer> parseInteger(String expr) {
        SortedSet<Integer> result = new TreeSet<>();
        int value = Integer.parseInt(expr);
        validateWithinRange(value);
        result.add(value);
        return result;
    }

    private SortedSet<Integer> parseHyphen(String expr) {
        SortedSet<Integer> result = new TreeSet<>();
        String[] hyphenSplitted = expr.split("-");
        if (hyphenSplitted.length != 2) {
            throw new IllegalArgumentException(String.format(INVALID_ARGUMENTS_HYPHEN_MESSAGE, expr));
        }
        int left = Integer.parseInt(hyphenSplitted[0]);
        int right = Integer.parseInt(hyphenSplitted[1]);
        if (left > right) {
            throw new IllegalArgumentException(String.format(INVALID_RANGE_HYPHEN_MESSAGE, expr));
        }
        validateWithinRange(left);
        validateWithinRange(right);
        for (int i = left; i <= right; i++) {
            result.add(i);
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
            throw new IllegalArgumentException(String.format(INVALID_SLASH_ARGUMENTS_MESSAGE, expr));
        }
        SortedSet<Integer> numeratorValues = parse(slashSplitted[0]);
        int start = numeratorValues.first();
        int end = !slashSplitted[0].contains("-") && numeratorValues.size() == 1 ?
                this.last : numeratorValues.last();
        int denominator = Integer.parseInt(slashSplitted[1]);
        if (denominator < 1) {
            throw new IllegalArgumentException(String.format(INVALID_SLASH_DENOMINATOR_MESSAGE, expr));
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
        for (int i = this.first; i <= this.last; i++) {
            set.add(i);
        }
        return set;
    }

    private void validateWithinRange(int value) throws IllegalArgumentException {
        if (value < this.first || value > this.last) {
            throw new IllegalArgumentException(String.format(VALUES_OUTSIDE_RANGE_MESSAGE, value, fieldName));
        }
    }

}
