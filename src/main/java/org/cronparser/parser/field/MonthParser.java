package org.cronparser.parser.field;

public class MonthParser extends RangeFieldParser {
    @Override
    protected Integer getFirst() {
        return 1;
    }

    @Override
    protected Integer getLast() {
        return 12;
    }

    @Override
    protected String getFieldName() {
        return "month";
    }
}
