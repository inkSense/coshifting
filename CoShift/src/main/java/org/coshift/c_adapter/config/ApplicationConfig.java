package org.coshift.c_adapter.config;

import org.coshift.b_application.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApplicationConfig {

    @Bean
    ShiftService shiftService(ShiftRepository repository) {
        return new ShiftService(repository);
    }
}

