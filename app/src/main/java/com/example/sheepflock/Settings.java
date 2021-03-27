package com.example.sheepflock;

import java.io.Serializable;

public class Settings implements Serializable {
    //private static Settings currentSettings=new Settings();
/*    public static Settings Singleton(){
        return currentSettings;
    }*/
    public Settings() {
    }

    public int feedPeriod_seconds =5;//seconds
    public int remindPeriod=2000;//millisec
}
