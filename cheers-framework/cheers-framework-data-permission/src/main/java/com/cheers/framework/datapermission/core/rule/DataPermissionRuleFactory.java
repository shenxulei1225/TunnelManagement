package com.cheers.framework.datapermission.core.rule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据权限规则工厂
 * 负责创建和管理数据权限规则
 */
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Slf4j
public class DataPermissionRuleFactory {

    /**
     * 数据权限规则缓存
     * key: 规则类型
     * value: 规则实例
     */
    private final Map<Class<? extends DataPermissionRule>, DataPermissionRule> ruleCache = new ConcurrentHashMap<>();

    /**
     * 当前线程的规则列表
     */
    private final ThreadLocal<List<DataPermissionRule>> currentRules = new ThreadLocal<>();

    public DataPermissionRuleFactory(ObjectProvider<List<DataPermissionRule>> rulesProvider) {
        // 初始化规则
        List<DataPermissionRule> rules = rulesProvider.getIfAvailable(Collections::emptyList);
        rules.forEach(rule -> ruleCache.put(rule.getClass(), rule));
        log.info("[DataPermissionRuleFactory][初始化数据权限规则,数量:{}]", rules.size());
    }

    /**
     * 获取规则实例
     */
    public DataPermissionRule getRule(Class<? extends DataPermissionRule> ruleClass) {
        return ruleCache.get(ruleClass);
    }

    /**
     * 设置当前线程的规则列表
     */
    public void setRules(List<DataPermissionRule> rules) {
        currentRules.set(rules);
    }

    /**
     * 获取当前线程的规则列表
     */
    public List<DataPermissionRule> getRules() {
        return currentRules.get();
    }

    /**
     * 清除当前线程的规则列表
     */
    public void clearRules() {
        currentRules.remove();
    }

    /**
     * 创建规则列表
     * 
     * @param ruleClasses 规则类数组
     * @return 规则列表
     */
    public List<DataPermissionRule> createRules(Class<? extends DataPermissionRule>[] ruleClasses) {
        if (ruleClasses == null || ruleClasses.length == 0) {
            return Collections.emptyList();
        }
        List<DataPermissionRule> rules = new ArrayList<>(ruleClasses.length);
        for (Class<? extends DataPermissionRule> ruleClass : ruleClasses) {
            DataPermissionRule rule = getRule(ruleClass);
            if (rule != null) {
                rules.add(rule);
            }
        }
        return rules;
    }

    /**
     * 规则组合缓存
     * key: 规则组合ID
     * value: 规则组合
     */
    private final Map<String, RuleCombination> combinationCache = new ConcurrentHashMap<>();

    /**
     * 创建规则组合
     *
     * @param rules 规则列表
     * @param type 组合类型(AND/OR)
     * @return 规则组合ID
     */
    public String createRuleCombination(List<Class<? extends DataPermissionRule>> rules, RuleCombination.Type type) {
        // 生成组合ID
        String combinationId = UUID.randomUUID().toString();
        // 创建规则组合
        RuleCombination combination = new RuleCombination();
        combination.setId(combinationId);
        combination.setType(type);
        combination.setRules(new ArrayList<>());
        // 添加规则
        for (Class<? extends DataPermissionRule> ruleClass : rules) {
            DataPermissionRule rule = getRule(ruleClass);
            if (rule != null) {
                combination.getRules().add(rule);
            }
        }
        // 缓存规则组合
        combinationCache.put(combinationId, combination);
        return combinationId;
    }

    /**
     * 获取规则组合
     */
    public RuleCombination getRuleCombination(String combinationId) {
        return combinationCache.get(combinationId);
    }

    /**
     * 删除规则组合
     */
    public void removeRuleCombination(String combinationId) {
        combinationCache.remove(combinationId);
    }

    /**
     * 规则组合
     */
    @lombok.Data
    public static class RuleCombination {
        /**
         * 组合ID
         */
        private String id;
        /**
         * 组合类型
         */
        private Type type;
        /**
         * 规则列表
         */
        private List<DataPermissionRule> rules;

        /**
         * 组合类型
         */
        public enum Type {
            /**
             * 且
             */
            AND,
            /**
             * 或
             */
            OR
        }
    }

} 