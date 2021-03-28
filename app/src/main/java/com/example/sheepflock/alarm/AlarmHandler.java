package com.example.sheepflock.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

        sendNotification(context,sheep);

        sheepListeners.sendToAll(sheep);
        HomeFragment.refresh();
        wl.release();
    }

    private void sendNotification(Context context,Sheep sheep) {
        String notifyHeader=context.getResources().getString(R.string.notificationHeader);
        String notifyMessage=context.getResources().getString(R.string.notificationMessage, sheep.nikName);

/*        Intent open_activity_intent = new Intent(context, MapsActivity.class);
        open_activity_intent.putExtra("itemId",itemId);
        SystemServices.notifyWithSound(context, notifyMessage,notifyHeader,open_activity_intent);*/

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("itemId",sheep.id);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    // Adds the back stack
        stackBuilder.addParentStack(MainActivity.class);
    // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
    // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        SystemServices.notify_pending(context, notifyMessage,notifyHeader,resultPendingIntent);
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


    public static void setAlarm2(Context context, Sheep sheep) {
        /*Intent i2 = new Intent("com.example.sheepflock.alarm");
        Intent i = new Intent(context,AlarmHandler.class);*/
        Intent alarmIntent = new Intent(context, AlarmHandler.class);
        //alarmIntent.setAction("com.example.sheepflock.alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, sheep.id, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Settings settings=new SheepContentManager(MainActivity.mainActivityContext).getSetting();
        Calendar nextDate=(Calendar)sheep.lastFeedDate.clone();
        if (Build.VERSION.SDK_INT >= 23) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextDate.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            manager.setExact(AlarmManager.RTC_WAKEUP, nextDate.getTimeInMillis(), pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, nextDate.getTimeInMillis(), pendingIntent);
        }
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

