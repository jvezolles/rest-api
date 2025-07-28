package com.jvezolles.api.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.nio.charset.StandardCharsets;

/**
 * Configuration spring boot for resources
 *
 * @author Vezolles
 */
@Configuration
public class ResourceConfig {

    /**
     * Bean declaration for message source
     *
     * @return message source
     */
    @Bean
    public MessageSource getMessageSource() {

        ReloadableResourceBundleMessageSource messageRessource = new ReloadableResourceBundleMessageSource();
        messageRessource.setBasenames("classpath:locale/messages");
        messageRessource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageRessource.setAlwaysUseMessageFormat(Boolean.TRUE);

        return messageRessource;
    }

    /**
     * Bean declaration for validator
     *
     * @return factory for validators with message source
     */
    @Bean
    public LocalValidatorFactoryBean getValidator() {

        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(getMessageSource());

        return bean;
    }

}
