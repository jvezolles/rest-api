package com.jvezolles.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Configuration spring boot for time
 *
 * @author Vezolles
 */
@Configuration
public class TimeConfig {

    /**
     * Bean declaration for clock
     *
     * @return the clock used
     */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}
