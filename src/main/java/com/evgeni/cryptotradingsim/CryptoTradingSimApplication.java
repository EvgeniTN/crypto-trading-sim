package com.evgeni.cryptotradingsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoTradingSimApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoTradingSimApplication.class, args);
    }

}
