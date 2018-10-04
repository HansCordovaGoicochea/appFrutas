package tesis.frutas.com.appfrutas.utils;

import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static final int FIRST = 1;
    public static final int LAST = 2;
    public static final int OK = 0;
    public static final int OUT = 3;

    public static int isMonthIncluded(int month, int first, int last) {
        if (last < first) {
            last += 12;
            if (month < first) {
                month += 12;
            }
        }
        if (first == 1 && last == 12) {
            return 0;
        }
        if (first < month && month < last) {
            return 0;
        }
        if (first == month) {
            return 1;
        }
        if (last == month) {
            return 2;
        }
        return 3;
    }

    public static int getMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.MONTH) + 1;
    }

    public static String reemplazarCaracteresRaros(String input) {
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i = 0; i < original.length(); i++) {
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }
        return output;
    }


    public static String getFirstWord(String text) {
        if (text.indexOf(32) > -1) {
            return text.substring(0, text.indexOf(32));
        }
        return text;
    }
}
