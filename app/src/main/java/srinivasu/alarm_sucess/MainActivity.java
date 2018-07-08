package srinivasu.alarm_sucess;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
 TextView mytext,mytext2,mytext3;
     @RequiresApi(api = Build.VERSION_CODES.M)
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mytext = (TextView)  findViewById(R.id.mytext);
         mytext2 = (TextView)  findViewById(R.id.mytext2);
         mytext3 = (TextView)  findViewById(R.id.mytext3);

         mytext.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                 Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                 PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                 // cal.add(Calendar.SECOND, 5);
                 // alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                 //alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,cal.getTimeInMillis(), pendingIntent);
                 alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +60000, pendingIntent);

                 Log.d("sucess","clicked");
 }
         });
         mytext2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                 Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                 PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                 // cal.add(Calendar.SECOND, 5);
                 // alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                 //alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,cal.getTimeInMillis(), pendingIntent);
                 alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +60000, pendingIntent);

                 Log.d("sucess","clicked");

               }
         });
         mytext3.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                 Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                 PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                 // cal.add(Calendar.SECOND, 5);
                 // alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                 //alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,cal.getTimeInMillis(), pendingIntent);
                 alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +60000, pendingIntent);

                 Log.d("sucess","clicked");

                }
         });

        Log.d("MainActivity", "Alarm running ");



       /*
*/
    }
}
