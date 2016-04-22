package com.adamcrossan.naoapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
public class getLocationTask extends AsyncTask<String, Void, JSONObject> {
    private JSONArray user = null;
    private JSONArray coordinates = null ;
    private Context ctx;
    private String courseId,courseDay, setIdNo , room, roomCoor,  roomName, roomLat, roomLon ,roomALt ;

    public getLocationTask(Context context) {
            this.ctx =context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONParser jParser = new JSONParser();

        String getCourseID_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/getCourseID.php";
        String getCourseDay_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/getCourseDay.php";
        String getSetID_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/getSetId.php";
        String getLocation_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/getLocation.php";
        String getRoomCo_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/getRoomCo.php";
        String upDateClassCo_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/updateClassCo.php";



        String userIdVar = params[0];
        String weekDay = params[1];
        String setNo = params[2];


        try {
            URL url = new URL(getCourseID_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("UserID", "UTF-8")+ "="+URLEncoder.encode(userIdVar,"UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            courseId = "" ;
            String line = "";
            while ((line = bufferedReader.readLine())!= null)
            {
                courseId += line ;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.d("View", "*******************Course ID *****************"+ courseId);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            URL url = new URL(getCourseDay_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("Day", "UTF-8")+ "="+URLEncoder.encode(weekDay,"UTF-8")+"&"+
                    URLEncoder.encode("CourseID", "UTF-8")+ "="+URLEncoder.encode(courseId, "UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            courseDay = "" ;
            String line = "";
            while ((line = bufferedReader.readLine())!= null)
            {
                courseDay += line ;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.d("View", "*******************Course Day *****************"+ courseDay);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            URL url = new URL(getSetID_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("CourseDay", "UTF-8")+ "="+URLEncoder.encode(courseDay,"UTF-8")+"&"+
                    URLEncoder.encode("setNo", "UTF-8")+ "="+URLEncoder.encode(setNo, "UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            setIdNo = "" ;
            String line = "";
            while ((line = bufferedReader.readLine())!= null)
            {
                setIdNo += line ;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.d("View", "******************* SetNo *****************"+ setIdNo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            URL url = new URL(getLocation_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("setUI", "UTF-8")+ "="+URLEncoder.encode(setIdNo, "UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            room = "" ;
            String line = "";
            while ((line = bufferedReader.readLine())!= null)
            {
                room += line ;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.d("View", "******************* Room Number *******" + room);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //**************REMEMBER TO CHANGE BACK ********************
        room = "W2213";
        try {
            URL url = new URL(getRoomCo_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("roomNo", "UTF-8")+ "="+URLEncoder.encode(room, "UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            roomCoor = "" ;
            String line = "";
            while ((line = bufferedReader.readLine())!= null)
            {
                roomCoor += line ;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.d("View", "******************* Room Coor *******" + roomCoor);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Getting JSON from URL
        JSONObject json = null;
        try {
            json = new JSONObject(roomCoor);
            coordinates = json.getJSONArray("Coordinates");
            Log.d("View", "Where i thought it was  ");

            for (int i = 0; i < coordinates.length(); i++)
            {
                JSONObject c = coordinates.getJSONObject(i);
                // Storing each json item in variable
                roomName = c.getString("lat");
                roomLat = c.getString("lat");
                roomLon = c.getString("lon");
                roomALt = c.getString("alt");
                Log.d("View", "the user Location is " + roomName + ": " + roomLat + ", " + roomLon);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            URL url = new URL(upDateClassCo_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("roomName", "UTF-8")+ "="+URLEncoder.encode(roomName, "UTF-8")+"&"+
                    URLEncoder.encode("roomLat", "UTF-8")+ "="+URLEncoder.encode(roomLat, "UTF-8")+"&"+
                    URLEncoder.encode("roomLon", "UTF-8")+ "="+URLEncoder.encode(roomLon, "UTF-8")+"&"+
                    URLEncoder.encode("roomAlt", "UTF-8")+ "="+URLEncoder.encode(roomALt, "UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String reply = "" ;
            String line = "";
            while ((line = bufferedReader.readLine())!= null)
            {
                reply += line ;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.d("View", "******************* REPLY  *******" + reply);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
    @Override
    protected void onPostExecute(JSONObject json) {
        try {
            // Getting JSON Array
            if ( room.equals("outside"))
            {
                Toast.makeText(ctx, "You Currently are not scheduled for any classes", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(ctx, "You are currently in " + room ,Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

