package com.reactive.training;

import com.reactive.training.model.Sport;
import com.reactive.training.service.SportDataService;
import com.reactive.training.service.SportService;
import com.reactive.training.service.SportServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class SportController {

    private final SportService sportService;

    private final SportDataService sportDataService;

    public Mono<ServerResponse> init() {
        return ServerResponse
                .status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromProducer(sportDataService.saveData(), Sport.class));
    }

    public Mono<ServerResponse> createSport(ServerRequest serverRequest) {
        return sportService.createSport(serverRequest.pathVariable("sportName"))
                .flatMap(sport -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(sport))
                .onErrorResume(error -> Mono.just(error.getMessage())
                        .flatMap(e -> ServerResponse.badRequest()
                                .bodyValue(e)));
    }
}
