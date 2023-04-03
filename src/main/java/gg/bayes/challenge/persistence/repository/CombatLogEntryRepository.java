package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.rest.model.HeroKills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@Repository
public interface CombatLogEntryRepository extends JpaRepository<CombatLogEntryEntity, Long> {
    @Query(value = "select actor as hero, count(id ) as kills from dota_combat_log where entry_type = 'HERO_KILLED' and match_id = :matchId group by actor", nativeQuery = true)
    public List<Tuple> findHeroKillsByMatchId(long matchId);

    @Query(value = "select item, entry_timestamp from DOTA_COMBAT_LOG where match_id = :matchId and actor = :hero and entry_type= 'ITEM_PURCHASED' order by entry_timestamp, item asc", nativeQuery = true)
    public List<Tuple> findHeroItemsByHeroName(long matchId, String hero);

    @Query(value = "select ability as spell, count(id ) as casts from dota_combat_log where entry_type = 'SPELL_CAST' and match_id = :matchId and actor = :hero group by ability", nativeQuery = true)
    public List<Tuple> findHeroSpellsByHeroName(long matchId, String hero);

    @Query(value = "select target, count(id ) as damage_instances, sum(damage) from dota_combat_log where entry_type = 'DAMAGE_DONE' and match_id = :matchId and actor = :hero group by target", nativeQuery = true)
    public List<Tuple> findHeroDamagesByHeroName(long matchId, String hero);

}
