package com.example.sheepflock;

import com.example.sheepflock.system.SheepContentManager;

import java.io.Serializable;
import java.util.Calendar;

public class Sheep implements Serializable {
    public Sheep() {
    }

    public Sheep(int id, String nikName, float weight, Calendar lastFeedDate, double latitude, double longitude) {
        this.id = id;
        this.nikName = nikName;
        this.weight = weight;
        this.lastFeedDate = lastFeedDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int id;
    public String nikName;
    public float weight;
    public Calendar lastFeedDate;
    public double latitude;
    public double longitude;

    public boolean isHungry(){
        Settings settings=new SheepContentManager(MainActivity.mainActivityContext).getSetting();
        return (Calendar.getInstance().getTimeInMillis()-this.lastFeedDate.getTimeInMillis())>(settings.feedPeriod_seconds*1000);
    }

}
