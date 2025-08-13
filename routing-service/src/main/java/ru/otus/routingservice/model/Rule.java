package ru.otus.routingservice.model;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Rule {
    private long id;
    private String condition;
    private String destinationURL;
}
