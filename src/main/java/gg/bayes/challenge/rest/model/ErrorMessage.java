package gg.bayes.challenge.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class ErrorMessage {
    private int statusCode;
    private String message;
}
