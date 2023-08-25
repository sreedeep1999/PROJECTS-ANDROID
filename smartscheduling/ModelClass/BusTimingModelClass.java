package com.example.smartscheduling.ModelClass;

public class BusTimingModelClass {

    String id;
    String name;
    String route;
    String timings;

    public BusTimingModelClass(String id, String name, String route, String timings) {
        this.id = id;
        this.name = name;
        this.route = route;
        this.timings = timings;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoute() {
        return route;
    }

    public String getTimings() {
        return timings;
    }
}
