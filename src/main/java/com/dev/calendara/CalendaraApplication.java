package com.dev.calendara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class CalendaraApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendaraApplication.class, args);
    }

}
