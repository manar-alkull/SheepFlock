package com.example.sheepflock.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.sheepflock.MainActivity;
import com.example.sheepflock.MapsActivity;
import com.example.sheepflock.R;
import com.example.sheepflock.Settings;
import com.example.sheepflock.Sheep;
import com.example.sheepflock.system.SheepContentManager;
import com.example.sheepflock.system.GenericListenersRegister;
import com.example.sheepflock.system.SystemServices;
import com.example.sheepflock.ui.home.HomeFragment;

import java.util.Calendar;

//https://stackoverflow.com/questions/4459058/alarm-manager-example
//https://stackoverflow.com/questions/24196890/android-schedule-task-to-execute-at-specific-time-daily
public class AlarmHandler extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "sheeper:alarm set");
        wl.acquire();

        int itemId=intent.getIntExtra("sheepId",-1);
        Log.i("alarm","ready to alarm: "+itemId);

        SheepContentManager sheepContentManager=new SheepContentManager(context.getApplicationContext());
        Sheep sheep= sheepContentManager.getSheeps().get(itemId);
        Intent open_activity_intent = new Intent(context, MapsActivity.class);
        open_activity_intent.putExtra("itemId",itemId);
        String notifyHeader=context.getResources().getString(R.string.notificationHeader);
        String notifyMessage=context.getResources().getString(R.string.notificationMessage, sheep.nikName);
        SystemServices.notifyWithSound(context, notifyMessage,notifyHeader,open_activity_intent);

        sheepListeners.sendToAll(sheep);
        HomeFragment.refresh();
        wl.release();
    }

    public static GenericListenersRegister<Sheep, SheepHungryListener> sheepListeners=new GenericListenersRegister<Sheep, SheepHungryListener>() {
        @Override
        protected void doForEachListener(SheepHungryListener sheepHungryListener, Sheep sheep) {
            sheepHungryListener.onSheepHungry(sheep);
        }
    };



    public static void setAlarm(Context context, Sheep sheep)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i2 = new Intent("com.example.sheepflock.alarm");
        Intent i = new Intent(context,AlarmHandler.class);
        i.putExtra("sheepId",sheep.id);
        PendingIntent pi = PendingIntent.getBroadcast(context, sheep.id, i, 0);
        PendingIntent pi2 = PendingIntent.getBroadcast(context, sheep.id, i2, 0);

        Settings settings=new SheepContentManager(MainActivity.mainActivityContext).getSetting();
        Calendar nextDate=(Calendar)sheep.lastFeedDate.clone();
        nextDate.add(Calendar.SECOND,settings.feedPeriod_seconds);
        am.setRepeating(AlarmManager.RTC_WAKEUP,  nextDate.getTimeInMillis(),settings.remindPeriod, pi);
        am.setRepeating(AlarmManager.RTC_WAKEUP,  nextDate.getTimeInMillis(),settings.remindPeriod, pi2);
    }

    public static void cancelAlarm(Context context, Sheep sheep)
    {
        Intent intent = new Intent(context, AlarmHandler.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, sheep.id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public interface SheepHungryListener{
        void onSheepHungry(Sheep sheep);
    }
}

