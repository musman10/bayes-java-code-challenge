package gg.bayes.challenge.rest.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HeroKills {
    String hero;
    Integer kills;
}
