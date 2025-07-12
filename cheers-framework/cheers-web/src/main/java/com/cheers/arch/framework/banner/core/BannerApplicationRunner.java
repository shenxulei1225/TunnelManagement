package com.cheers.arch.framework.banner.core;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.ClassUtils;

import java.util.concurrent.TimeUnit;

/**
 * 项目启动成功后，提供文档相关的地址
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        ThreadUtil.execute(() -> {
            ThreadUtil.sleep(1, TimeUnit.SECONDS); // 延迟 1 秒，保证输出到结尾
            log.info("\n----------------------------------------------------------\n\t" +
                            "项目启动成功！\n\t" +
                            "接口文档: \t{} \n\t" +
                            "开发文档: \t{} \n" +
                            "----------------------------------------------------------",
                    "http://localhost:48080/doc.html");

            // 系统管理
            if (isNotPresent("com.cheers.arch.module.system.framework.web.config.SystemWebConfiguration")) {
                System.out.println("[系统管理模块 cheers-module-system - 已禁用]");
            }
            // 设备管理
            if (isNotPresent("com.cheers.arch.module.device.framework.web.config.DeviceWebConfiguration")) {
                System.out.println("[设备管理模块 cheers-module-device - 已禁用]");
            }
            // 动态业务
            if (isNotPresent("com.cheers.arch.module.dynamic.framework.web.config.DynamicWebConfiguration")) {
                System.out.println("[动态业务模块 cheers-module-dynamic - 已禁用]");
            }
            // UX设计器
            if (isNotPresent("com.cheers.arch.module.uxdesigner.framework.web.config.UxDesignerWebConfiguration")) {
                System.out.println("[UX设计器模块 cheers-module-uxdesigner - 已禁用]");
            }
        });
    }

    private static boolean isNotPresent(String className) {
        return !ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader());
    }

}
