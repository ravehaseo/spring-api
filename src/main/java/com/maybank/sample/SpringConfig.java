package com.maybank.sample;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:spring/mvc-core-config-override.xml")
public class SpringConfig {

}
