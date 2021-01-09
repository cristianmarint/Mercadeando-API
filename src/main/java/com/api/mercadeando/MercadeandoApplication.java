package com.api.mercadeando;

import com.api.mercadeando.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories
@Import(SwaggerConfig.class)
public class MercadeandoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MercadeandoApplication.class, args);
	}

}
