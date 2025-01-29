package org.cronparser.parser.field;

public class HourParser extends RangeFieldParser {
    @Override
    protected Integer getFirst() {
        return 0;
    }

    @Override
    protected Integer getLast() {
        return 23;
    }

    @Override
    protected String getFieldName() {
        return "hour";
    }
}
