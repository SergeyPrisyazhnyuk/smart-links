package ru.otus.rulemanagementservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "route_url")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RouteUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String route_id;
    private String destinationURL;

}
