package srinivasu.alarm_sucess;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

/**
 * Created by srinivas on 11/07/18.
 */

public class BackgroundService  extends Service {
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    private LocationRequest mLocationRequest;
    private boolean isRunning;
    private Context context;
    Intent intent;
    String latitude = "0.0", longitude = "0.0";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("BackgroundService", "onBinder service");
        this.intent = intent;
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        mFusedLocationClient = getFusedLocationProviderClient(this);
        initializefuse();
      /*  GPSTracker gpsTracker = new GPSTracker(context);
        Location location = gpsTracker.getLocation();
        // String lat = "0.0", lat_long = "0.0";
        if (location != null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
        }*/
        Log.d("BackgroundService", "oncreate back me service");
        sendnotify();

    }

    public void initializefuse() {

        // startgetlatlong();
      /*  mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(2000);
        //  mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);


        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }
        });*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Log.d("findithere_first", task.toString());
                if (task.isSuccessful() && task.getResult() != null) {
                    mLastLocation = task.getResult();
                    String lat, longi;
                    lat = String.valueOf(mLastLocation.getLatitude());
                    longi = String.valueOf(mLastLocation.getLongitude());
                    latitude = String.valueOf(mLastLocation.getLatitude());
                    longitude = String.valueOf(mLastLocation.getLongitude());

                    Log.d("findithere_middle", lat + " " + longi);

                } else {
                    latitude = "0.0";
                    longitude = "0.0";
                     Log.d("findithere_exception",task.getException().toString());
                }
            }
        });

              /*  getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());*/

    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        // You can now create a LatLng Object for use with maps
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        Log.d("findther_change", latitude + " " + longitude);
    }

    @Override
    public void onDestroy() {
        Log.d("BackgroundService", "Destroy service");

        this.isRunning = false;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initializefuse();
        Log.d("BackgroundService", "again start called service... ");
      /*  GPSTracker gpsTracker = new GPSTracker(context);
        Location location = gpsTracker.getLocation();
        // String lat = "0.0", lat_long = "0.0";
        if (location != null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
        }*/

        new JobAsyncTask(this).execute();
        return START_STICKY;
    }

    private class JobAsyncTask extends AsyncTask<JobParameters, Void, JobParameters> {

        @SuppressLint("NewApi")
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
                sendlatlong_to_server(latitude, longitude, millisInString);
            } catch (IOException e) {
                e.printStackTrace();
            }

           /* try {
                if (!Validation.internet(context)) {
                    // No internet connnection Available....
                } else {
                    // Internet connection available..
                    sendlatlong_to_server(latitude, longitude, millisInString);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }*/
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
        //  urlBuilder.addQueryParameter("DeviceID", "2");
        urlBuilder.addQueryParameter("DeviceID", "5");
        urlBuilder.addQueryParameter("Lat", latitude);
        urlBuilder.addQueryParameter("Long", longitude);
        urlBuilder.addQueryParameter("Altitude", "20");
        urlBuilder.addQueryParameter("Speed", "10");
        urlBuilder.addQueryParameter("Course", "redmi5a");
        urlBuilder.addQueryParameter("Battery", "20");
        urlBuilder.addQueryParameter("Address", "vizag");
        urlBuilder.addQueryParameter("LocationProvider", "srinivasdadi");
        urlBuilder.addQueryParameter("UpdatedDateTime", datetime);
        urlBuilder.addQueryParameter("AppStatus", "2");
        //  urlBuilder.addQueryParameter("MobileDeviceID", "4f92900a52d28ab8");
        urlBuilder.addQueryParameter("MobileDeviceID", "8790467636");

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
