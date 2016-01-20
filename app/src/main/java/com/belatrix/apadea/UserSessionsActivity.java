package com.belatrix.apadea;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.belatrix.apadea.adapter.SessionAdapter;
import com.belatrix.apadea.datamanager.SessionManager;
import com.belatrix.apadea.datamanager.UserManager;
import com.belatrix.apadea.datamodel.Session;
import com.belatrix.apadea.datamodel.User;

import java.util.List;


public class UserSessionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sessions);

        final long userId = getIntent().getExtras().getLong("userId");
        User mUser = UserManager.getsInstance(this).fetchUserById(userId);

        ImageView mSubjectAvatarImageView = (ImageView) findViewById(R.id.subjectAvatar);
        mSubjectAvatarImageView.setImageBitmap(BitmapFactory.decodeByteArray(mUser.getProfileImage(), 0, mUser.getProfileImage().length));

        TextView mSubjectNameTextView = (TextView) findViewById(R.id.subjectNameTextView);
        mSubjectNameTextView.setText(mUser.getCompleteName());

        final List<Session> sessionList = SessionManager.getsInstance(this).fetchSessionsByUser(userId);
        SessionAdapter adapter = new SessionAdapter(this,sessionList);

        ListView mSessionsListView = (ListView) findViewById(R.id.listView);
        mSessionsListView.addHeaderView(getLayoutInflater().inflate(R.layout.header_session, null),null,false);

        View empty = getLayoutInflater().inflate(R.layout.empty_session, null, false);
        addContentView(empty, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSessionsListView.setEmptyView(empty);
        mSessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserSessionsActivity.this,DetailSessionActivity.class);
                intent.putExtra("userId",userId);
                intent.putExtra("sessionId",sessionList.get(position - 1).getId());
                startActivity(intent);
            }
        });
        mSessionsListView.setAdapter(adapter);

    }

    public void back(View view) {
        finish();
    }
}
