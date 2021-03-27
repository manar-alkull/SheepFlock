package com.example.sheepflock;

public class Settings {
    private static Settings currentSettings=new Settings();
    public static Settings Singleton(){
        return currentSettings;
    }
    private Settings() {
    }

    public int feedPeriod_seconds =5;//seconds
    public int remindPeriod=2;//millisec
}
