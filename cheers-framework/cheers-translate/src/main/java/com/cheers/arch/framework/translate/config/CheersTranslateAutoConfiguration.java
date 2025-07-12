package com.cheers.arch.framework.translate.config;

import com.cheers.arch.framework.translate.core.TranslateUtils;
import com.fhs.trans.service.impl.TransService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 多语言翻译配置类
 *
 * @author cheers
 */
@AutoConfiguration
public class CheersTranslateAutoConfiguration {

    @Bean
    public void translateUtils(TransService transService) {
        TranslateUtils.init(transService);
    }

}
