package gg.bayes.challenge.util;

import lombok.experimental.UtilityClass;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class Utility {

    public static long convertTimeContentToNanoseconds(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        LocalTime localTime = LocalTime.parse(time, formatter);
        return localTime.toNanoOfDay();
    }

    public static String trimLogTimestamp(String timestamp){
        return timestamp.substring(1, timestamp.length() - 1);
    }

    public static Integer parseAbilityLevel(String abilityLevel){
//        abilityLevel = abilityLevel.replace("lvl", "");
//        abilityLevel = abilityLevel.replace(" ", "");
        abilityLevel = abilityLevel.substring(0, abilityLevel.length() - 1);
        return Integer.valueOf(abilityLevel);
    }
}
