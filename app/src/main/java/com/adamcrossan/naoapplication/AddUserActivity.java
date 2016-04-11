package com.adamcrossan.naoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.adamcrossan.naoapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AddUserActivity extends AppCompatActivity /*implements AsyncResponse */{

    private Intent intent ;
    private Spinner courseYearSpin, courseDepSpin, courseSemSpin, courseNameSpin ;
    private String courseYear, courseDep, courseSem, courseName, userName ;
    private EditText ET_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New User");


        //fillCourseNames();
        courseYearSpin = (Spinner)findViewById(R.id.YearSpinner);
        courseSemSpin = (Spinner)findViewById(R.id.SemesterSpinner);
        courseDepSpin = (Spinner)findViewById(R.id.DepartmentSpinner);
        courseNameSpin = (Spinner)findViewById(R.id.CourseNameSpinner);
        ET_userName = (EditText)findViewById((R.id.UsernameText));


    }

    public void findCourseNames(View view)
    {
       /*if (courseDepSpin.getSelectedItemPosition()!= 0 && courseSemSpin.getSelectedItemPosition() !=0 && courseNameSpin.getSelectedItemPosition()!=0 )
        {*/
            courseSem = courseSemSpin.getSelectedItem().toString();
            courseDep = courseDepSpin.getSelectedItem().toString();
            courseYear = courseYearSpin.getSelectedItem().toString();
            String method = "getCourseNames";
            AddNewUserTask addNewUserTask = new AddNewUserTask(this);
            addNewUserTask.execute(method, courseYear, courseDep, courseSem);
           // Log.d("dbg", courseName );
           // Toast.makeText(this, courseName, Toast.LENGTH_LONG).show();
       /* }
        else
       {
            Toast.makeText(this, "Please Eneter a user name and select a \n"+
                    "Year, Semester, and Department" , Toast.LENGTH_LONG).show();
        }*/
    }

    public void addNewUser(View view)
    {
        courseSem = courseSemSpin.getSelectedItem().toString();
        courseDep = courseDepSpin.getSelectedItem().toString();
        courseYear = courseYearSpin.getSelectedItem().toString();
        courseName = courseNameSpin.getSelectedItem().toString();
        userName = ET_userName.getText().toString();
        String method = "addNewUser";

        AddNewUserTask addNewUserTask = new AddNewUserTask(this);
        addNewUserTask.execute(method, courseName, courseYear, courseSem, userName);
        finish();

    }

    /*public void processFinish(String output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
       Toast.makeText(this, output, Toast.LENGTH_LONG).show();
    }*/


}
