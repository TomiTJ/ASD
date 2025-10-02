

package com.asd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BankAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankAdminApplication.class, args);
    }

}
