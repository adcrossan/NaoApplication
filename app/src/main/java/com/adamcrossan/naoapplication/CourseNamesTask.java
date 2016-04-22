package com.adamcrossan.naoapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Setup on 11/04/2016.
 */
public class CourseNamesTask extends AsyncTask<String, String, JSONObject> {
    private JSONArray user = null;
    private Activity activity;
    private Context context;
    private String response;

    public CourseNamesTask(Activity activity,Context context) {
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

        String getCourseNames_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/getCourseNames.php";

            String yearVar = params[0];
            String departmentVar = params[1];
            String semesterVar = params[2];

            try {
                URL url = new URL(getCourseNames_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("semester", "UTF-8")+ "="+URLEncoder.encode(semesterVar,"UTF-8")+"&"+
                        URLEncoder.encode("department", "UTF-8")+ "="+URLEncoder.encode(departmentVar,"UTF-8")+"&"+
                        URLEncoder.encode("year", "UTF-8")+ "="+URLEncoder.encode(yearVar,"UTF-8");
                Log.d("View", semesterVar + "," + departmentVar + "," + yearVar);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                response = "" ;
                String line = "";
                while ((line = bufferedReader.readLine())!= null)
                {
                    response += line ;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("View", response);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        // Getting JSON from URL
        JSONObject json = null;
        try {
            json = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            // Getting JSON Array
            Log.d("View", "inside Execute");
            user = json.getJSONArray("Courses");
            final String[] items = new String[user.length()];
            Log.d("View", "inside Execute again " + user.length());
            Spinner mySpinner = (Spinner) activity.findViewById(R.id.CourseNameSpinner);

            for(int i = 0; i < user.length(); i++){

                JSONObject c = user.getJSONObject(i);
                // Storing each json item in variable

                String name = c.getString("CourseName");
                Log.d("View", "the user name is " + name);
                items[i]=c.getString("CourseName");

                mySpinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

