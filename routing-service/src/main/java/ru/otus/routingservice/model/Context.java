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

}
