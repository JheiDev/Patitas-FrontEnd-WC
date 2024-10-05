package pe.edu.cibertec.Patitas_FrontEnd_wc.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClientAuthentication(WebClient.Builder builder) {
        return builder
                .build();
    }
}
