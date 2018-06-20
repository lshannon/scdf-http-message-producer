package com.lukeshannon.scdf.SimpleMessageProducer;

import java.util.Date;
import javax.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.client.RestTemplate;

@EnableTask
@SpringBootApplication
@EnableJms
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
		
		@Value("${message:null}")
		private String message;
		
		@Value("${numberOfMessages}")
		@Min(1)
		private int numberOfMessages;
		
		@Value("${pojo:false}")
		private boolean pojo;
		
		@Value("${useJms:false}")
		private boolean useJms;
		
		@Value("${jmsDestination:null}")
		private String jmsDestination;
		
		@Autowired
		private JmsTemplate jmsTemplate;
		
		@Bean // Serialize message content to json using TextMessage
	    public MessageConverter jacksonJmsMessageConverter() {
	        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
	        converter.setTargetType(MessageType.TEXT);
	        converter.setTypeIdPropertyName("_type");
	        return converter;
	    }

		@Override
		public void run(String... strings) throws Exception {
				RestTemplate restTemplate = new RestTemplate();
				log.info("Sending " + numberOfMessages + " Using JMS: " + useJms + " Pojo Messages: " + pojo + " Time: " + new Date());
				for (int i = 0; i < numberOfMessages; i++) {
					if (pojo) {
						SimpleMessage simpleMessage =  new SimpleMessage("Simple Message: " + i, i, message);
						if (useJms) {
							jmsTemplate.convertAndSend(jmsDestination, simpleMessage);
							log.info("Sent message: " +  simpleMessage + " to destination " + jmsDestination);
						} else {
							HttpEntity<SimpleMessage> payload = new HttpEntity<SimpleMessage>(simpleMessage);
							restTemplate.postForLocation(endpoint, payload);
							log.info("Sent message: " +  payload.getBody() + " to " + endpoint);
						}
					} else {
						String messageToSend = message + " : " + i;
						if (useJms) {
							jmsTemplate.convertAndSend(jmsDestination, messageToSend);
							log.info("Sent message: " +  messageToSend + " to destination " + jmsDestination);
						} else {
							HttpEntity<String> payload = new HttpEntity<String>(messageToSend + " : " + i);
							restTemplate.postForLocation(endpoint, payload);
							log.info("Sent message: " +  payload.getBody() + " to " + endpoint);
						}
					}
					
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
