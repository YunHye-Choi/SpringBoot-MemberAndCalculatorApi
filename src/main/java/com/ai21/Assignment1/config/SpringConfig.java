package com.ai21.Assignment1.config;

import com.ai21.Assignment1.repository.UserRepository;
import com.ai21.Assignment1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class SpringConfig {

    private final UserRepository userRepository;

    @Autowired
    public SpringConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
/*

    @Bean
    public UserService userService(){
        return new UserService(userRepository);
    }
*/
}
