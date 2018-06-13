package com.lukeshannon.scdf.SimpleMessageProducer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
		@Value("${endpoint}")
		@NotNull
		@Size(min=1)
		private String endpoint;
		
		@Value("${message}")
		@NotNull
		@Size(min=1)
		private String message;
		
		@Value("${numberOfMessages}")
		@Min(1)
		private int numberOfMessages;
		
		@Value("${complexMessage}")
		private boolean complexMessage;

		@Override
		public void run(String... strings) throws Exception {
			RestTemplate restTemplate = new RestTemplate();
			log.info("Ready to send messges to : " + endpoint);
			if (endpoint != null) {
				for (int i = 0; i < numberOfMessages; i++) {
					if (complexMessage) {
						HttpEntity<SimpleMessage> payload = new HttpEntity<SimpleMessage>(new SimpleMessage("Simple Message: " + i, i, message));
						restTemplate.postForLocation(endpoint, payload);
						log.info("Sent message: " +  payload.getBody() + " to " + endpoint);
					}
					else {
						HttpEntity<String> payload = new HttpEntity<String>(message + " : " + i);
						restTemplate.postForLocation(endpoint, payload);
						log.info("Sent message: " +  payload.getBody() + " to " + endpoint);
					}
				}
			}
			else {
				log.error("Endpoint value is not set. No messages sent");
			}
		}
		
		class SimpleMessage {
			
			private String name;
			private int id;
			private String description;
			
			public SimpleMessage(String name, int id, String description) {
				this.name = name;
				this.id = id;
				this.description = description;
			}
			
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public int getId() {
				return id;
			}
			public void setId(int id) {
				this.id = id;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
		}
	}

}
