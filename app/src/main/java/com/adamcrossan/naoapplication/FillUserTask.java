package com.adamcrossan.naoapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Setup on 11/04/2016.
 */
public class FillUserTask extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        private JSONArray user = null;
        private Activity activity;
        private Context context;

    public FillUserTask(Activity activity,Context context) {
        this.activity = activity;
        this.context = context;
    }


    @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            String getUsers_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/getUsers.php";
            JSONObject json = jParser.getJSONFromUrl(getUsers_url);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                // Getting JSON Array
                user = json.getJSONArray("Users");
                final String[] items = new String[user.length()];
                Spinner mySpinner = (Spinner) activity.findViewById(R.id.userSpinner);

                for(int i = 0; i < user.length(); i++){

                    JSONObject c = user.getJSONObject(i);
                    // Storing each json item in variable

                    String name = c.getString("Username");
                    Log.d("View", "the user name is " + name );
                    items[i]=c.getString("Username");

                    mySpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
}

