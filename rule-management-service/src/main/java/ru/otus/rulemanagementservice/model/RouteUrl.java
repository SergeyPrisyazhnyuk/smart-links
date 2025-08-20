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

    @Column(name = "route_rule_id")
    private Long routeRuleId;

    @Column(name = "destination_url")
    private String destinationURL;

}
