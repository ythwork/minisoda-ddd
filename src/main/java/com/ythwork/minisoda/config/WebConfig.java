package com.ythwork.minisoda.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages="com.ythwork.minisoda")
public class WebConfig implements WebMvcConfigurer {

}
