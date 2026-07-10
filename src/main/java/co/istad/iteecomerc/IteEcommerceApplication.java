package co.istad.iteecomerc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class IteEcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IteEcommerceApplication.class, args);
    }

}
