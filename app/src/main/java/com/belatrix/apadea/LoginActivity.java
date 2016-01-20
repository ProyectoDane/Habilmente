package com.belatrix.apadea;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.belatrix.apadea.datamanager.SessionManager;
import com.belatrix.apadea.datamanager.UserManager;
import com.belatrix.apadea.datamodel.Session;
import com.belatrix.apadea.datamodel.User;

import java.util.Date;
import java.util.List;

public class LoginActivity extends Activity {
    public static final String SUBJECT = "subject";
    public static final String TERAPEUTA = "therapist";
    static final String USER_ID = "user_id";
    static final String USER_TYPE = "user_type";
    static final String CURRENT_SELECTED_SUBJECT_ID = "id_subject";
    static final String CURRENT_SELECTED_TERAPEUTA_ID = "id_therapist";
    static final String CURRENT_OPENED_SESSION_ID = "id_currentOpenedSession";

    private ImageView mSelectedSubjectAvatarImageView;
    private ImageView mSelectedTherapistAvatarImageView;
    private LinearLayout mSubjectsLinearLayout;
    private LinearLayout mTherapistsLinearLayout;

    private List<User> mSubjectsList;
    private List<User> mTherapistsList;
    private TextView mTherapistNameTextView;
    private TextView mSubjectNameTextView;

    private Long mCurrentSelectedSubject;
    private Long mCurrentSelectedTherapist;

    private boolean mIsBackFromEditUser;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSubjects();
        loadTherapists();

