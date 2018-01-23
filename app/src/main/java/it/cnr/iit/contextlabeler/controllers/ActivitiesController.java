package it.cnr.iit.contextlabeler.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.cnr.iit.contextlabeler.adapters.ActivityElement;

import it.cnr.iit.contextlabeler.R;

/**
 * Created by mattia on 18/01/18.
 */

public class ActivitiesController {

    public static List<ActivityElement> getActivities(){

        List<ActivityElement> activities = new ArrayList<>();
        activities.add(new ActivityElement(R.drawable.trip, "Trip"));
        activities.add(new ActivityElement(R.drawable.relax, "Relax"));
        activities.add(new ActivityElement(R.drawable.cinema, "Cinema"));
        activities.add(new ActivityElement(R.drawable.theater, "Theater"));
        activities.add(new ActivityElement(R.drawable.nightlife, "Nightlife"));
        activities.add(new ActivityElement(R.drawable.fast_food, "Fast Food"));
        activities.add(new ActivityElement(R.drawable.eating, "Eating"));
        activities.add(new ActivityElement(R.drawable.wine_bar_pub, "Wine bar/Pub"));
        activities.add(new ActivityElement(R.drawable.driving, "Driving"));
        activities.add(new ActivityElement(R.drawable.study, "Studying"));
        activities.add(new ActivityElement(R.drawable.reading, "Reading"));
        activities.add(new ActivityElement(R.drawable.tv, "Watching TV"));
        activities.add(new ActivityElement(R.drawable.shopping, "Shopping"));
        activities.add(new ActivityElement(R.drawable.physical_activity, "Physical exercise"));
        activities.add(new ActivityElement(R.drawable.free_time, "Free time"));
        activities.add(new ActivityElement(R.drawable.work, "Working"));
        activities.add(new ActivityElement(R.drawable.restaurant, "Restaurant"));
        activities.add(new ActivityElement(R.drawable.coffee_break, "Break"));
        activities.add(new ActivityElement(R.drawable.sleep, "Sleep"));
        activities.add(new ActivityElement(R.drawable.home, "Home"));

        Collections.sort(activities);

        return activities;
    }
}
