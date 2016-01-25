package com.gankmobile.android.fastplan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mrigank on 7/14/15.
 */
public class ChatFragment extends Fragment
{
    ListView mChatList;
    EditText mTextbox;
    Button mSendButton;
    private String postID;
    private long currentMessage;

    private String postName;
    private Firebase mRef;

    private static boolean testingMode = false;

    public static ChatFragment NewInstance(String postID)
    {
        ChatFragment frag = new ChatFragment();

        Bundle args = new Bundle();
        args.putString("postIDChat", postID);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(getActivity());
        mRef = new Firebase("https://gankfirebaseexample.firebaseio.com/");

        postID = getArguments().getString("postIDChat");

        Firebase mUserRef = mRef.child("Users");
        mUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                if(dataSnapshot.getKey().equals(postID))
                {
                    Map<String, String> userMap = (Map<String, String>)dataSnapshot.getValue();
                    postName = userMap.get("name");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getRecentMessage()
    {
        Firebase mMessageRef = mRef.child("Current_Message");

        mMessageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Map<String, Integer> messageMap = (Map<String, Integer>) dataSnapshot.getValue();
                //currentMessage = (int) messageMap.get("current_message");
                currentMessage = (long) dataSnapshot.getValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                currentMessage = (long) dataSnapshot.getValue();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v;

        if(testingMode) {
            v = inflater.inflate(R.layout.fragment_chat, parent, false);
            mChatList = (ListView) v.findViewById(R.id.chat_list_view);

            mTextbox = (EditText) v.findViewById(R.id.user_text_box);
            mSendButton = (Button) v.findViewById(R.id.submit_button);
            mSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Firebase mChatRef = mRef.child("Chat");
                    getRecentMessage();
                    mChatRef.child("Message_" + currentMessage).setValue(postName + " says: " + mTextbox.getText() + "");

                    Firebase mMessageRef = mRef.child("Current_Message");

                    currentMessage++;
                    if (currentMessage > 5) {
                        currentMessage = 1;
                    }
                    Map<String, Integer> messageMap = new HashMap<String, Integer>();
                    messageMap.put("current_message", (int) currentMessage);


                    mMessageRef.setValue(messageMap);
                }
            });
        }
        else
        {
            v = inflater.inflate(R.layout.unfinished, parent, false);
        }

        return v;
    }
}