        if(mIsBackFromEditUser) {
            mIsBackFromEditUser = false;

            mCurrentSelectedSubject = null;
            mCurrentSelectedTherapist = null;

            mSubjectNameTextView.setText("");
            mSelectedSubjectAvatarImageView.setImageResource(R.drawable.default_avatar);

            mTherapistNameTextView.setText("");
            mSelectedTherapistAvatarImageView.setImageResource(R.drawable.default_avatar);

        }
    }

    private void initializeUI() {
        mSelectedSubjectAvatarImageView = (ImageView) findViewById(R.id.subjectLargeAvatar);
        mSelectedTherapistAvatarImageView = (ImageView) findViewById(R.id.therapistLargeAvatar);
        mSubjectsLinearLayout = (LinearLayout) findViewById(R.id.subjectsLinearLayout);
        mTherapistsLinearLayout = (LinearLayout) findViewById(R.id.therapistsLinearLayout);

        mTherapistNameTextView = (TextView) findViewById(R.id.therapistNameTextView);
        mSubjectNameTextView = (TextView) findViewById(R.id.subjectNameTextView);

        //Button mAddUserButton = (Button) findViewById(R.id.addUserBtn);
        mSelectedSubjectAvatarImageView.setImageResource(R.drawable.default_avatar);
        mSelectedTherapistAvatarImageView.setImageResource(R.drawable.default_avatar);

        TextView mIniciarTextView = (TextView) findViewById(R.id.iniciarTextView);

        mIniciarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSelectedSubject != null && mCurrentSelectedTherapist != null) {

                    Long idCurrentOpenedSession;

                    if(SessionManager.getsInstance(LoginActivity.this).hasSessionsOpened(mCurrentSelectedSubject,mCurrentSelectedTherapist)){
                        idCurrentOpenedSession = SessionManager.getsInstance(LoginActivity.this).getLastSession(mCurrentSelectedSubject,mCurrentSelectedTherapist);
                    } else {
                        Date currentDeviceDate = new Date();

                        Session currentSession = new Session();
                        currentSession.setSubjectId(mCurrentSelectedSubject);
                        currentSession.setTherapistId(mCurrentSelectedTherapist);
                        currentSession.setSessionStartDate(currentDeviceDate);
                        currentSession.setClosed(false);

                        idCurrentOpenedSession = SessionManager.getsInstance(LoginActivity.this).getDaoSession().getSessionDao().insert(currentSession);
                    }

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    intent.putExtra(CURRENT_SELECTED_SUBJECT_ID, mCurrentSelectedSubject);
                    intent.putExtra(CURRENT_SELECTED_TERAPEUTA_ID, mCurrentSelectedTherapist);
                    intent.putExtra(CURRENT_OPENED_SESSION_ID, idCurrentOpenedSession);

                    startActivity(intent);
                } else {
                    if (mCurrentSelectedSubject == null && mCurrentSelectedTherapist == null) {
                        Toast.makeText(LoginActivity.this, "Seleccione un evaluado y un evaluador", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mCurrentSelectedSubject == null) {
                            Toast.makeText(LoginActivity.this, "Seleccione un evaluado ", Toast.LENGTH_SHORT).show();
                        }
                        if (mCurrentSelectedTherapist == null) {
                            Toast.makeText(LoginActivity.this, "Seleccione un evaluador", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void loadSubjects() {
        mSubjectsList = UserManager.getsInstance(this).fetchUsersByType(SUBJECT);

        if (mSubjectsLinearLayout.getChildCount() > 0) {
            mSubjectsLinearLayout.removeAllViews();
        }

        for (int i = 0; i < mSubjectsList.size(); i++) {
            final int avatar = i;
            final ViewGroup scenarioView = (ViewGroup) getLayoutInflater().inflate(R.layout.item_user_avatar, null, false);

            ((ImageView) scenarioView.findViewById(R.id.userAvatar)).setImageBitmap(convertBytesToBitmap(mSubjectsList.get(i).getProfileImage()));
            mSubjectsLinearLayout.addView(scenarioView);

            // This is to set the big avatar selected.
            RelativeLayout scenarioItem = ((RelativeLayout) scenarioView.findViewById(R.id.userAvatarItem));
            scenarioItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSubjectNameTextView.setText(mSubjectsList.get(avatar).getFirstName() + " " + mSubjectsList.get(avatar).getLastName());
                    mSelectedSubjectAvatarImageView.setImageBitmap(convertBytesToBitmap(mSubjectsList.get(avatar).getProfileImage()));
                    mCurrentSelectedSubject = mSubjectsList.get(avatar).getId();
                }
            });
        }
    }

    private void loadTherapists() {
        mTherapistsList = UserManager.getsInstance(this).fetchUsersByType(TERAPEUTA);

        if (mTherapistsLinearLayout.getChildCount() > 0) {
            mTherapistsLinearLayout.removeAllViews();
        }

        for (int i = 0; i < mTherapistsList.size(); i++) {
            final int avatar = i;
            final ViewGroup scenarioView = (ViewGroup) getLayoutInflater().inflate(R.layout.item_user_avatar, null, false);

            ((ImageView) scenarioView.findViewById(R.id.userAvatar)).setImageBitmap(convertBytesToBitmap(mTherapistsList.get(i).getProfileImage()));
            mTherapistsLinearLayout.addView(scenarioView);

            // This is to set the big avatar selected.
            RelativeLayout scenarioItem = ((RelativeLayout) scenarioView.findViewById(R.id.userAvatarItem));
            scenarioItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTherapistNameTextView.setText(mTherapistsList.get(avatar).getFirstName() + " " + mTherapistsList.get(avatar).getLastName());
                    mSelectedTherapistAvatarImageView.setImageBitmap(convertBytesToBitmap(mTherapistsList.get(avatar).getProfileImage()));

                    mCurrentSelectedTherapist = mTherapistsList.get(avatar).getId();
                }
            });
        }
    }

    public void goToUserList (View view) {

        Intent intent = new Intent(LoginActivity.this, ABMUsersActivity.class);

        if(view.getId() == R.id.addPacient) {
            intent.putExtra(AddUserActivity.USER_TYPE, User.USER_TYPE_SUBJECT);
        } else {
            intent.putExtra(AddUserActivity.USER_TYPE, User.USER_TYPE_TERAPEUTA);
        }

        startActivity(intent);
    }

    public void goToUsersStatistics(View view) {
        Intent intent = new Intent(LoginActivity.this,UsersListActivity.class);
        startActivity(intent);
    }

    public static Bitmap convertBytesToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
