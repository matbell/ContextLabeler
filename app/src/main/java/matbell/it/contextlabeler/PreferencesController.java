package matbell.it.contextlabeler;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreferencesController {

    private static final String PREFS = "contextLabeler";
    private static final String PREF_START = "startActivity";
    private static final String PREF_START_ACTIVITY = "activityName";
    private static final String PREF_ACTIVITIES_HISTORY = "activitiesHistory";
    public static final String PREF_SETUP_COMPLETE = "setupComplete";

    static boolean readingIsActive(Context context){
        return getStartReading(context) != -1L;
    }

    static void stopReading(Context context){
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putLong(
                PREF_START, -1L).apply();
    }

    static long getStartReading(Context context){
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getLong(
                PREF_START, -1L);
    }

    static String getActivityName(Context context){
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(
                PREF_START_ACTIVITY, "");
    }

    static void setNewActivity(Context context, long start, String name){
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(
                PREF_START_ACTIVITY, name).apply();
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putLong(
                PREF_START, start).apply();
    }

    static void setActivityHistory(Context context, String activity){

        Set<String> history = context.getSharedPreferences(PREFS,Context.MODE_PRIVATE).getStringSet(
                PREF_ACTIVITIES_HISTORY, new HashSet<String>());

        history.add(activity);

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putStringSet(
                PREF_ACTIVITIES_HISTORY, history).apply();
    }

    static List<String> getActivitiesHistory(Context context){

        Set<String> history = context.getSharedPreferences(PREFS,Context.MODE_PRIVATE).getStringSet(
                PREF_ACTIVITIES_HISTORY, new HashSet<String>());

        return new ArrayList<>(history);
    }

    public static boolean isSetupComplete(Context context){
        return context.getSharedPreferences(PREFS,Context.MODE_PRIVATE).getBoolean(
                PREF_SETUP_COMPLETE, false);
    }

    public static void storeSetupComplete(Context context){
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean(
                PREF_SETUP_COMPLETE, true).apply();
    }
}
