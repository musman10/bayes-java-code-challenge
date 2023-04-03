package gg.bayes.challenge.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HeroSpells {
    String spell;
    Integer casts;
    @JsonCreator
    public HeroSpells(@JsonProperty("spell") String spell, @JsonProperty("casts") Integer casts){
        this.spell = spell;
        this.casts = casts;
    }
}
