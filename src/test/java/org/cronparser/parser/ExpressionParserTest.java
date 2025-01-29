package org.cronparser.parser;

import org.cronparser.model.Output;
import org.cronparser.model.RangeOutputField;
import org.cronparser.model.StringOutputField;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class ExpressionParserTest {
    private final ExpressionParser expressionParser = new ExpressionParser();

    @Test
    public void test() {
        Output output = new Output();
        output.addOutputField(new RangeOutputField("minute", new TreeSet<>(Arrays.asList(0, 15, 30, 45))));
        output.addOutputField(new RangeOutputField("hour", new TreeSet<>(List.of(0))));
        output.addOutputField(new RangeOutputField("day of month", new TreeSet<>(Arrays.asList(1, 15))));
        output.addOutputField(new RangeOutputField("month", new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))));
        output.addOutputField(new RangeOutputField("day of week", new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5))));
        output.addOutputField(new StringOutputField("command", "/usr/bin/find"));
        Assert.assertEquals(output, expressionParser.parse("*/15 0 1,15 * 1-5 /usr/bin/find"));
    }

    @Test
    public void test_longCommand() {
        Output output = new Output();
        output.addOutputField(new RangeOutputField("minute", new TreeSet<>(Arrays.asList(0, 15, 30, 45))));
        output.addOutputField(new RangeOutputField("hour", new TreeSet<>(List.of(0))));
        output.addOutputField(new RangeOutputField("day of month", new TreeSet<>(Arrays.asList(1, 15))));
        output.addOutputField(new RangeOutputField("month", new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))));
        output.addOutputField(new RangeOutputField("day of week", new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5))));
        output.addOutputField(new StringOutputField("command", "apt-get clean"));
        Assert.assertEquals(output, expressionParser.parse("*/15 0 1,15 * 1-5 apt-get clean"));
    }

    @Test
    public void test_extraspaces() {
        Output output = new Output();
        output.addOutputField(new RangeOutputField("minute", new TreeSet<>(Arrays.asList(0, 15, 30, 45))));
        output.addOutputField(new RangeOutputField("hour", new TreeSet<>(List.of(0))));
        output.addOutputField(new RangeOutputField("day of month", new TreeSet<>(Arrays.asList(1, 15))));
        output.addOutputField(new RangeOutputField("month", new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))));
        output.addOutputField(new RangeOutputField("day of week", new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5))));
        output.addOutputField(new StringOutputField("command", "apt-get clean"));
        Assert.assertEquals(output, expressionParser.parse("*/15  0  1,15 * 1-5 apt-get clean"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_invalid() {
        expressionParser.parse("*/15 0 1,15 * /usr/bin/find");
    }

}