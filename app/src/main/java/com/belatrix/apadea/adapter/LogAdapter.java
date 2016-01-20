package com.belatrix.apadea.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.belatrix.apadea.datamodel.Log;

import java.util.List;

public class LogAdapter extends ArrayAdapter<Log> {
    public LogAdapter(Context context, int resource, List<Log> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
