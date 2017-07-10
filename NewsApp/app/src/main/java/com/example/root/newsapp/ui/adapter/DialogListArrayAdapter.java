package com.example.root.newsapp.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.newsapp.R;
import com.example.root.newsapp.ui.activity.DialogListActivity;

import java.util.ArrayList;

/**
 * Created by root on 4/10/17.
 */

public class DialogListArrayAdapter extends ArrayAdapter<DialogListActivity.Country> {

    private final ArrayList<DialogListActivity.Country> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
        protected ImageView flag;
    }

    public DialogListArrayAdapter(Activity context, ArrayList<DialogListActivity.Country> list) {
        super(context, R.layout.dialog_row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.dialog_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.flag = (ImageView) view.findViewById(R.id.flag);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.flag.setImageDrawable(list.get(position).getFlag());
        return view;
    }
}
