package org.cronparser.parser;


import org.cronparser.parser.field.RangeFieldParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class RangeFieldParserTest {

    private final RangeFieldParser parser = new RangeFieldParser(0, 59, "minute");

    @Test(expected = IllegalArgumentException.class)
    public void test_nullExpression() {
        parser.parseExpression(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_emptyExpression() {
        parser.parseExpression("");
    }

    @Test
    public void test_integer_valid() {
        Assert.assertEquals(new TreeSet<>(List.of(10)), parser.parseExpression("10").getRange());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_integer_outOfRange() {
        parser.parseExpression("60");
    }

    @Test
    public void test_asterisk() {
        SortedSet<Integer> set = new TreeSet<>();
        for (int i = 0; i < 60; i++) {
            set.add(i);
        }
        Assert.assertEquals(set, parser.parseExpression("*").getRange());
    }

    @Test
    public void test_hyphen_valid() {
        SortedSet<Integer> set = new TreeSet<>();
        for (int i = 10; i <= 20; i++) {
            set.add(i);
        }
        Assert.assertEquals(set, parser.parseExpression("10-20").getRange());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_hyphen_outOfRange() {
        parser.parseExpression("10-100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_hyphen_invalidRange() {
        parser.parseExpression("100-10");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_hyphen_invalid_noArg() {
        parser.parseExpression("-");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_hyphen_invalid_oneArg() {
        parser.parseExpression("10-");
    }

    @Test
    public void test_slash_valid() {
        SortedSet<Integer> set1 = new TreeSet<>();
        for (int i = 0; i < 60; i++) {
            if (i % 10 == 0) {
                set1.add(i);
            }
        }
        Assert.assertEquals(set1, parser.parseExpression("*/10").getRange());

        SortedSet<Integer> set2 = new TreeSet<>();
        for (int i = 0; i < 59; i++) {
            if (i % 2 == 0) {
                set2.add(i);
            }
        }
        Assert.assertEquals(set2, parser.parseExpression("0-59/2").getRange());

        Assert.assertEquals(new TreeSet<>(Arrays.asList(1, 18, 35, 52)), parser.parseExpression("1-59/17").getRange());
        Assert.assertEquals(new TreeSet<>(Arrays.asList(20, 32, 44)), parser.parseExpression("20-49/12").getRange());
        Assert.assertEquals(new TreeSet<>(Arrays.asList(0, 15, 30, 45)), parser.parseExpression("*/15").getRange());
        Assert.assertEquals(new TreeSet<>(Arrays.asList(20, 35, 50)), parser.parseExpression("20/15").getRange());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_slash_outOfRange() {
        parser.parseExpression("0-100/2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_slash_invalid_noArgs() {
        parser.parseExpression("/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_slash_invalid_oneArg() {
        parser.parseExpression("1/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_slash_invalid_denominatorZero() {
        parser.parseExpression("*/0");
    }

    @Test
    public void test_comma_valid() {
        Assert.assertEquals(new TreeSet<>(Arrays.asList(1, 20, 32, 45)), parser.parseExpression("1,20,32,45").getRange());
        Assert.assertEquals(new TreeSet<>(Arrays.asList(1, 20, 30, 40)), parser.parseExpression("1,20-40/10").getRange());
        Assert.assertEquals(new TreeSet<>(Arrays.asList(0, 1, 2, 10, 20, 30, 40, 50)), parser.parseExpression("1,2,*/10").getRange());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_comma_outOfRange() {
        parser.parseExpression("1,20,32,85");
    }

    @Test
    public void testInvalidExpressions() {
        try {
            parser.parseExpression("*-");
        } catch (IllegalArgumentException ignored) {}
        try {
            parser.parseExpression("1-*");
        } catch (IllegalArgumentException ignored) {}

    }

}