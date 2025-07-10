package com.cheers.framework.datapermission.core.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataPermissionContextTest {

    @AfterEach
    void tearDown() {
        DataPermissionContext.clear();
    }

    @Test
    void testGet_NewContext() {
        // Execute
        DataPermissionContext.Context context = DataPermissionContext.get();

        // Verify
        assertNotNull(context);
        assertTrue(context.getEnable());
        assertTrue(context.getTableAliases().isEmpty());
    }

    @Test
    void testGet_ExistingContext() {
        // Setup
        DataPermissionContext.Context context1 = DataPermissionContext.get();
        context1.disable();
        context1.addTableAlias("users", "u");

        // Execute
        DataPermissionContext.Context context2 = DataPermissionContext.get();

        // Verify
        assertSame(context1, context2);
        assertFalse(context2.getEnable());
        assertEquals("u", context2.getTableAlias("users"));
    }

    @Test
    void testClear() {
        // Setup
        DataPermissionContext.Context context = DataPermissionContext.get();
        context.addTableAlias("users", "u");

        // Execute
        DataPermissionContext.clear();

        // Verify
        DataPermissionContext.Context newContext = DataPermissionContext.get();
        assertNotSame(context, newContext);
        assertTrue(newContext.getTableAliases().isEmpty());
    }

    @Test
    void testContext_EnableDisable() {
        // Setup
        DataPermissionContext.Context context = DataPermissionContext.get();

        // Execute & Verify
        assertTrue(context.getEnable());
        
        context.disable();
        assertFalse(context.getEnable());
        
        context.enable();
        assertTrue(context.getEnable());
    }

    @Test
    void testContext_TableAliases() {
        // Setup
        DataPermissionContext.Context context = DataPermissionContext.get();

        // Execute
        context.addTableAlias("users", "u");
        context.addTableAlias("departments", "d");

        // Verify
        assertEquals("u", context.getTableAlias("users"));
        assertEquals("d", context.getTableAlias("departments"));
        assertNull(context.getTableAlias("unknown"));
    }

    @Test
    void testContext_TableAliasesOverwrite() {
        // Setup
        DataPermissionContext.Context context = DataPermissionContext.get();

        // Execute
        context.addTableAlias("users", "u");
        context.addTableAlias("users", "u2");

        // Verify
        assertEquals("u2", context.getTableAlias("users"));
    }
} 