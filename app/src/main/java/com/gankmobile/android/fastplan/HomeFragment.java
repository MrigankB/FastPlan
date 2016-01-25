package com.gankmobile.android.fastplan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * Created by Mrigank on 7/3/2015.
 */
public class HomeFragment extends Fragment
{
    private Button mInterestedButton;
    private Button mChangePlansButton;
    private ImageView mDrawingView1;
    private ImageView mDrawingView2;
    private TextView mNumInterested;

    private boolean madePlans;
    private long numInterested;
    private String postID;

    private Firebase mRef;

    public static HomeFragment NewInstance(boolean madePlans, String postID)
    {
        HomeFragment frag = new HomeFragment();

        Bundle args = new Bundle();
        args.putBoolean("plans", madePlans);
        args.putString("key", postID);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(getActivity());

        madePlans = getArguments().getBoolean("plans");
        postID = getArguments().getString("key");

    }

    private String getMonth(int month)
    {
        switch (month)
        {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "January";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_home, parent, false);

        mDrawingView1 = (ImageView)v.findViewById(R.id.line1);
        mDrawingView2 = (ImageView)v.findViewById(R.id.line2);
        Bitmap bitmap = Bitmap.createBitmap(20, 300, Bitmap.Config.ALPHA_8);

        TextView mDay = (TextView) v.findViewById(R.id.date_day);
        TextView mMonth = (TextView) v.findViewById(R.id.date_month);
        TextView mYear = (TextView) v.findViewById(R.id.date_year);
        TextView mTime = (TextView) v.findViewById(R.id.time_label);
        mNumInterested = (TextView) v.findViewById(R.id.people_interested_label);

        mRef = new Firebase("https://gankfirebaseexample.firebaseio.com/");

        mRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numInterested = dataSnapshot.getChildrenCount();
                mNumInterested.setText((int) numInterested + " people are interested -->");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        Date currentDate = new Date();
        mDay.setText(currentDate.getDate() + "");
        mMonth.setText(getMonth(currentDate.getMonth()));
        mYear.setText("2016");
        if(currentDate.getHours() > 12)
        {
            mTime.setText(currentDate.getHours() - 12 + ":" + currentDate.getMinutes() + " PM");
        }
        else
        {
            mTime.setText(currentDate.getHours() + ":" + currentDate.getMinutes() + " AM");
        }

        mInterestedButton = (Button) v.findViewById(R.id.interested_button);
        mChangePlansButton = (Button) v.findViewById(R.id.change_plans_button);

        Canvas canvas = new Canvas(bitmap);
        mDrawingView1.setImageBitmap(bitmap);
        mDrawingView2.setImageBitmap(bitmap);

        Paint p = new Paint();
        p.setColor(Color.rgb(200, 200, 200));
        p.setStrokeWidth(15);
        canvas.drawLine(5, 50, 10, 300, p);

        if(!madePlans)
        {
            mInterestedButton.setVisibility(View.VISIBLE);
            mChangePlansButton.setVisibility(View.INVISIBLE);

            mInterestedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), MakePlanActivity.class);
                    startActivity(i);
                }
            });
        }
        else
        {
            mChangePlansButton.setVisibility(View.VISIBLE);
            mInterestedButton.setVisibility(View.INVISIBLE);

            mChangePlansButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ChangePlanActivity.class);
                    i.putExtra("key", postID);
                    startActivity(i);
                }
            });
        }

        return v;
    }
}
