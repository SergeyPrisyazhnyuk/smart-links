package ru.otus.routingservice.model;

import lombok.*;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Context {
    private String device;
    private String browser;
    private String region;

    @Override
    public String toString() {
        return "Context{" +
                "device='" + device + '\'' +
                ", browser='" + browser + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
