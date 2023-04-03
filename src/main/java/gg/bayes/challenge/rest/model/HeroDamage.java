package gg.bayes.challenge.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HeroDamage {
    String target;
    Integer damageInstances;
    Integer totalDamage;
    @JsonCreator
    public HeroDamage(@JsonProperty("target") String target, @JsonProperty("damage_instances") Integer damageInstances, @JsonProperty("total_damage") Integer totalDamage){
        this.target = target;
        this.damageInstances = damageInstances;
        this.totalDamage = totalDamage;
    }
}
