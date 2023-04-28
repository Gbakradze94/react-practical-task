package com.reactive.training.service;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.reactive.training.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.reactive.training.model.Sport;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SportDataService {

    private static final String JSON_DATA_URL = "https://sports.api.decathlon.com/sports";
    private final WebClient webClient = WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 100)) // 100 MB
            .build();
    private final SportRepository sportRepository;

   public Flux<Sport> saveData() {
       return parseJsonData(new Gson())
               .flatMap(sportRepository::save)
               .doOnNext(sport -> log.info("Saved sport: " + sport))
               .doOnError(error -> log.error("Cannot save sport: ", error));
   }

    public Flux<Sport> parseJsonData(Gson gson) {
        return webClient.get()
                .uri(JSON_DATA_URL)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMapMany(response -> Flux.fromIterable(gson.fromJson(response, JsonObject.class)
                        .get("data").getAsJsonArray())).map(JsonElement::getAsJsonObject)
                .map(sport -> Optional.ofNullable(sport.get("attributes"))
                        .map(JsonElement::getAsJsonObject)
                        .map(this::mapJsonToSportModel))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private Sport mapJsonToSportModel(JsonObject jsonObject) {
       JsonElement name = jsonObject.get("name");
       if(name.isJsonNull()) {
           return null;
       }
       return Sport.builder()
               .name(name.getAsString())
               .build();
    }
}
