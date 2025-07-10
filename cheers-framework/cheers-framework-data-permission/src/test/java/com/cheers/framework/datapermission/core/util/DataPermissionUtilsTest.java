package com.cheers.framework.datapermission.core.util;

import com.cheers.framework.datapermission.core.context.DataPermissionContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataPermissionUtilsTest {

    @AfterEach
    void tearDown() {
        DataPermissionUtils.clear();
    }

    @Test
    void testEnable() {
        // Setup
        DataPermissionUtils.disable();

        // Execute
        DataPermissionUtils.enable();

        // Verify
        assertTrue(DataPermissionUtils.getContext().getEnable());
    }

    @Test
    void testDisable() {
        // Setup
        DataPermissionUtils.enable();

        // Execute
        DataPermissionUtils.disable();

        // Verify
        assertFalse(DataPermissionUtils.getContext().getEnable());
    }

    @Test
    void testGetContext() {
        // Execute
        DataPermissionContext.Context context = DataPermissionUtils.getContext();

        // Verify
        assertNotNull(context);
        assertTrue(context.getEnable()); // Default is enabled
    }

    @Test
    void testClear() {
        // Setup
        DataPermissionContext.Context context = DataPermissionUtils.getContext();
        context.disable();

        // Execute
        DataPermissionUtils.clear();

        // Verify
        DataPermissionContext.Context newContext = DataPermissionUtils.getContext();
        assertNotSame(context, newContext);
        assertTrue(newContext.getEnable());
    }
} 