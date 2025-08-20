package ru.otus.rulemanagementservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "rule")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class RouteRule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String device;

    @Column(nullable = false)
    private String browser;

    @Column(nullable = false)
    private String region;
}