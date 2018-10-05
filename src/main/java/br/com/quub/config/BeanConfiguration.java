package br.com.quub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.quub.model.storage.ChatStorage;

@Configuration
public class BeanConfiguration {
  
  @Bean
  public ChatStorage customerChatStorage() {
    return new ChatStorage();
  }
}