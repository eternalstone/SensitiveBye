package io.github.eternalstone.example;

import io.github.eternalstone.annotation.EnableGlobalSensitiveBye;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * to do something
 *
 * @author Justzone on 2022/10/9 16:51
 */
@EnableGlobalSensitiveBye
@SpringBootApplication
public class SensitiveByeExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SensitiveByeExampleApplication.class, args);
    }

}
