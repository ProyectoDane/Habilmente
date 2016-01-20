package com.belatrix.apadea;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.belatrix.apadea.datamanager.LogManager;
import com.belatrix.apadea.datamanager.UserManager;
import com.belatrix.apadea.datamodel.Log;
import com.belatrix.apadea.datamodel.User;
import com.belatrix.apadea.view.DetailSessionView;

import java.util.List;


public class DetailSessionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_session);

        long userId = getIntent().getExtras().getLong("userId");
        User mUser = UserManager.getsInstance(this).fetchUserById(userId);

        long sessionId = getIntent().getExtras().getLong("sessionId");
        //Session mSession = SessionManager.getsInstance(this).fetchSessionById(sessionId);

        ImageView mSubjectAvatarImageView = (ImageView) findViewById(R.id.subjectAvatar);
        mSubjectAvatarImageView.setImageBitmap(BitmapFactory.decodeByteArray(mUser.getProfileImage(), 0, mUser.getProfileImage().length));

        TextView mSubjectNameTextView = (TextView) findViewById(R.id.subjectNameTextView);
        mSubjectNameTextView.setText(mUser.getCompleteName());

        LinearLayout containerLayout = (LinearLayout) findViewById(R.id.containerLayout);

        String[] levels = {"Deseo diverso","Creencia diversa","Acceso perceptivo","Creencia falsa","Emociones aparentes","Comprensión de uso social del lenguaje no literal"};

        List<Log> logList = LogManager.getsInstance(this).getLogBySession(sessionId);

        for(int i=1;i<7;i++) {
            DetailSessionView view = new DetailSessionView(this,i,levels[i-1]);
            containerLayout.addView(view);

            for (int j = 0; j< logList.size();j++) {
                if (logList.get(j).getCurrentLevel().equals(String.valueOf(i))) {
                    view.addLog(logList.get(j));
                }
            }
        }

        containerLayout.requestLayout();
    }

    public void back(View view) {
        finish();
    }

}
