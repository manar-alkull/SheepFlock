package com.example.sheepflock.system;

import android.content.Context;

import com.example.sheepflock.Sheep;
import com.example.sheepflock.alarm.AlarmHandler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SheepContentManager extends ContentHandler{

    String fileName="sheeps.bin";
    public SheepContentManager(Context context) {
        super(context, "");
    }

    public Map<Integer,Sheep> getSheeps(){
        if(isFileExist(fileName)) {
            try {
                return (HashMap<Integer,Sheep>) getObject(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new HashMap<Integer,Sheep>();
    }
    public void saveSheep(Sheep sheep){
        HashMap<Integer,Sheep> sheeps=(HashMap<Integer,Sheep>)getSheeps();
        sheeps.put(sheep.id,sheep);
        try {
            saveObject(sheeps,fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void demo(Context context){
        Map<Integer, Sheep> sheeps=null;
        Calendar c = Calendar.getInstance();
        addDemoSheep(context,1, "Dolly", 51, c,21.453348173455414,39.94836946708256);
            Calendar c2 = Calendar.getInstance();
            c2.add(Calendar.SECOND, 7);
    }

    private void addDemoSheep(Context context, int id, String nikName, float weight, Calendar lastFeedDate, double latitude, double longitude){
        Sheep sheep=new Sheep(1, "Dolly", 51, lastFeedDate,21.453348173455414,39.94836946708256);
        saveSheep(sheep);
        AlarmHandler.setAlarm(context,sheep);
    }
}