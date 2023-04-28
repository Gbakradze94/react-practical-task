package com.reactive.training.service;


import com.reactive.training.model.Sport;
import reactor.core.publisher.Mono;

public interface SportService {
    Mono<Sport> createSport(String name);
}
