package ru.otus.common.model;

import lombok.*;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatchingResult {
    private List<String> urls;

    @Override
    public String toString() {

    return Objects.toString(urls);    }
}