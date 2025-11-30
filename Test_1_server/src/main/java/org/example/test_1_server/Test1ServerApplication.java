package org.example.test_1_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class Test1ServerApplication {

    public static void main(String[] args) {

        // vytvorí tabuľku users, ak ešte nie je
        DBManager.createTables();
        SpringApplication.run(Test1ServerApplication.class, args);
    }
}
