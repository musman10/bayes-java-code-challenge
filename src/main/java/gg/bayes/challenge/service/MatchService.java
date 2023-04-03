package gg.bayes.challenge.service;

import gg.bayes.challenge.exception.ResourceNotFoundException;
import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.persistence.repository.CombatLogEntryRepository;
import gg.bayes.challenge.persistence.repository.MatchRepository;
import gg.bayes.challenge.rest.helper.Constants;
import gg.bayes.challenge.rest.helper.Errors;
import gg.bayes.challenge.rest.helper.LogParser;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.scanner.Constant;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private CombatLogEntryRepository combatLogEntryRepository;

    @Transactional
    public MatchEntity saveCombatLogs(String combatLog){
        MatchEntity matchEntity = matchRepository.save(new MatchEntity());

        LogParser.matchEntity = matchEntity;
        Set<CombatLogEntryEntity> logEntryEntities = combatLog.lines()
                .map(LogParser::parseLogLine)
                .filter(log -> log != null)
                .collect(Collectors.toSet());

        if(logEntryEntities == null){
            return matchEntity;
        }

        combatLogEntryRepository.saveAll(logEntryEntities);
        return matchEntity;
    }

    public List<HeroKills> getMatch(Long matchId){
        matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException(Errors.MATCH_NOT_FOUND.getErrorMessage() + matchId));

        List<Tuple> heroKillsTuples = combatLogEntryRepository.findHeroKillsByMatchId(matchId);
        List<HeroKills> heroKillsList = heroKillsTuples.stream()
                .filter(tuple -> tuple.get(0, String.class).startsWith(Constants.COMBAT_LOG_HERO_PREFIX))
                .map(filteredTuple -> HeroKills.builder()
                        .kills(filteredTuple.get(1,BigInteger.class).intValue())
                        .hero(filteredTuple.get(0, String.class).replace(Constants.COMBAT_LOG_HERO_PREFIX,""))
                        .build())
                .collect(Collectors.toList());
        return heroKillsList;
    }

    public List<HeroItem> getHeroItems(long matchId, String heroName) {
        heroName = Constants.COMBAT_LOG_HERO_PREFIX + heroName;
        List<Tuple> heroItemTuples = combatLogEntryRepository.findHeroItemsByHeroName(matchId, heroName);
        List<HeroItem> heroItems= heroItemTuples.stream()
                .map(itemTuple -> HeroItem.builder()
                        .item(itemTuple.get(0, String.class))
                        .timestamp(itemTuple.get(1,BigInteger.class).longValue())
                        .build())
                .collect(Collectors.toList());
        return heroItems;
    }

    public List<HeroSpells> getHeroSpells(long matchId, String heroName){
        heroName = Constants.COMBAT_LOG_HERO_PREFIX + heroName;
        List<Tuple> heroItemTuples = combatLogEntryRepository.findHeroSpellsByHeroName(matchId, heroName);
        List<HeroSpells> heroSpells= heroItemTuples.stream()
                .map(itemTuple -> HeroSpells.builder()
                        .spell(itemTuple.get(0, String.class))
                        .casts(itemTuple.get(1,BigInteger.class).intValue())
                        .build())
                .collect(Collectors.toList());
        return heroSpells;
    }

    public List<HeroDamage> getHeroDamages(long matchId, String heroName){
        heroName = Constants.COMBAT_LOG_HERO_PREFIX + heroName;
        List<Tuple> heroDamageTuples = combatLogEntryRepository.findHeroDamagesByHeroName(matchId, heroName);
        List<HeroDamage> heroDamages= heroDamageTuples.stream()
                .map(itemTuple -> HeroDamage.builder()
                        .target(itemTuple.get(0, String.class))
                        .damageInstances(itemTuple.get(1,BigInteger.class).intValue())
                        .totalDamage(itemTuple.get(2,BigInteger.class).intValue())
                        .build())
                .collect(Collectors.toList());
        return heroDamages;
    }
}
