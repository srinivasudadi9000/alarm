package srinivasu.alarm_sucess;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by srinivas on 08/07/18.
 */

public class BackgroundService extends Service {

    private boolean isRunning;
    private Context context;
    Intent intent;
    Thread backgroundThread;
    Timer t;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("BackgroundService", "onBinder service");
        this.intent =intent;
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        Log.d("BackgroundService", "oncreate service");
        sendnotify();
        // startgetlatlong();
    }

    public void startgetlatlong() {
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d("BackgroundService", "timercalled service");
                sendnotify();
            }
        }, 0, 3500);//put here time 1000 milliseconds=1 second
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            // Do something here
            sendnotify();
            Log.d("BackgroundService", "Mytask running service");

            // stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        Log.d("BackgroundService", "Destroy service");

        this.isRunning = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BackgroundService", "again start called service... ");
        new JobAsyncTask(this).execute();
        return START_STICKY;
    }

    private class JobAsyncTask extends AsyncTask<JobParameters, Void, JobParameters> {


        @RequiresApi(api = Build.VERSION_CODES.M)
        public JobAsyncTask(BackgroundService backgroundService) {
            Log.d("JObschedular", "JobAsyncTask");

            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            // cal.add(Calendar.SECOND, 5);
            // alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            //alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,cal.getTimeInMillis(), pendingIntent);
            SharedPreferences sharedPreferences = getSharedPreferences("interval",MODE_PRIVATE);
            int val = Integer.parseInt(sharedPreferences.getString("const",""));
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +val, pendingIntent);

        }

        @Override
        public JobParameters doInBackground(JobParameters... jobParameters) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String millisInString = dateFormat.format(new Date());
            Log.d("sending...to...server", "before service calling");
            try {

                sendlatlong_to_server("18.181", "18.181", millisInString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // SystemClock.sleep(60000);

            return null;
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            Log.d("JObschedular", "JobAsyncTask completed");

            // Finish the job service if required
//            jobService.jobFinished(jobParameters, false);
        }
    }

    public void sendnotify() {
        Log.d("BackgroundService", "sendnotification service");
        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.journaldev.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Notifications Title");
        builder.setContentText("Your notification content here.");
        builder.setSubText("Tap to view the website.");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
        Log.d("BackgroundService", "sendnotificationend service");
    }

    public void sendlatlong_to_server(String latitude, String longitude, String datetime) throws IOException {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "DoNjfdhotDimScreen");
        // if (!wl.isHeld()) {
        wl.acquire(60000);


        // avoid creating several instances, should be singleon
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://125.62.194.181/tracker/trackernew.asmx/UpdateLocation?").newBuilder();
        urlBuilder.addQueryParameter("Token", "VVD@14");
        urlBuilder.addQueryParameter("DeviceID", "2");
        urlBuilder.addQueryParameter("Lat", latitude);
        urlBuilder.addQueryParameter("Long", longitude);
        urlBuilder.addQueryParameter("Altitude", "20");
        urlBuilder.addQueryParameter("Speed", "10");
        urlBuilder.addQueryParameter("Course", "android_srinivas");
        urlBuilder.addQueryParameter("Battery", "20");
        urlBuilder.addQueryParameter("Address", "vizag");
        urlBuilder.addQueryParameter("LocationProvider", "srinivasdadi");
        urlBuilder.addQueryParameter("UpdatedDateTime", datetime);
        urlBuilder.addQueryParameter("AppStatus", "2");
        urlBuilder.addQueryParameter("MobileDeviceID", "4f92900a52d28ab8");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("result", e.getMessage().toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {

                    Log.d("result", response.toString());
                    throw new IOException("Unexpected code " + response);
                } else {
                    Log.d("result_else", response.toString());
                }
            }
        });


    }


}