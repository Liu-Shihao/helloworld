package com.lsh.controller;

/**
 * @Author: LiuShihao
 * @Date: 2023/1/4 17:47
 * @Desc:
 */
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        String format = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        log.info("- - - - Now is "+ format);
        return "Hello, World! "+format;
    }
}
