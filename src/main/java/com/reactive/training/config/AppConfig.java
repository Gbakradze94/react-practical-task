package com.reactive.training.config;

import com.reactive.training.SportController;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class AppConfig {
    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        return initializer;
    }

    @Bean
    public RouterFunction<ServerResponse> setRoutes(SportController sportController) {
        log.info("INSIDE init()");
        return RouterFunctions
                .route(RequestPredicates.GET("/sport/initialize"), request -> sportController.init())
                .andRoute(RequestPredicates.POST("/sport/{sportName}"), sportController::createSport)
                .andRoute(RequestPredicates.GET("/sport/{id}"), sportController::getSportById)
                .andRoute(RequestPredicates.GET("/backpressure/sport/initialize"),
                        request -> sportController.backPressureInit());
    }
}
