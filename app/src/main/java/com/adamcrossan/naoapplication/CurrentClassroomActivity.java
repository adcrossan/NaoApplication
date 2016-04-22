package com.adamcrossan.naoapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.adamcrossan.naoapplication.MyNaoService;
import com.adamcrossan.naoapplication.R;
import com.adamcrossan.naoapplication.utils.PermissionsUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.polestar.naosdk.api.external.NAOERRORCODE;
import com.polestar.naosdk.api.external.NAOLocationHandle;
import com.polestar.naosdk.api.external.NAOLocationListener;
import com.polestar.naosdk.api.external.NAOSensorsListener;
import com.polestar.naosdk.api.external.NAOSyncListener;
import com.polestar.naosdk.api.external.TNAOFIXSTATUS;

import java.util.Calendar;

public class CurrentClassroomActivity extends AppCompatActivity implements NAOLocationListener, NAOSensorsListener {


    public static final String API_KEY = "lRl5ZwBA75erb8Byziyk1g";
    private double lat = 0;
    private double lon = 0;
    private double alt = 0 ;
    private Intent intent ;
    private String weekDay, username ;
    private String setNo ;
    Calendar c = Calendar.getInstance();
    WebView mymap ;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private GoogleApiClient client;
    private static final int PERMISSION_REQUEST_CODE = 0;

