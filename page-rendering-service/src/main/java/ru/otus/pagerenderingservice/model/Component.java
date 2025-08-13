package ru.otus.pagerenderingservice.model;

import lombok.*;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Component {
    private String type;
    private String content;
}