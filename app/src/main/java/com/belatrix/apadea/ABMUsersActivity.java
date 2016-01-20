package com.belatrix.apadea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.belatrix.apadea.adapter.UserAdapter;
import com.belatrix.apadea.datamanager.UserManager;
import com.belatrix.apadea.datamodel.User;

import java.util.List;

/**
 * Created by LeoSalmaso on 1/5/16.
 */
public class ABMUsersActivity extends Activity {

    public static final String USER_TYPE = "user_type";
    private String mUserType;
    private UserAdapter mUserAdapter;
    private List<User> mUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        mUserType = getIntent().getExtras().get(USER_TYPE).toString();

        mUsersList = UserManager.getsInstance(this).fetchUsersByType(mUserType);
        mUserAdapter = new UserAdapter(this, mUsersList);

        TextView title = (TextView)findViewById(R.id.title);
        View empty;
        if(mUserType.equals(User.USER_TYPE_SUBJECT)) {
            title.setText(getResources().getString(R.string.subjects));
            empty = getLayoutInflater().inflate(R.layout.empty_subject, null, false);
        } else {
            title.setText(getResources().getString(R.string.therapists));
            empty = getLayoutInflater().inflate(R.layout.empty_therapist, null, false);
        }

        ListView mUsersListView = (ListView) findViewById(R.id.listView);

        addContentView(empty, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mUsersListView.setEmptyView(empty);
        mUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ABMUsersActivity.this, AddUserActivity.class);
                intent.putExtra(AddUserActivity.USER_TYPE, mUserType);
                intent.putExtra(AddUserActivity.USER_ID, mUserAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });

        mUsersListView.setAdapter(mUserAdapter);

        Button addUserButton = (Button)findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ABMUsersActivity.this, AddUserActivity.class);
                intent.putExtra(AddUserActivity.USER_TYPE, mUserType);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mUsersList = UserManager.getsInstance(this).fetchUsersByType(mUserType);
        mUserAdapter.clear();
        mUserAdapter.addAll(mUsersList);
    }


    public void back(View view) {
        finish();
    }
}
