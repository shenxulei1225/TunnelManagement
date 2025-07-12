package com.cheers.arch.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目的启动类
 *
 * @author Cheers
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别扫描包
@SpringBootApplication(scanBasePackages = {"com.cheers.arch.server", "com.cheers.arch.module", "com.cheers"})
public class CheersServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheersServerApplication.class, args);
    }

}
