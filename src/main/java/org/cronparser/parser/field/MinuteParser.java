package org.cronparser.parser.field;

import java.util.SortedSet;
import java.util.TreeSet;

public class MinuteParser extends RangeFieldParser {
    @Override
    protected Integer getFirst() {
        return 0;
    }

    @Override
    protected Integer getLast() {
        return 59;
    }

    @Override
    protected String getFieldName() {
        return "minute";
    }
}
