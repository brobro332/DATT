package xyz.datt.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${domain.agent}")
    private String agentDomain;

    @Bean
    public WebClient dataGoWebClient() {
        return WebClient.builder()
            .baseUrl("https://apis.data.go.kr")
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(10 * 1024 * 1024)
            )
            .build();
    }

    @Bean
    public WebClient agentWebClient() {
        return WebClient.builder()
            .baseUrl(agentDomain)
            .build();
    }
}
