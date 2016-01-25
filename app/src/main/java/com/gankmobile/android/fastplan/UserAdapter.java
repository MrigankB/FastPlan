package com.gankmobile.android.fastplan;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mrigank on 7/9/2015.
 */
public class UserAdapter extends ArrayAdapter
{
    private LayoutInflater inflater;
    ArrayList<User> users;

    public UserAdapter(Activity activity, ArrayList<User> users)
    {
        super(activity, R.layout.user_list_item, users);
        inflater = activity.getWindow().getLayoutInflater();
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if(convertView == null)
        {
            row = inflater.inflate(R.layout.user_list_item, parent, false);
        }

        TextView message1 = (TextView)row.findViewById(R.id.name_text);
        message1.setText(users.get(position).getName());

        TextView message2 = (TextView) row.findViewById(R.id.time_text);
        message2.setText(users.get(position).getTime());

        return row;
    }
}
