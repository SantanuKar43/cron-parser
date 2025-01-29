package org.cronparser;

import org.cronparser.parser.ExpressionParser;

public class Main {

    public static void main(String[] args) {
        ExpressionParser expressionParser = new ExpressionParser();
        System.out.println(expressionParser.parse(args[0]));
    }
}