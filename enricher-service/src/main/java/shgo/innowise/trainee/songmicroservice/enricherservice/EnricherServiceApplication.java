package shgo.innowise.trainee.songmicroservice.enricherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EnricherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnricherServiceApplication.class, args);
	}

}
