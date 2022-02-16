package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*
 * このアノテーションの記述により、以前はXMLで書く
 * 必要のあった多くの設定を自動的に行えるようになった
 */
public class WebBeginnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebBeginnerApplication.class, args);
	}

}
