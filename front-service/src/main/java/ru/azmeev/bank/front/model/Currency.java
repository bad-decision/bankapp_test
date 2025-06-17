package ru.azmeev.bank.front.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Currency {
    private String title;
    private String name;
}
