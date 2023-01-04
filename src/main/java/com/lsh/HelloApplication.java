package com.lsh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: LiuShihao
 * @Date: 2023/1/4 17:51
 * @Desc:
 */
@Slf4j
@SpringBootApplication
public class HelloApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class,args);
        log.info("************* HelloApplication Start Up! *****************");
    }
}
