package ru.otus.pagerenderingservice.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageInfo {
    private String device;
    private String browser;
    private String region;
}