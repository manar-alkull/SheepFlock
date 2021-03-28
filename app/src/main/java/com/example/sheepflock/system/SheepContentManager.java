package com.example.sheepflock.system;

import android.content.Context;

import com.example.sheepflock.Settings;
import com.example.sheepflock.Sheep;
import com.example.sheepflock.alarm.AlarmHandler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SheepContentManager extends ContentHandler{

    String sheepsFileName ="sheeps.bin";
    String settingsFileName ="settings.bin";
    public SheepContentManager(Context context) {

        super(context, "");
    }

    public Map<Integer,Sheep> getSheeps(){
        if(isFileExist(sheepsFileName)) {
            try {
                return (HashMap<Integer,Sheep>) getObject(sheepsFileName);
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
            saveObject(sheeps, sheepsFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void demo(Context context){
        Map<Integer, Sheep> sheeps=null;
        Calendar c = Calendar.getInstance();
        addDemoSheep(context,1, "دولي", 51, c,21.453348173455414,39.94836946708256);
        c = Calendar.getInstance();
        addDemoSheep(context,2, "بولي", 46, c,21.453623744145755,39.950574247074464);
        c = Calendar.getInstance();
        addDemoSheep(context,3, "يوكي", 44, c,21.453348173455414,39.950574247074464);
        c = Calendar.getInstance();
        addDemoSheep(context,4, "الكبش", 69, c,21.453623744145755,39.94836946708256);
        //c2.add(Calendar.SECOND, 7);
    }

    private void addDemoSheep(Context context, int id, String nikName, float weight, Calendar lastFeedDate, double latitude, double longitude){
        Sheep sheep=new Sheep(id, nikName, weight, lastFeedDate,latitude,longitude);
        saveSheep(sheep);
        AlarmHandler.setAlarm(context,sheep);
    }


    public Settings getSetting() {
        if(isFileExist(settingsFileName)) {
            try {
                return (Settings) getObject(settingsFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new Settings();
    }

    public void saveSettings(Settings settings){
        try {
            saveObject(settings, settingsFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
