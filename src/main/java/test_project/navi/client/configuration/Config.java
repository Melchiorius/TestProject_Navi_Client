package test_project.navi.client.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("test_project.navi.client")
public class Config {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }



}
