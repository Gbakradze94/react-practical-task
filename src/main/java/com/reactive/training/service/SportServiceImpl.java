package com.reactive.training.service;


import com.reactive.training.model.Sport;
import com.reactive.training.repository.SportRepository;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SportServiceImpl implements SportService {

    private final SportRepository sportRepository;


    public Mono<Sport> getSportById(Integer sportId) {
        return sportRepository.findById(sportId);
    }

    @Override
    public Mono<Sport> createSport(String name) {
        return sportRepository.findSportByName(name)
                .flatMap(sport -> Mono.error(new R2dbcDataIntegrityViolationException("Sport name already exists")))
                .switchIfEmpty(sportRepository.save(new Sport(name)))
                .cast(Sport.class);
    }
}
