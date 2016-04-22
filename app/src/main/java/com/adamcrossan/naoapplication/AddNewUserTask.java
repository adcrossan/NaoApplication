package com.adamcrossan.naoapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Setup on 06/04/2016.
 */
public class AddNewUserTask extends AsyncTask <String,Void,String > {


    AlertDialog alertDialog;
    Context ctx;
    String text ;
    AddNewUserTask(Context ctx)
    {
        this.ctx =ctx;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Registration Success ...."))
        {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
        }
        else
        {
            alertDialog.setMessage(result);


            alertDialog.show();
        }

    }

    @Override
    protected String doInBackground(String... params) {
        String addNewUser_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/insertion.php";
        String method = params[0];

        if(method.equals("addNewUser"))
        {
            String nameVar = params[1];
            String yearVar = params[2];
            String semesterVar = params[3];
            String userNameVar = params[4];
            String courseIdVar = nameVar + yearVar + semesterVar ;
            String userCourseVar = userNameVar + courseIdVar ;


            try {
                URL url = new URL(addNewUser_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data =  URLEncoder.encode("userCourse", "UTF-8")+ "="+URLEncoder.encode(userCourseVar, "UTF-8")+"&"+
                        URLEncoder.encode("userName", "UTF-8")+ "="+URLEncoder.encode(userNameVar, "UTF-8")+"&"+
                        URLEncoder.encode("courseId", "UTF-8")+ "="+URLEncoder.encode(courseIdVar,"UTF-8");
                Log.d( "View", userCourseVar+","+userNameVar+","+courseIdVar);

                bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return  "Registration Success ....";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    return null;
    }
}
