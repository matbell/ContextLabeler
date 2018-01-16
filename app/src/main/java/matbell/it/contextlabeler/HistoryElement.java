package matbell.it.contextlabeler;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryElement implements Comparable<HistoryElement>{

    String activityName;
    String start;
    String duration;
    long startActivity, endActivity;

    HistoryElement(String activityName, long start, long end){

        this.activityName = activityName;
        this.startActivity = start;
        this.endActivity = end;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        this.start = sdf.format(new Date(start));
        this.duration = getDifference(new Date(start), new Date(end));
    }

    public String toString(){
        return startActivity + LogManager.LOG_SEP + endActivity + LogManager.LOG_SEP + activityName;
    }

    //1 minute = 60 seconds
    // 1 hour = 60 x 60 = 3600
    // 1 day = 3600 x 24 = 86400
    private String getDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String diff = "";

        if(elapsedDays > 0)
            diff = String.format(Locale.getDefault(), "%d:%d days", elapsedDays,
                    elapsedHours);

        else if(elapsedHours > 0)
            diff = String.format(Locale.getDefault(), "%d:%d hours", elapsedHours,
                    elapsedMinutes);

        else if(elapsedMinutes > 0)
            diff = String.format(Locale.getDefault(), "%d:%d minutes", elapsedMinutes,
                    elapsedSeconds);

        else if(elapsedSeconds > 0)
            diff = String.format(Locale.getDefault(), "%d seconds", elapsedSeconds);

        return diff;
    }

    @Override
    public int compareTo(@NonNull HistoryElement o) {
        return (this.activityName+this.startActivity).compareTo(o.activityName+o.startActivity);
    }
}
