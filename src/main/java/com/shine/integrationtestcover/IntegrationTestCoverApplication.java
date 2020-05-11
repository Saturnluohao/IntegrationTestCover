package com.shine.integrationtestcover;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.shine.integrationtestcover.mapper")
public class IntegrationTestCoverApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegrationTestCoverApplication.class, args);
	}

}
