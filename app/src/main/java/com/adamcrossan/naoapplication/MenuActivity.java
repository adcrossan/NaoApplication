package com.adamcrossan.naoapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    Spinner userSpinner ;
    public final static String EXTRA_MESSAGE = "";
    String message  = "";
    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lyit Navigational Service");
        turnOn(this);

        if ( isConnectedToInternet())
        {
            fillUserSpinner();

        }
        else {
            Toast.makeText(this, "No internet Conectivity please connect to the internet to continue", Toast.LENGTH_LONG).show();
        }
        userSpinner = (Spinner)findViewById(R.id.userSpinner);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public void turnOn(MenuActivity view) {
        if (!mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.enable();
            message = "Bluetooth is on";
            Toast.makeText(getApplicationContext(), "Bluetooth has been enabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        fillUserSpinner();
        //Refresh your stuff here
    }

    public void GetXY(View view) {
        Intent intent = new Intent(this, CurrentLocationActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void NewUser(View view) {
        Intent intent = new Intent(this, AddUserActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void findClass(View view) {
        String username = userSpinner.getSelectedItem().toString();

        String i="hi";
        Intent intent = new Intent(this, CurrentClassroomActivity.class);
        //Create the bundle
        Bundle b = new Bundle();
        //Add your data to bundle
        b.putString("stuff", username);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void fillUserSpinner() {
        FillUserTask fillUserTask = new FillUserTask(this, this);
        fillUserTask.execute();
    }


    public void Finish(View view) {
        finish();
        mBluetoothAdapter.disable();
        Toast.makeText(this, "Bluetooth has now been disabled", Toast.LENGTH_LONG).show();
    }

}
