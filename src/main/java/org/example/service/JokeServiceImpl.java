package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.models.api.JokeRS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.security.InvalidParameterException;
import java.util.List;

@Slf4j
@Service
public class JokeServiceImpl implements JokeService {
    private final WebClient apiClient;
    private final Scheduler scheduler;

    public JokeServiceImpl(@Value("${app.jokes-service-url:https://official-joke-api.appspot.com/random_joke}") String jokesServiceUrl) {
        this.apiClient = WebClient.create(jokesServiceUrl);
        this.scheduler = Schedulers.newBoundedElastic(5, 10, "group");
    }

    @Override
    public List<JokeRS> getJokes(Integer count) {
        if (count < 0) {
            throw new InvalidParameterException("Incorrect value");
        }
        if (count > 100) {
            throw new InvalidParameterException("За один раз можно получить не более 100 шуток.");
        }

        return Flux.range(1, count)
                .flatMap(body -> getJoke().onErrorResume(e -> Mono.empty()))
                .publishOn(scheduler)
                .collectList()
                .block();
    }

    private Mono<JokeRS> getJoke() {
        return apiClient
                .get()
                .retrieve()
                .bodyToMono(JokeRS.class);
    }

}
