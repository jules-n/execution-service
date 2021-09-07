package com.ynero.ss.execution.config;

import groovy.lang.GroovyShell;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroovyShellConfig {

    @Bean
    public GroovyShell getGroovyShell(){
        return new GroovyShell();
    }
}
