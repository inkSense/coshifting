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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.coshift.b_application.useCases.AuthenticateUserUseCase;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.PasswordChecker;
import org.coshift.b_application.UseCaseInteractor;
import org.coshift.b_application.ports.PresenterInputPort;
import org.coshift.b_application.ports.ShiftRepository;
import org.coshift.b_application.ports.TimeAccountRepository;
import org.coshift.c_adapters.persistence.json.TimeAccountJsonRepository;
import org.coshift.c_adapters.ports.TimeAccountJsonFileAccessor;

@Configuration
@EnableWebSecurity
public class SpringConfig {

    @Bean
    UseCaseInteractor useCaseInteractor(ShiftRepository    shiftRepo,
                                        PersonRepository   personRepo,
                                        TimeAccountRepository accountRepo,
                                        PasswordChecker   passwordChecker,
                                        PresenterInputPort presenter) {
        return new UseCaseInteractor(
                shiftRepo,
                personRepo,
                accountRepo,
                passwordChecker,
                presenter);
    }

    @Bean
    SecurityFilterChain filterChain(
        HttpSecurity http,
        AuthenticationProvider ucProvider
    ) throws Exception {
        http
            .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .authenticationProvider(ucProvider)
            .authorizeHttpRequests(a -> a
                  .requestMatchers("/api/admin/**").hasRole("ADMIN")
                  .requestMatchers("/api/**").authenticated()
                  .anyRequest().permitAll())
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticateUserUseCase authUC(PersonRepository repo, PasswordChecker checker){
        return new AuthenticateUserUseCase(repo, checker);
    }

    @Bean
    TimeAccountRepository timeAccountRepo(TimeAccountJsonFileAccessor accessor) {
        return new TimeAccountJsonRepository(accessor);
    }
}


