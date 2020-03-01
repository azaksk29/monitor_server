package com.bundang.monitor;

import com.bundang.monitor.netty.MonitorDeviceServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MonitorDeviceServerApplication {

    @Autowired
    private MonitorDeviceServer monitorDeviceServer;

    public static void main(String[] args) {
        SpringApplication.run(MonitorDeviceServerApplication.class, args);
    }

    @Bean
    CommandLineRunner myRun() {
        return args -> monitorDeviceServer.start();
    }

}
