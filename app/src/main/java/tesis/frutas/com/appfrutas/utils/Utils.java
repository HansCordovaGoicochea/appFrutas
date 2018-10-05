package tesis.frutas.com.appfrutas.utils;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

import tesis.frutas.com.appfrutas.R;

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

    public static String monthToString(long l, Context context) {
        switch ((int) l) {
            case 1:
                return context.getResources().getString(R.string.Enero);
            case 2:
                return context.getResources().getString(R.string.Febrero);
            case 3:
                return context.getResources().getString(R.string.Marzo);
            case 4:
                return context.getResources().getString(R.string.Abril);
            case 5:
                return context.getResources().getString(R.string.Mayo);
            case 6:
                return context.getResources().getString(R.string.Junio);
            case 7:
                return context.getResources().getString(R.string.Julio);
            case 8:
                return context.getResources().getString(R.string.Agosto);
            case 9:
                return context.getResources().getString(R.string.Septiembre);
            case 10:
                return context.getResources().getString(R.string.Octubre);
            case 11:
                return context.getResources().getString(R.string.Noviembre);
            case 12:
                return context.getResources().getString(R.string.Diciembre);
            default:
                return "El mes no es valido";
        }
    }
}
