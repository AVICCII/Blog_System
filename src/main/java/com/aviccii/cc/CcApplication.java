package com.aviccii.cc;

import com.aviccii.cc.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableSwagger2
@SpringBootApplication
public class CcApplication {

    public static void main(String[] args) {
        SpringApplication.run(CcApplication.class, args);
    }


    @Bean
    public IdWorker createIdWorker(){
        return new IdWorker(0,0);
    }

}
