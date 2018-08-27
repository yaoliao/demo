package com.example.demo;

import com.example.demo.agent.consumer.ConsumerServer;
import com.example.demo.agent.provider.ProviderServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        new Thread(() -> new ConsumerServer().startServer()).start();
        new ProviderServer().startServer();
    }
}
