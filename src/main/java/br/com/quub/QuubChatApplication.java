package br.com.quub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class QuubChatApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(QuubChatApplication.class, args);
	}
}