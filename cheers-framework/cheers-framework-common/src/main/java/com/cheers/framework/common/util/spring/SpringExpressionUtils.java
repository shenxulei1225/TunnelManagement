package com.cheers.framework.common.util.spring;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Spring Expression Language (SpEL) 工具类
 */
public class SpringExpressionUtils {

    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * 解析表达式
     *
     * @param expressionString 表达式字符串
     * @return 解析结果
     */
    public static Object parseExpression(String expressionString) {
        Expression expression = EXPRESSION_PARSER.parseExpression(expressionString);
        return expression.getValue();
    }

    /**
     * 解析表达式，返回指定类型的结果
     *
     * @param expressionString 表达式字符串
     * @param resultType 结果类型
     * @param <T> 结果类型泛型
     * @return 解析结果
     */
    public static <T> T parseExpression(String expressionString, Class<T> resultType) {
        Expression expression = EXPRESSION_PARSER.parseExpression(expressionString);
        return expression.getValue(resultType);
    }
} 