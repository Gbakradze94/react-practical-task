package com.reactive.training.service;

import com.reactive.training.model.Sport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SportDataService {
    Flux<Sport> saveData();
    Mono<Void> backPressureInit();
}
