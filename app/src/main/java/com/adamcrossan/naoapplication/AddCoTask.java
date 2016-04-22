package com.adamcrossan.naoapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
 * Created by Setup on 13/04/2016.
 */
public class AddCoTask extends AsyncTask<String,Void,String > {


    AlertDialog alertDialog;
    Context ctx;
    String response;
    AddCoTask(Context ctx)
    {
        this.ctx =ctx;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Co-ordinate Info");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected String doInBackground(String... params) {
        String updateCo_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/updateCo.php";

            String lat = params[0];
            String lon = params[1];
            String alt = params[2];

            try {
                URL url = new URL(updateCo_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data =  URLEncoder.encode("lat", "UTF-8")+ "="+URLEncoder.encode(lat, "UTF-8")+"&"+
                        URLEncoder.encode("lon", "UTF-8")+ "="+URLEncoder.encode(lon, "UTF-8")+"&"+
                URLEncoder.encode("alt", "UTF-8")+ "="+URLEncoder.encode(alt, "UTF-8");
                Log.d("View", lat + "," + lon + "," );

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
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

                return  response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
}

