package com.api.mercadeando;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
//@Import(SwaggerConfiguration.class)
public class MercadeandoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MercadeandoApplication.class, args);
	}

}
