package com.adamcrossan.naoapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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
import java.util.ArrayList;
import java.util.List;

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

    public String getCourse() {
        //handle value
       //a Log.d("dbg", "course name : "+text );
        return text ;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Registration Success ...."))
        {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
        }
        else
        {
            //delegate.processFinish(result);
            alertDialog.setMessage(result);


            alertDialog.show();
        }

    }

    @Override
    protected String doInBackground(String... params) {
        String getCourseID_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/getCourseID.php";
        String addNewUser_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/insertion.php";
        String getCourseNames_url = "http://ec2-52-37-220-89.us-west-2.compute.amazonaws.com/getCourseNames.php";
        String method = params[0];

        if(method.equals("getCourseNames"))
        {
            String yearVar = params[1];
            String departmentVar = params[2];
            String semesterVar = params[3];

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
                Log.d( "View", semesterVar+","+departmentVar+","+yearVar);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line ="";
                while ((line = bufferedReader.readLine())!= null)
                {
                    response+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return  response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

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
