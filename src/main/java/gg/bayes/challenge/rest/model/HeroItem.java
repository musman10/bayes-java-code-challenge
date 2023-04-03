package gg.bayes.challenge.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
public class HeroItem {
    String item;
    Long timestamp;
    @JsonCreator
    public HeroItem(@JsonProperty("item") String item, @JsonProperty("timestamp") long timestamp){
        this.item = item;
        this.timestamp = timestamp;
    }
}
