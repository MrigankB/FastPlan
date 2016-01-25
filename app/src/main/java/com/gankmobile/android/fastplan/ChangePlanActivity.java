package com.gankmobile.android.fastplan;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;


public class ChangePlanActivity extends ActionBarActivity
{
    private Firebase mRef;
    private Firebase usersRef;

    private Button mWriteButton;
    private Button mRemoveButton;
    private TimePicker mTimePicker;

    private String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_plan);

        postID = getIntent().getStringExtra("key");

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://gankfirebaseexample.firebaseio.com/");
        usersRef = mRef.child("Users");

        mTimePicker = (TimePicker) findViewById(R.id.time_picker_2);

        mWriteButton = (Button) findViewById(R.id.change_plans_button);
        mWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour;
                String meridian;
                if(mTimePicker.getCurrentHour() > 12)
                {
                    hour = mTimePicker.getCurrentHour() - 12;
                    meridian = "PM";
                }
                else
                {
                    hour = mTimePicker.getCurrentHour();
                    meridian = "AM";
                }

                Firebase thisPostRef = usersRef.child(postID);
                Firebase postTime = thisPostRef.child("time");
                postTime.setValue(hour + ":" + mTimePicker.getCurrentMinute() + meridian);
                //Map<String, String> userMap = new HashMap<String, String>();

                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });

        mRemoveButton = (Button) findViewById(R.id.remove_plans_button);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase thisPostRef = usersRef.child(postID);
                thisPostRef.removeValue();

                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.putExtra("plans", false);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_plan, menu);
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
}
