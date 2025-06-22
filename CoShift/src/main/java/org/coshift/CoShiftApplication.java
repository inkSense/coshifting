package org.coshift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// (
//     exclude = {
//         DataSourceAutoConfiguration.class,
//         HibernateJpaAutoConfiguration.class
//     })
//@EnableJpaRepositories(basePackages = "org.coshift.c_adapter.persistence")
public class CoShiftApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoShiftApplication.class, args);
    }

}
