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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Mrigank on 7/9/2015.
 */
public class ListFragment extends Fragment
{
    ListView mList;
    ArrayList<User> users;
    ArrayAdapter<User> adapter;

    private Firebase mRef;
    private Firebase usersRef;
    private Firebase userCount;
    private long count;

    public static ListFragment NewInstance()
    {
        ListFragment frag = new ListFragment();

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getActivity());
        mRef = new Firebase("https://gankfirebaseexample.firebaseio.com/");
        usersRef = mRef.child("Users");
        users = new ArrayList<>();

        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //dataSnapshot.getKey();
                Map<String, String> newEntry = (Map<String, String>) dataSnapshot.getValue();
                User person = new User(newEntry.get("name"), newEntry.get("time"));
                users.add(person);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Map<String, String> newEntry = (Map<String, String>) dataSnapshot.getValue();
                User person = new User(newEntry.get("name"), newEntry.get("time"));
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getName().equals(newEntry.get("name"))) {
                        users.remove(i);
                    }
                }
                users.add(person);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Map<String, String> deletedEntry = (Map<String, String>) dataSnapshot.getValue();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getName().equals(deletedEntry.get("name"))) {
                        users.remove(i);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        adapter = new UserAdapter(getActivity(), users);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_list, parent, false);

        mList = (ListView) v.findViewById(R.id.people_list);
        mList.setAdapter(adapter);

        ImageView mBorder = (ImageView) v.findViewById(R.id.border);

        Bitmap map = Bitmap.createBitmap(getActivity().getWindowManager().getDefaultDisplay().getWidth(), 240, Bitmap.Config.ALPHA_8);

        mBorder.setImageBitmap(map);
        Canvas canvas = new Canvas(map);

        Paint p = new Paint();
        p.setColor(Color.rgb(200, 200, 200));
        p.setStrokeWidth(15);
        canvas.drawLine(0, 5, getActivity().getWindowManager().getDefaultDisplay().getWidth(), 5, p);


        return v;
    }

}
