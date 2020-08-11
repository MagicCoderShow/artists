package com.izrbh.artists;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.izrbh.artists.mapper")
public class ArtistsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtistsApplication.class, args);
    }

}
