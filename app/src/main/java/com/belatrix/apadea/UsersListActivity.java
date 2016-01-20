package com.belatrix.apadea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.belatrix.apadea.adapter.UserAdapter;
import com.belatrix.apadea.datamanager.UserManager;
import com.belatrix.apadea.datamodel.User;

import java.util.List;

public class UsersListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        List<User> mUsersList = UserManager.getsInstance(this).fetchUsersByType("subject");

        final UserAdapter adapter = new UserAdapter(this,mUsersList);

        ListView mUsersListView = (ListView) findViewById(R.id.listView);
        View empty = getLayoutInflater().inflate(R.layout.empty_subject, null, false);
        addContentView(empty, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mUsersListView.setEmptyView(empty);
        mUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UsersListActivity.this, UserSessionsActivity.class);
                intent.putExtra("userId", adapter.getItem(position).getId());
                startActivity(intent);
            }
        });
        mUsersListView.setAdapter(adapter);

        Button addUserButton = (Button)findViewById(R.id.addUserButton);
        addUserButton.setVisibility(View.GONE);
    }

    public void back(View view) {
        finish();
    }
}
