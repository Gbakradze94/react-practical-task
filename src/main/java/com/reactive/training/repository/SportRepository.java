package com.reactive.training.repository;

import com.reactive.training.model.Sport;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SportRepository extends ReactiveCrudRepository<Sport, Integer> {
    Mono<Sport> findSportByName(String name);
}
