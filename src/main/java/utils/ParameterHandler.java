package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ParameterHandler {


    public static int tryParseInteger(String string) {
        int value = -1;
        try {
            value = Integer.parseInt(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static LocalDate tryParseDate(DateTimeFormatter formatter, String dateString) {
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateString, formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
