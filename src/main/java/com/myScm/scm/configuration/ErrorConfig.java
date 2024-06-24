 package com.myScm.scm.configuration;

 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

 import java.util.Properties;

 @Configuration
 public class ErrorConfig {
 
     @Bean
     public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
         SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();

         Properties exceptionMappings = new Properties();
         exceptionMappings.setProperty("com.myScm.scm.errorHandler.ResourceNotFoundException", "error-404");
         exceptionMappings.setProperty("java.lang.Exception", "error-500");
         resolver.setExceptionMappings(exceptionMappings);

         Properties statusCodes = new Properties();
         statusCodes.setProperty("error-404", "404");
         statusCodes.setProperty("error-500", "500");
         resolver.setStatusCodes(statusCodes);

         return resolver;
     }
 }
