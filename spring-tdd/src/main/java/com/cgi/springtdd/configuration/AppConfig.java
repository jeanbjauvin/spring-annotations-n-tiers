package com.cgi.springtdd.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

@Configuration
@ComponentScan(basePackages = "com.cgi.springtdd")
@PropertySource("classpath:application.properties")
public class AppConfig {

    private static final Logger logger = LogManager.getLogger("AppConfig");

    public @Bean
    CouchbaseEnvironment couchbaseEnvironment() {
        logger.debug("Creating default Couchbase environment");
        return DefaultCouchbaseEnvironment.create();
    }
}
