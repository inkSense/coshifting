package org.coshift.d_frameworks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                    AuthenticationProvider ucProvider) throws Exception {
        http
          .csrf(c -> c.disable())
          .authenticationProvider(ucProvider)
          .authorizeHttpRequests(a -> a
                  .requestMatchers("/api/**").authenticated()
                  .anyRequest().permitAll())
          .httpBasic(Customizer.withDefaults())
          .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean  // ToDo: löschen und unten einkommentieren
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();  
    }

    // ToDo: einkommentieren und oben löschen
    // @Bean
    // PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

}


