package org.cronparser.parser.field;

public class DayOfMonthParser extends RangeFieldParser {
    @Override
    protected Integer getFirst() {
        return 1;
    }

    @Override
    protected Integer getLast() {
        return 31;
    }

    @Override
    protected String getFieldName() {
        return "day of month";
    }
}
