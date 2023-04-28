package com.reactive.training.model;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("sport")
public class Sport {
    @Id
    @Nullable
    private Integer id;
    private String name;

    public Sport(String name) {
        this.name = name;
    }
}
