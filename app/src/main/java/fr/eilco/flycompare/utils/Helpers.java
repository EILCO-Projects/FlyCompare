package fr.eilco.flycompare.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

public class Helpers {
    public static void storeLocally(Context context, String key, String val) {
        // Stocker une valeur
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static String loadFromLocal(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Keys.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static String capitalize(String text) {
        int index = 0;
        text = text.toLowerCase();
        while (index < text.length()) {
            if (Arrays.asList(new String[]{"foo", "bar"}).contains(text.charAt(index))) {
                break;
            }
            index += 1;
        }
        if (index < text.length()) {
            if (index < text.length() - 1) {
                return text.substring(0, index) + text.toUpperCase().charAt(index) + text.substring(index + 1);
            } else {
                return text.substring(0, index) + text.toUpperCase().charAt(index);
            }

        }

        return text;
    }

    public static String formatDateFromCalendar(int date) {
        return Arrays.asList(new String[]{"janv.", "févr.", "mars", "Avr.", "Mai", "Juin", "Juil.", "Août", "Sept.", "Oct.", "Nov.", "Déc."}).get(date);
    }



    public static String getFlightClass(KEnum.FlightClassTypeEnum type) {
        if(type == KEnum.FlightClassTypeEnum.CABIN_CLASS_FIRST) {
            return "Première";
        } else if(type == KEnum.FlightClassTypeEnum.CABIN_CLASS_BUSINESS) {
            return "Business";
        } else if(type == KEnum.FlightClassTypeEnum.CABIN_CLASS_PREMIUM_ECONOMY) {
            return "H. Eco";
        }
        return "Economique";
    }

    public static String convertMinutesToString(int totalMinutes) {
        int days = totalMinutes / (24 * 60);
        int hours = (totalMinutes % (24 * 60)) / 60;
        int minutes = totalMinutes % 60;

        StringBuilder builder = new StringBuilder();

        if (days > 0) {
            builder.append(days).append("j. ");
        }
        if (hours > 0) {
            builder.append(hours).append("h. ");
        }
        if (minutes > 0) {
            builder.append(minutes).append("m. ");
        }

        return builder.toString().trim();
    }


}
