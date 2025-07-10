package com.cheers.framework.mybatis.core.util;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.cheers.framework.common.pojo.PageParam;
import com.cheers.framework.common.pojo.SortingField;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.plugin.Interceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MyBatis 工具类
 */
public class MyBatisUtils {

    private static final String MYSQL_ESCAPE_CHARACTER = "\\";

    public static Interceptor addInterceptor(InnerInterceptor innerInterceptor) {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(innerInterceptor);
        return mybatisPlusInterceptor;
    }

    public static String buildLikeCondition(String value) {
        return escapeCharacter(value) + StringPool.PERCENT;
    }

    public static Expression buildLikeExpression(String value) {
        return new StringValue(buildLikeCondition(value));
    }

    public static String escapeCharacter(String value) {
        if (value == null) {
            return null;
        }

        return value.replaceAll("[%_\\\\]", MYSQL_ESCAPE_CHARACTER + "$0");
    }

    public static Collection<OrderItem> buildOrderItem(Collection<SortingField> sortingFields) {
        if (CollectionUtil.isEmpty(sortingFields)) {
            return null;
        }
        return sortingFields.stream().map(field -> {
            OrderItem item = new OrderItem();
            item.setColumn(field.getField());
            item.setAsc(field.isAsc());
            return item;
        }).collect(Collectors.toList());
    }

    public static List<OrderItem> buildOrderItem(PageParam pageParam) {
        List<OrderItem> orderItems = new ArrayList<>();
        if (CollectionUtil.isEmpty(pageParam.getSortingFields())) {
            return orderItems;
        }
        pageParam.getSortingFields().forEach(sortingField -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(sortingField.getField());
            orderItem.setAsc(sortingField.isAsc());
            orderItems.add(orderItem);
        });
        return orderItems;
    }

} 