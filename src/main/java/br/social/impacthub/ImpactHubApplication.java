package br.social.impacthub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ImpactHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImpactHubApplication.class, args);
	}

}
