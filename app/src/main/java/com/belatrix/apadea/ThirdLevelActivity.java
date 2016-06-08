package com.belatrix.apadea;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.belatrix.apadea.datamanager.LogManager;
import com.belatrix.apadea.datamanager.UserManager;
import com.belatrix.apadea.datamodel.Log;
import com.belatrix.apadea.datamodel.User;
import com.belatrix.apadea.util.ScoreDialogListener;
import com.belatrix.apadea.util.Utils;
import com.belatrix.apadea.util.ZoomDialogListener;

public class ThirdLevelActivity extends Activity implements ScoreDialogListener.OnEvaluateListener {

    private TextView mQuestionTextView,mCenterAnswerTextView,mLeftAnswerTextView,mRightAnswerTextView,mQuestionNumberTextView,mPreviousQuestionTextView,mNextQuestionTextView;

    private Button mNextQuestionButton,mPreviousQuestionButton;

    private int mQuestionNumber = 1;

    private boolean mSecondQuestionActivatedFlag = false;

    private Long mCurrentOpenedSessionId;
    private ImageView mCenterImageView,mLeftImageView,mRightImageView;
    private RelativeLayout mCenterAnswerRelativeLayout,mLeftAnswerRelativeLayout,mRightAnswerRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_level_question);

        initializeUI();
    }

    private void initializeUI() {
        ImageView mSubjectAvatarImageView = (ImageView) findViewById(R.id.subjectAvatar);
        ImageView mTherapistAvatarImageView = (ImageView) findViewById(R.id.therapistAvatar);
        TextView mSubjectNameTextView = (TextView) findViewById(R.id.subjectNameTextView);
        TextView mTherapistNameTextView = (TextView) findViewById(R.id.therapistNameTextView);

        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mPreviousQuestionTextView = (TextView) findViewById(R.id.previousQuestionTextView);
        mNextQuestionTextView = (TextView) findViewById(R.id.nextQuestionTextView);
        mQuestionTextView = (TextView) findViewById(R.id.questionContentTextView);
        mCenterAnswerTextView = (TextView) findViewById(R.id.centerAnswerTextView);
        mLeftAnswerTextView = (TextView) findViewById(R.id.leftAnswerTextView);
        mRightAnswerTextView = (TextView) findViewById(R.id.rightAnswerTextView);

        mCenterImageView = (ImageView) findViewById(R.id.centerImageView);
        mLeftImageView = (ImageView) findViewById(R.id.leftImageView);
        mRightImageView = (ImageView) findViewById(R.id.rightImageView);
        mCenterImageView.setOnClickListener(new ZoomDialogListener(mCenterImageView, mCenterAnswerTextView));
        mLeftImageView.setOnClickListener(new ZoomDialogListener(mLeftImageView, mLeftAnswerTextView));
        mRightImageView.setOnClickListener(new ZoomDialogListener(mRightImageView, mRightAnswerTextView));

        mCenterAnswerRelativeLayout = (RelativeLayout) findViewById(R.id.centerAnswerRelativeLayout);
        mLeftAnswerRelativeLayout = (RelativeLayout) findViewById(R.id.leftAnswerRelativeLayout);
        mRightAnswerRelativeLayout = (RelativeLayout) findViewById(R.id.rightAnswerRelativeLayout);

        Button mBackButton = (Button) findViewById(R.id.backButton);

        mNextQuestionButton = (Button) findViewById(R.id.nextQuestionButton);
        mPreviousQuestionButton = (Button) findViewById(R.id.previousQuestionButton);

        mQuestionNumberTextView.setText(String.format("%d/2",mQuestionNumber));

        mCurrentOpenedSessionId = getIntent().getLongExtra("id_currentOpenedSession", 0);
        Long mCurrentSelectedSubjectId = getIntent().getLongExtra("id_subject", 0);
        Long mCurrentSelectedTherapistId = getIntent().getLongExtra("id_therapist", 0);

        User mCurrentSelectedSubject = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedSubjectId);
        User mCurrentSelectedTherapist = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedTherapistId);

        mSubjectAvatarImageView.setImageBitmap(Utils.convertBytesToBitmap(mCurrentSelectedSubject.getProfileImage()));
        mTherapistAvatarImageView.setImageBitmap(Utils.convertBytesToBitmap(mCurrentSelectedTherapist.getProfileImage()));

        mSubjectNameTextView.setText(String.format("%s %s", mCurrentSelectedSubject.getFirstName(), mCurrentSelectedSubject.getLastName()));
        mTherapistNameTextView.setText(String.format("%s %s", mCurrentSelectedTherapist.getFirstName(), mCurrentSelectedTherapist.getLastName()));

        mNextQuestionButton.setOnClickListener(nextListener);

        mPreviousQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNextQuestionButton.setOnClickListener(nextListener);
                mNextQuestionButton.setBackgroundResource(R.drawable.next_btn);
                mNextQuestionTextView.setText(getResources().getString(R.string.next_question));

                if (mSecondQuestionActivatedFlag) {
                    mSecondQuestionActivatedFlag = false;

                    mPreviousQuestionButton.setVisibility(View.INVISIBLE);
                    mPreviousQuestionTextView.setVisibility(View.INVISIBLE);

                    mQuestionNumber = 1;
                    mQuestionNumberTextView.setText(String.format("%d/2",mQuestionNumber));

                    mQuestionTextView.setText(getResources().getString(R.string.thirdLevelFirstQuestion).toUpperCase());

                    mCenterAnswerRelativeLayout.setVisibility(View.INVISIBLE);
                    mCenterImageView.setVisibility(View.INVISIBLE);
                    mLeftAnswerRelativeLayout.setVisibility(View.VISIBLE);
                    mLeftImageView.setVisibility(View.VISIBLE);
                    mRightAnswerRelativeLayout.setVisibility(View.VISIBLE);
                    mRightImageView.setVisibility(View.VISIBLE);
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPreviousQuestionButton.setVisibility(View.VISIBLE);
            mPreviousQuestionTextView.setVisibility(View.VISIBLE);

            mSecondQuestionActivatedFlag = true;

            mQuestionNumber = 2;
            mQuestionNumberTextView.setText(String.format("%d/2",mQuestionNumber));

            mQuestionTextView.setText(getResources().getString(R.string.thirdLevelSecondQuestion));

            mCenterAnswerRelativeLayout.setVisibility(View.VISIBLE);
            mCenterImageView.setVisibility(View.VISIBLE);
            mLeftImageView.setVisibility(View.INVISIBLE);
            mLeftAnswerRelativeLayout.setVisibility(View.INVISIBLE);
            mRightImageView.setVisibility(View.INVISIBLE);
            mRightAnswerRelativeLayout.setVisibility(View.INVISIBLE);

            mNextQuestionButton.setOnClickListener(new ScoreDialogListener(ThirdLevelActivity.this));
            mNextQuestionButton.setBackgroundResource(R.drawable.finish_btn);
            mNextQuestionTextView.setText(getResources().getString(R.string.finish));
        }
    };

    @Override
    public void onEvaluate(String result) {
        Log currentQuestionLog = new Log();
        currentQuestionLog.setCurrentLevel("3");
        currentQuestionLog.setSessionId(mCurrentOpenedSessionId);
        currentQuestionLog.setResult(result);

        LogManager.getsInstance(ThirdLevelActivity.this).getDaoSession().getLogDao().insert(currentQuestionLog);
        finish();
    }
}
