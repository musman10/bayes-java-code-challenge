package gg.bayes.challenge.rest.helper;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.util.Utility;

public class LogParser {

    public static MatchEntity matchEntity;
    public static CombatLogEntryEntity parseLogLine(String logLine){
        CombatLogEntryEntity.Type logType = getLogType(logLine);
        if(logType == null){
            return null;
        }

        switch (logType){
            case ITEM_PURCHASED:
                return parseItemPurchase(logLine);
            case SPELL_CAST:
                return parseSpellCast(logLine);
            case DAMAGE_DONE:
                return parseDamageDone(logLine);
            case HERO_KILLED:
                return parseHeroKilled(logLine);
            default:
                return null;
        }
    }

    public static CombatLogEntryEntity.Type getLogType(String logLine){
        if(logLine.contains("buys")){
            return CombatLogEntryEntity.Type.ITEM_PURCHASED;
        }
        if(logLine.contains("hits")){
            return CombatLogEntryEntity.Type.DAMAGE_DONE;
        }
        if(logLine.contains("casts")){
            return CombatLogEntryEntity.Type.SPELL_CAST;
        }
        if(logLine.contains("killed")){
            return CombatLogEntryEntity.Type.HERO_KILLED;
        }
        return null;
    }

    public static CombatLogEntryEntity parseItemPurchase(String logLine){
        String[] logContents = logLine.split(" ");
        String timestamp = logContents[0];
        String actor = logContents[1];
        String item = logContents[4];
        timestamp = Utility.trimLogTimestamp(timestamp);

        CombatLogEntryEntity combatLogEntry = new CombatLogEntryEntity();
        combatLogEntry.setTimestamp(Utility.convertTimeContentToNanoseconds(timestamp));
        combatLogEntry.setActor(actor);
        combatLogEntry.setItem(item);
        combatLogEntry.setType(CombatLogEntryEntity.Type.ITEM_PURCHASED);
        combatLogEntry.setMatch(matchEntity);
        return combatLogEntry;
    }

    public static CombatLogEntryEntity parseSpellCast(String logLine){
        String[] logContents = logLine.split(" ");
        String timestamp = logContents[0];
        String actor = logContents[1];
        String ability = logContents[4];
        String abilityLevel = logContents[6];
        String target = logContents[8];

        timestamp = Utility.trimLogTimestamp(timestamp);
        CombatLogEntryEntity combatLogEntry = new CombatLogEntryEntity();
        combatLogEntry.setTimestamp(Utility.convertTimeContentToNanoseconds(timestamp));
        combatLogEntry.setActor(actor);
        combatLogEntry.setAbility(ability);
        combatLogEntry.setAbilityLevel(Utility.parseAbilityLevel(abilityLevel));
        combatLogEntry.setTarget(target);
        combatLogEntry.setType(CombatLogEntryEntity.Type.SPELL_CAST);
        combatLogEntry.setMatch(matchEntity);
        return combatLogEntry;
    }

    public static CombatLogEntryEntity parseDamageDone(String logLine){
        String[] logContents = logLine.split(" ");
        String timestamp = logContents[0];
        String actor = logContents[1];
        String target = logContents[3];
        String toolUsed = logContents[5];
        String damage = logContents[7];

        timestamp = Utility.trimLogTimestamp(timestamp);
        CombatLogEntryEntity combatLogEntry = new CombatLogEntryEntity();
        combatLogEntry.setTimestamp(Utility.convertTimeContentToNanoseconds(timestamp));
        combatLogEntry.setActor(actor);
        combatLogEntry.setTarget(target);
        combatLogEntry.setAbility(!toolUsed.equalsIgnoreCase("dota_unknown") ? !toolUsed.startsWith("item") ? toolUsed : null : null );
        combatLogEntry.setItem(toolUsed.startsWith("item") ? toolUsed : null);
        combatLogEntry.setDamage(Integer.valueOf(damage));
        combatLogEntry.setType(CombatLogEntryEntity.Type.DAMAGE_DONE);
        combatLogEntry.setMatch(matchEntity);
        return combatLogEntry;
    }

    public static CombatLogEntryEntity parseHeroKilled(String logLine){
        String[] logContents = logLine.split(" ");
        String timestamp = logContents[0];
        String actor = logContents[5];
        String target = logContents[1];

        timestamp = Utility.trimLogTimestamp(timestamp);
        CombatLogEntryEntity combatLogEntry = new CombatLogEntryEntity();
        combatLogEntry.setTimestamp(Utility.convertTimeContentToNanoseconds(timestamp));
        combatLogEntry.setActor(actor);
        combatLogEntry.setTarget(target);
        combatLogEntry.setType(CombatLogEntryEntity.Type.HERO_KILLED);
        combatLogEntry.setMatch(matchEntity);
        return combatLogEntry;
    }
}
