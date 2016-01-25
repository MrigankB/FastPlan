package com.gankmobile.android.fastplan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class MakePlanActivity extends ActionBarActivity {

    private Firebase mRef;
    private Firebase usersRef;

    private Button mWriteButton;
    private EditText mNameText;
    private EditText mTimeText;
    private TimePicker mTimePicker;

    private String postID;
    private Date savedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_plan);

        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://gankfirebaseexample.firebaseio.com/");
        usersRef = mRef.child("Users");

        mNameText = (EditText) findViewById(R.id.write_name_text);
        mTimePicker = (TimePicker) findViewById(R.id.time_picker);

        mWriteButton = (Button) findViewById(R.id.write_data_button);
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


                Firebase thisPostRef = usersRef.push();
                Map<String, String> userMap = new HashMap<String, String>();
                userMap.put("name", mNameText.getText() + "");
                userMap.put("time", hour + ":" + mTimePicker.getCurrentMinute() + meridian);
                thisPostRef.setValue(userMap);

                //store postID into memory
                postID = thisPostRef.getKey();
                // Save savedDate as the current date and store it into memory
                savedDate = new Date();
                int month = savedDate.getMonth();
                int day = savedDate.getDate();
                JSONSerializer serializer = new JSONSerializer(getApplicationContext(), "post_info.json",
                        new String[] {"key", "month", "day"} );

                boolean isSaved = saveData(serializer, month, day);


                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean saveData(JSONSerializer serializer, int month, int day)
    {
        try
        {
            serializer.saveFile( new String[] {postID, month + "", day + ""});
            Log.d("save_file", "Intent info saved to file");
            return true;
        }
        catch (Exception e)
        {
            Log.e("save_file", "File save unsuccessful");
            return false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_plan, menu);
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
