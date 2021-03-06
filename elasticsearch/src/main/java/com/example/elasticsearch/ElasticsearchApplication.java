package com.example.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.example.elasticsearch", "com.example.myes"})
public class ElasticsearchApplication {

	public static void main(String[] args) {

		SpringApplication.run(ElasticsearchApplication.class, args);
	}

}
