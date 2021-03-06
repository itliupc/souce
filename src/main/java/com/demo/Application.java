package com.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序入口
 *
 */
@SpringBootApplication
@MapperScan("com.demo.mapper")
public class Application {
    public static void main( String[] args ) {
        SpringApplication.run(Application.class, args);
    }
}
