package com.sec4.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.SpringVersion;



@SpringBootApplication
@Configuration
@ComponentScan(basePackages = "com.sec4")
public class Lab96633805883Application {

	public static void main(String[] args) {
		SpringApplication.run(Lab96633805883Application.class, args);
		System.out.println("Spring framework version:"+SpringVersion.getVersion());
		System.out.println("Spring boot version:"+SpringBootVersion.getVersion());
	}

}
