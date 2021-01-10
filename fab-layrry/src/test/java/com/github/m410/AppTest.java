package com.github.m410;


import org.junit.jupiter.api.Test;
import org.moditect.layrry.Layers;

final class AppTest {
    @Test
    void shouldAnswerWithTrue() {
        Layers layers = Layers.builder()
                .layer("app")
                .withModule("com.example:app:1.0.0")
                .build();

        layers.run("com.example.app/com.example.app.App", "Alice");
    }
}
