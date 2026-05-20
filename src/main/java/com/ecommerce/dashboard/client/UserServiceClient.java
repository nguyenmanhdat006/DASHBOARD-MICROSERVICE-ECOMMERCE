package com.ecommerce.dashboard.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class UserServiceClient {

    private final WebClient userServiceClient;

    public UserServiceClient(@Qualifier("userWebClient") WebClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    public Long countTotalUsers() {
        try {
            return userServiceClient.get()
                    .uri("/api/users/stats/total-count")
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching total users", e);
            return 0L;
        }
    }

    public Long countUsersYesterday() {
        try {
            return userServiceClient.get()
                    .uri("/api/users/stats/count-yesterday")
                    .retrieve()
                    .bodyToMono(Long.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching users yesterday", e);
            return 0L;
        }
    }
}
