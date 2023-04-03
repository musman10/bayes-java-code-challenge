package gg.bayes.challenge.util;

import lombok.experimental.UtilityClass;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This provides global utility operations such as trimming, formatting data, converting time content to nanoseconds
 */
@UtilityClass
public class Utility {

    /**
     * This converts time notation to localTime in nanoseconds
     *
     * @param time time provided in format HH:mm:ss.SSS
     * @return localTime as nanoseconds of the day relevant to provided time
     */
    public static long convertTimeContentToNanoseconds(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        LocalTime localTime = LocalTime.parse(time, formatter);
        return localTime.toNanoOfDay();
    }

    /**
     * This formats timestamp data of log event
     *
     * @param timestamp time provided in format [HH:mm:ss.SSS]
     * @return trimmed time in format HH:mm:ss.SSS
     */
    public static String trimLogTimestamp(String timestamp){
        return timestamp.substring(1, timestamp.length() - 1);
    }

    /**
     * This parses ability level data of the log event
     *
     * @param abilityLevel provided in format (lvl 1)
     * @return parsed ability level number such as 1
     */
    public static Integer parseAbilityLevel(String abilityLevel){
        abilityLevel = abilityLevel.substring(0, abilityLevel.length() - 1);
        return Integer.valueOf(abilityLevel);
    }
}
