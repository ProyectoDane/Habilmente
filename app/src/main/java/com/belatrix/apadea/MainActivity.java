package com.belatrix.apadea;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.belatrix.apadea.datamanager.SessionManager;
import com.belatrix.apadea.datamanager.UserManager;
import com.belatrix.apadea.datamodel.Session;
import com.belatrix.apadea.datamodel.User;

import java.util.Date;

public class MainActivity extends Activity {

    private Intent intent;

    static final String CURRENT_SELECTED_SUBJECT_ID = "id_subject";
    static final String CURRENT_SELECTED_TERAPEUTA_ID = "id_therapist";
    static final String CURRENT_OPENED_SESSION_ID = "id_currentOpenedSession";
    static final String LEVEL_NUMBER="levelNumber";

    private Long mCurrentOpenedSessionId;
    private Long mCurrentSelectedSubjectId;
    private Long mCurrentSelectedTherapistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = getIntent();
        initializeUI();
    }

    private void initializeUI() {
        ImageView mSubjectAvatarImageView = (ImageView) findViewById(R.id.subjectAvatar);
        ImageView mTherapistAvatarImageView = (ImageView) findViewById(R.id.therapistAvatar);
        TextView mSubjectNameTextView = (TextView) findViewById(R.id.subjectNameTextView);
        TextView mTherapistNameTextView = (TextView) findViewById(R.id.therapistNameTextView);

        RelativeLayout mFirstLevelRelativeLayout = (RelativeLayout) findViewById(R.id.firstLevelRelativeLayout);
        RelativeLayout mSecondLevelRelativeLayout = (RelativeLayout) findViewById(R.id.secondLevelRelativeLayout);
        RelativeLayout mThirdLevelRelativeLayout = (RelativeLayout) findViewById(R.id.thirdLevelRelativeLayout);
        RelativeLayout mFourthLevelRelativeLayout = (RelativeLayout) findViewById(R.id.fourthLevelRelativeLayout);
        RelativeLayout mFifthLevelRelativeLayout = (RelativeLayout) findViewById(R.id.fifthLevelRelativeLayout);
        RelativeLayout mSixthLevelRelativeLayout = (RelativeLayout) findViewById(R.id.sixthLevelRelativeLayout);

        ImageView mCloseSessionImageView = (ImageView) findViewById(R.id.closeSessionImageView);

        Button mBackButton = (Button) findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCloseSessionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSession();
            }
        });

        mFirstLevelRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstLevelIntent = new Intent(MainActivity.this, QuestionActivity.class);

                firstLevelIntent.putExtra(CURRENT_SELECTED_SUBJECT_ID, mCurrentSelectedSubjectId);
                firstLevelIntent.putExtra(CURRENT_SELECTED_TERAPEUTA_ID, mCurrentSelectedTherapistId);
                firstLevelIntent.putExtra(CURRENT_OPENED_SESSION_ID, mCurrentOpenedSessionId);
                firstLevelIntent.putExtra(LEVEL_NUMBER, 1);

                startActivity(firstLevelIntent);
            }
        });

        mSecondLevelRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secondLevelIntent = new Intent(MainActivity.this, QuestionActivity.class);

                secondLevelIntent.putExtra(CURRENT_SELECTED_SUBJECT_ID, mCurrentSelectedSubjectId);
                secondLevelIntent.putExtra(CURRENT_SELECTED_TERAPEUTA_ID, mCurrentSelectedTherapistId);
                secondLevelIntent.putExtra(CURRENT_OPENED_SESSION_ID, mCurrentOpenedSessionId);
                secondLevelIntent.putExtra(LEVEL_NUMBER, 2);

                startActivity(secondLevelIntent);
            }
        });

        mThirdLevelRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent thirdLevelIntent = new Intent(MainActivity.this, ThirdLevelActivity.class);

                thirdLevelIntent.putExtra(CURRENT_SELECTED_SUBJECT_ID, mCurrentSelectedSubjectId);
                thirdLevelIntent.putExtra(CURRENT_SELECTED_TERAPEUTA_ID, mCurrentSelectedTherapistId);
                thirdLevelIntent.putExtra(CURRENT_OPENED_SESSION_ID, mCurrentOpenedSessionId);

                startActivity(thirdLevelIntent);
            }
        });

        mFourthLevelRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fourthLevelIntent = new Intent(MainActivity.this, FourthLevelActivity.class);

                fourthLevelIntent.putExtra(CURRENT_SELECTED_SUBJECT_ID, mCurrentSelectedSubjectId);
                fourthLevelIntent.putExtra(CURRENT_SELECTED_TERAPEUTA_ID, mCurrentSelectedTherapistId);
                fourthLevelIntent.putExtra(CURRENT_OPENED_SESSION_ID, mCurrentOpenedSessionId);

                startActivity(fourthLevelIntent);
            }
        });

        mFifthLevelRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fifthLevelIntent = new Intent(MainActivity.this, FifthLevelQuestionActivity.class);

                fifthLevelIntent.putExtra(CURRENT_SELECTED_SUBJECT_ID, mCurrentSelectedSubjectId);
                fifthLevelIntent.putExtra(CURRENT_SELECTED_TERAPEUTA_ID, mCurrentSelectedTherapistId);
                fifthLevelIntent.putExtra(CURRENT_OPENED_SESSION_ID, mCurrentOpenedSessionId);
                fifthLevelIntent.putExtra(LEVEL_NUMBER, 5);

                startActivity(fifthLevelIntent);
            }
        });

        mSixthLevelRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sixthLevelIntent = new Intent(MainActivity.this, SixthLevelQuestionActivity.class);

                sixthLevelIntent.putExtra(CURRENT_SELECTED_SUBJECT_ID, mCurrentSelectedSubjectId);
                sixthLevelIntent.putExtra(CURRENT_SELECTED_TERAPEUTA_ID, mCurrentSelectedTherapistId);
                sixthLevelIntent.putExtra(CURRENT_OPENED_SESSION_ID, mCurrentOpenedSessionId);
                sixthLevelIntent.putExtra(LEVEL_NUMBER, 6);

                startActivity(sixthLevelIntent);
            }
        });

        mCurrentOpenedSessionId = intent.getLongExtra(CURRENT_OPENED_SESSION_ID, 0);

        // To fill in the pictures on the top left corner.
        mCurrentSelectedSubjectId = intent.getLongExtra(CURRENT_SELECTED_SUBJECT_ID, 0);
        mCurrentSelectedTherapistId = intent.getLongExtra(CURRENT_SELECTED_TERAPEUTA_ID, 0);

        User mCurrentSelectedSubject = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedSubjectId);
        User mCurrentSelectedTherapist = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedTherapistId);

        mSubjectAvatarImageView.setImageBitmap(convertBytesToBitmap(mCurrentSelectedSubject.getProfileImage()));
        mTherapistAvatarImageView.setImageBitmap(convertBytesToBitmap(mCurrentSelectedTherapist.getProfileImage()));

        mSubjectNameTextView.setText(mCurrentSelectedSubject.getFirstName() + " " + mCurrentSelectedSubject.getLastName());
        mTherapistNameTextView.setText(mCurrentSelectedTherapist.getFirstName() + " " + mCurrentSelectedTherapist.getLastName());
    }

    public void back(View view) {
        closeSession();
        finish();
    }

    // Methods to support storing image to Greendao database
/*    public static byte[] convertBitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }*/

    public static Bitmap convertBytesToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    private void closeSession() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar sesión");
        builder.setMessage("Seguro desea dar por finalizada la evaluación?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Session sessionToUpdate = SessionManager.getsInstance(MainActivity.this).getDaoSession().getSessionDao().load(mCurrentOpenedSessionId);
                Date currentDeviceDate = new Date();
                sessionToUpdate.setSessionEndDate(currentDeviceDate);
                sessionToUpdate.setClosed(true);
                SessionManager.getsInstance(MainActivity.this).getDaoSession().getSessionDao().update(sessionToUpdate);
                finish();
            }
        });
        builder.create().show();
    }
}
