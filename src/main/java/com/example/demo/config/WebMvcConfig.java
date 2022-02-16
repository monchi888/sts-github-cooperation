package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @EnableWebMvc ★★★この表記はspring boot2以降では不要★★★
// 理由はcssなどが読みこめなくなるため
// This annotationn is not needed in spring boot2
/*
 * @Configuration が1stApp側で必要か否かが不明
 * 次のURLを読む限り、bean定義用クラスであれば今回は不要かもとも思っている
 * https://learning-collection.com/spring-boot%E3%81%A7%E3%82%88%E3%81%8F%E4%BD%BF%E3%81%86%E3%82%A2%E3%83%8E%E3%83%86%E3%83%BC%E3%82%B7%E3%83%A7%E3%83%B3%E4%B8%80%E8%A6%A7/
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

}