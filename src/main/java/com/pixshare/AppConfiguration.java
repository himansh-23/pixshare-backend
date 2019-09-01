package com.pixshare;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import static springfox.documentation.builders.PathSelectors.regex;


@Configuration
@EnableSwagger2
public class AppConfiguration {
	
	public static String exchangeForEmail="emailexchange";
	public static String queueForEmail="emailqueue";
	
	@Bean
	public PasswordEncoder getPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ModelMapper getModelMapper()
	{
		return new ModelMapper();
	}
	
	@Bean
	public Queue queueBuild()
	{
		return new Queue(queueForEmail, true);
	}
	
	@Bean
	public FanoutExchange exchangeBuild()
	{
		return new FanoutExchange(exchangeForEmail, true, false);
	}
	
	@Bean
	public Binding binding(Queue queue,FanoutExchange exchange)
	{
		return BindingBuilder.bind(queue).to(exchange);
	}
	
	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueForEmail);
        container.setMessageListener(listenerAdapter);
        return container;
	}
	
	 @Bean
	    MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
	        return new MessageListenerAdapter(receiver, "emailSend");
	    }
	 
	 @Bean
	 public Docket productApi() {
		 return new Docket(DocumentationType.SWAGGER_2)
				 .select()
				 .apis(RequestHandlerSelectors.basePackage("com.pixshare.controller"))
				 .paths(regex("/user/api.*")) //or PathSelector.any()
				 .build();
	 }

}
