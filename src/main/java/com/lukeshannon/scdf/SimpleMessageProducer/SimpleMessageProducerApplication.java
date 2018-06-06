package com.lukeshannon.scdf.SimpleMessageProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@EnableTask
@SpringBootApplication
public class SimpleMessageProducerApplication {
	
	private static final Logger log = LoggerFactory.getLogger(SimpleMessageProducerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SimpleMessageProducerApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return new SimpleMessageRunner();
	}

	public static class SimpleMessageRunner implements CommandLineRunner {

		/*
		 * To get the code to compile a valid looking URL needs be set
		 */
		@Value("${endpoint:null}")
		private String endpoint;

		@Override
		public void run(String... strings) throws Exception {
			RestTemplate restTemplate = new RestTemplate();
			log.info("Ready to send messges to : " + endpoint);
			if (endpoint != null) {
				for (int i = 0; i < 10000; i++) {
					HttpEntity<String> payload = new HttpEntity<String>("A number for you! : " + i);
					restTemplate.postForLocation(endpoint, payload);				
				}
			}
			else {
				log.error("Endpoint value is not set. No messages sent");
			}
		}
	}

}
