package ru.otus.pagerenderingservice.model;

import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Template {
    private Integer id;
    private List<Component> components;
}
