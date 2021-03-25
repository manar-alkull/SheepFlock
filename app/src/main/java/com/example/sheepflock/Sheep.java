package com.example.sheepflock;

import java.util.Calendar;

public class Sheep {
    public Sheep() {
    }

    public Sheep(String id, String nikName, float weight, Calendar lastFeedDate, double latitude, double longitude) {
        this.id = id;
        this.nikName = nikName;
        this.weight = weight;
        this.lastFeedDate = lastFeedDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String id;
    public String nikName;
    public float weight;
    public Calendar lastFeedDate;
    public double latitude;
    public double longitude;

}
