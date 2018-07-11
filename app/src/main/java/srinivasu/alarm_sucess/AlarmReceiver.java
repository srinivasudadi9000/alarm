package srinivasu.alarm_sucess;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by srinivas on 08/07/18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "DoNjfdhotDimScreen");


        // if (!wl.isHeld()) {
        wl.acquire(60000);
        // }
        // wl.release();
        /*
        // If the screen is off then the device has been locked
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        boolean isScreenOn;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            isScreenOn = powerManager.isDeviceIdleMode();
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            isScreenOn = powerManager.isInteractive();
        } else {
            isScreenOn = powerManager.isScreenOn();
        }

        if (!isScreenOn) {

            // The screen has been locked
            // do stuff...
        }*/


        Intent background = new Intent(context, BackgroundService.class);
        context.startService(background);
        //  restartAlarm();
    }

    public void restartAlarm() {
        Log.d("mystatus","alaram Restarted babau");

        Intent alarm = new Intent(this.context, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this.context, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == true) {
            Log.d("mystatus","alaram running babau");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //  alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 240000, pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("mystatus","servie guru this is marshmallow greater version");
                // if you don't want the alarm to go off even in Doze mode, use
                //  alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,100, 30000, pendingIntent);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,3000, pendingIntent);
                //  alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(60000, pendingIntent), pendingIntent);
            } else {
                Log.d("mystatus","service guru this is marshmallow below version");
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,0, 120000, pendingIntent);
                //manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
            }

        }else {
            Log.d("mystatus","service alaram not running babau");
        }
    }



}
