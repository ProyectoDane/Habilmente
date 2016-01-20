package com.belatrix.apadea.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.belatrix.apadea.R;
import com.belatrix.apadea.datamodel.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User>{

    private Context mContext;
    private List<User> mUsersList;

    public UserAdapter(Context context, List<User> list) {
        super(context,R.layout.item_user,list);
        this.mContext = context;
        this.mUsersList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user, null);

            holder = new UserViewHolder();

            holder.avatarImageView = (ImageView) convertView.findViewById(R.id.profileImageView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);

            convertView.setTag(holder);
        } else {
            holder = (UserViewHolder) convertView.getTag();
        }

        holder.avatarImageView.setImageBitmap(BitmapFactory.decodeByteArray(mUsersList.get(position).getProfileImage(), 0, mUsersList.get(position).getProfileImage().length));
        holder.nameTextView.setText(mUsersList.get(position).getCompleteName());

        return convertView;
    }

    static class UserViewHolder {
        public ImageView avatarImageView;
        public TextView nameTextView;
    }
}
