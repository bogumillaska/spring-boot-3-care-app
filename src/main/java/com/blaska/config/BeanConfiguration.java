package com.blaska.config;

import com.blaska.CareApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CareApplication.class)
public class BeanConfiguration {
}
