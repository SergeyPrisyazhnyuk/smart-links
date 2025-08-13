package ru.otus.rulemanagementservice.model;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Plugin {
    private String name;
    private String description;

}
