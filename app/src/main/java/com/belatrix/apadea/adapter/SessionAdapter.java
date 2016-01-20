package com.belatrix.apadea.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.belatrix.apadea.R;
import com.belatrix.apadea.datamodel.Session;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class SessionAdapter extends ArrayAdapter<Session>{

    public static DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    private Context mContext;
    private List<Session> mSessions;

    public SessionAdapter(Context context, List<Session> objects) {
        super(context, R.layout.item_session, objects);
        mContext = context;
        mSessions = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Session session = mSessions.get(position);

        SessionViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_session, null);
            holder = new SessionViewHolder();
            holder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
            holder.statusTextView = (TextView) convertView.findViewById(R.id.statusTextView);
            holder.therapistTextView = (TextView) convertView.findViewById(R.id.therapistTextView);
            convertView.setTag(holder);
        } else {
            holder = (SessionViewHolder) convertView.getTag();
        }

        holder.dateTextView.setText(String.format("Evaluación del %s",df.format(session.getSessionStartDate())));
        holder.statusTextView.setText(session.getClosed()?"Finalizada":"En curso");
        holder.therapistTextView.setText(session.getTherapist().getCompleteName());

        return convertView;
    }

    static class SessionViewHolder {
        public TextView dateTextView;
        public TextView statusTextView;
        public TextView therapistTextView;
    }
}
