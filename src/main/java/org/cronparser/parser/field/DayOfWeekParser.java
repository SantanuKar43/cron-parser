package org.cronparser.parser.field;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class DayOfWeekParser extends RangeFieldParser {
    @Override
    protected Integer getFirst() {
        return 1;
    }

    @Override
    protected Integer getLast() {
        return 7;
    }

    @Override
    protected String getFieldName() {
        return "day of week";
    }
}
