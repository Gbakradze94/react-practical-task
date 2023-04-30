package com.reactive.training;

import com.reactive.training.model.Sport;
import com.reactive.training.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SportSubscriber implements Subscriber<Sport> {
    private static final int NUMBER_OF_ELEMENTS_PER_REQUEST = 20;

    private Subscription subscription;
    private final SportRepository sportRepository;
    private int elementCounter;

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
        subscription.request(NUMBER_OF_ELEMENTS_PER_REQUEST);
    }

    @Override
    public void onNext(Sport sport) {
        sportRepository.save(sport).subscribe();
        elementCounter++;
        if (elementCounter % NUMBER_OF_ELEMENTS_PER_REQUEST == 0) {
            log.info("20 elements consumed and saved");
            subscription.request(NUMBER_OF_ELEMENTS_PER_REQUEST);
        }
    }

    @Override
    public void onError(Throwable t) {
        log.error(t.getMessage());
    }

    @Override
    public void onComplete() {
        log.info("Data init with backpressure completed");
    }
}
