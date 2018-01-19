package matbell.it.contextlabeler.adapters;

import android.support.annotation.NonNull;

public class ActivityElement implements Comparable<ActivityElement>{

    public int activityIconRes;
    public String activityLabel;
    public boolean selected = false;


    public ActivityElement(int activityIconRes, String activityLabel){

        this.activityIconRes = activityIconRes;
        this.activityLabel = activityLabel;
    }

    @Override
    public int compareTo(@NonNull ActivityElement o) {
        return this.activityLabel.compareTo(o.activityLabel);
    }
}
