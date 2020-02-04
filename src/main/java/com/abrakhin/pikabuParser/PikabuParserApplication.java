package com.abrakhin.pikabuParser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
public class PikabuParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(PikabuParserApplication.class, args);
	}

}