    private static final String[] NEEDDED_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    protected boolean checkPermissions(){
        return PermissionsUtils.checkAndRequest(this, NEEDDED_PERMISSIONS, getResources().getString(R.string.permission_msg), PERMISSION_REQUEST_CODE,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onPermissionsRefused();
                    }
                });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE){
            intent = getIntent();
            boolean allRequestsAccepted = false;
            if (grantResults.length == permissions.length){
                for (int i = 0; i < grantResults.length;  i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED ){
                        allRequestsAccepted = false;
                        break;
                    }else{
                        allRequestsAccepted = true;
                    }
                }
                if (allRequestsAccepted){
                    startService(intent);
                }else{
                    onPermissionsRefused();
                }
            }
        }
    }

    private void onPermissionsRefused(){
        notifyUser("Cannot run the service because permissions have been denied");
        /*
        setNaoServiceStarted(false);
        startButton.setEnabled(false);
        syncButton.setEnabled(false);
        startButton.setBackgroundColor(ColorUtils.getColor(this, R.color.disabledButton));
        syncButton.setBackgroundColor(ColorUtils.getColor(this, R.color.disabledButton));
        syncStateTextView.setText("No sync in progress");*/
    }

    public void notifyUser(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_classroom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getDay();
        getTimeSet();
        Log.d(this.getClass().getName(), "*************** Set No  = " + setNo + "***************");
        if (weekDay.equals("Saturday") ||weekDay.equals("Sunday") ||setNo == null )
        {
            Toast.makeText(this, "You currently are not sceduled for any classes", Toast.LENGTH_LONG).show();
            finish();
        }
        else {

            Bundle bundle = getIntent().getExtras();
            username = bundle.getString("stuff");
            Log.d(this.getClass().getName(), "*************** " + username + "***************");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Get Current Classroom");

            NAOLocationHandle handle = new NAOLocationHandle(this, MyNaoService.class, API_KEY, this, this);
            checkPermissions();
            intent = getIntent();

            getLocationTask getLoTask = new getLocationTask(this);
            AddCoTask addCoTask = new AddCoTask(this);

            String latS = String.valueOf(lat);
            String lonS = String.valueOf(lon);
            String altS = String.valueOf(alt);

            Log.d(this.getClass().getName(), "about to execute");
            getLoTask.execute(username, weekDay, setNo);
            addCoTask.execute(latS, lonS, altS);

            mymap = (WebView) findViewById(R.id.mymap);
            WebSettings settings = mymap.getSettings();
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
            mymap.getSettings().setJavaScriptEnabled(true);
            mymap.loadUrl("file:///android_asset/mapLocation2.php");


            NAOSyncListener naoSyncListener = new NAOSyncListener() {
                @Override
                public void onSynchronizationSuccess() {

                    Log.d(this.getClass().getName(), "onSynchronizationSuccess");
                }


                @Override
                public void onSynchronizationFailure(NAOERRORCODE naoerrorcode, String message) {

                    Log.d(this.getClass().getName(), "onSynchronizationFailure: " + message);
                }
            };
            handle.synchronizeData(naoSyncListener);
            handle.start();
            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.

            client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        }
    }
    @Override
    public void onError(NAOERRORCODE naoerrorcode, String s) {

        Log.e(this.getClass().getName(), "onError " + s);
        //TextView textView = (TextView)findViewById(R.id.coordin);

        //textView.setText("Error in recieving co-ordinated \nPlease try again later");

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("dbg", location.getLatitude() + "," + location.getLongitude() +
                "," + location.getAltitude() + "," + location.getAccuracy());

        lat = location.getLatitude();
        lon = location.getLongitude();
        alt = location.getAltitude();


        AddCoTask addCoTask = new AddCoTask(this);
        String latS = String.valueOf(lat);
        String lonS = String.valueOf(lon);
        String altS = String.valueOf(alt);
        addCoTask.execute(latS, lonS, altS);
    }

    @Override
    public void onLocationStatusChanged(TNAOFIXSTATUS tnaofixstatus) {

        switch (tnaofixstatus) {
            case NAO_FIX_AVAILABLE:
                Log.d("dbg", "status changed > AVAILABLE");
                break;
            case NAO_TEMPORARY_UNAVAILABLE:
                Log.d("dbg", "status changed > TEMPORARILY UNAVAILABLE");
                break;
            case NAO_OUT_OF_SERVICE:
            default:
                Log.d("dbg", "status changed > OUT_OF_SERVICE");
                break;
        }
    }

    @Override
    public void onEnterSite(String s) {

    }

    @Override
    public void onExitSite(String s) {

    }

    @Override
    public void requiresCompassCalibration() {

    }

    @Override
    public void requiresWifiOn() {

    }

    @Override
    public void requiresBLEOn() {

    }

    @Override
    public void requiresLocationOn() {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        checkPermissions();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CurrentLocation Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.adamcrossan.naoapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    public void getDay()
    {
        int day = c.get(Calendar.DAY_OF_WEEK);
         weekDay = "";
        switch (day)
        {
            case Calendar.MONDAY:
                weekDay ="Monday";
                break ;
            case Calendar.TUESDAY:
                weekDay ="Tuesday";
                break ;
            case Calendar.WEDNESDAY:
                weekDay ="Wednesday";
                break ;
            case Calendar.THURSDAY:
                weekDay ="Thursday";
                break ;
            case Calendar.FRIDAY:
                weekDay ="Friday";
                break ;
            case Calendar.SATURDAY:
                weekDay ="Saturday";
                break ;
            case Calendar.SUNDAY:
                weekDay ="Sunday";
                break ;
        }
    }

    public String getTimeSet()
    {
        Calendar c = Calendar.getInstance();
        int timeHour = c.get(Calendar.HOUR);
        int timeMin = c.get(Calendar.MINUTE) ;

        if ( timeHour == 9 )
        {
            if (timeMin < 30 )
            {
                setNo = null ;
            }
            else
            {
                setNo = "set1";
            }
        }
        if ( timeHour == 10 )
        {
            if (timeMin < 30 )
            {
                setNo = "set1" ;
            }
            else
            {
                setNo = "set2";
            }
        }
        else if (timeHour == 11  )
        {
            if (timeMin < 30 )
            {
                setNo = "set2" ;
            }
            else
            {
                setNo = "set3";
            }
        }
        else  if (timeHour == 12 )
        {
            if (timeMin < 30 )
            {
                setNo = "set3" ;
            }
            else
            {
                setNo = "set4";
            }
        }
        else if (timeHour == 1  )
        {
            if (timeMin < 30 )
            {
                setNo = "set4" ;
            }
            else
            {
                setNo = "set5";
            }
        }
        else if (timeHour == 2  )
        {
            if (timeMin < 30 )
            {
                setNo = "set5" ;
            }
            else
            {
                setNo = "set6";
            }
        }
        else if (timeHour == 3  )
        {
            if (timeMin < 30 )
            {
                setNo = "set6" ;
            }
            else
            {
                setNo = "set7";
            }
        }
        else if (timeHour == 4  )
        {
            if (timeMin < 30 )
            {
                setNo = "set7" ;
            }
            else
            {
                setNo = "set8";
            }
        }
        else if (timeHour == 5)
        {
            if (timeMin < 30 )
            {
                setNo = "set8";
            }
        }
        else
            setNo = null ;
        return setNo ;

    }


    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CurrentLocation Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.adamcrossan.naoapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


