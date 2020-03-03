package br.com.restful;

import br.com.restful.config.FileStorageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageConfig.class})
public class SpringRestfulApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRestfulApiApplication.class, args);
    }

}
