package com.example.sheepflock;

import java.util.Calendar;
import java.util.Date;

public class Sheep {
    public Sheep() {
    }

    public Sheep(String id, String nikName, float weight, Calendar lastFeedDate, String latitude, String altitude) {
        this.id = id;
        this.nikName = nikName;
        this.weight = weight;
        this.lastFeedDate = lastFeedDate;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    public String id;
    public String nikName;
    public float weight;
    public Calendar lastFeedDate;
    public String latitude;
    public String altitude;

}
