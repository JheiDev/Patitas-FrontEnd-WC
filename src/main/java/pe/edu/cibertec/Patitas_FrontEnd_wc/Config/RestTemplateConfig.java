package pe.edu.cibertec.Patitas_FrontEnd_wc.Config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
public class RestTemplateConfig {
    @Bean
        public RestTemplate restTemplateAuthentication(RestTemplateBuilder builder) {
            return builder
                    .rootUri("http://localhost:8081/autentication")
                    .setConnectTimeout(Duration.ofSeconds(5)) // tiempo de espera maximo para la espeade conexión con el servidor
                    .setReadTimeout(Duration.ofSeconds(15)) // tiempo de espera maximo para recibir la respuesta total
                    .build();
        }

    @Bean
    public RestTemplate restTemplateFinanzas(RestTemplateBuilder builder) {
        return builder
                .rootUri("http://localhost:8081/finanzas")
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }
    @Bean
    public RestTemplate restTemplateeporteria(RestTemplateBuilder builder) {
        return builder
                .rootUri("http://localhost:8081/reporteria")
                .setReadTimeout(Duration.ofSeconds(60))
                .build();
    }

}
