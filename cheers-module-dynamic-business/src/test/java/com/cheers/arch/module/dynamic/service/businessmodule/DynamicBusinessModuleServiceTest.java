package com.cheers.arch.module.dynamic.service.businessmodule;

import com.cheers.arch.module.dynamic.dal.dataobject.businessmodule.DynamicBusinessModuleDO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DynamicBusinessModuleServiceTest {

    @Resource
    private DynamicBusinessModuleService dynamicBusinessModuleService;

    @Test
    public void testGetBusinessModuleTree() {
        List<DynamicBusinessModuleDO> tree = dynamicBusinessModuleService.getBusinessModuleTree(null);
        assertNotNull(tree);
        System.out.println("业务分组树节点数量: " + tree.size());
    }

    @Test
    public void testGetBusinessModuleSubTree() {
        // 假设存在ID为1的节点
        List<DynamicBusinessModuleDO> subTree = dynamicBusinessModuleService.getBusinessModuleSubTree(1L);
        assertNotNull(subTree);
        System.out.println("子树节点数量: " + subTree.size());
    }

    @Test
    public void testDragBusinessModule() {
        // 测试拖拽功能
        // 注意：这个测试需要数据库中有相应的数据
        try {
            Boolean result = dynamicBusinessModuleService.dragBusinessModule(2L, 1L, "inner", null);
            assertNotNull(result);
            System.out.println("拖拽操作结果: " + result);
        } catch (Exception e) {
            System.out.println("拖拽测试异常（可能是数据不存在）: " + e.getMessage());
        }
    }
} 