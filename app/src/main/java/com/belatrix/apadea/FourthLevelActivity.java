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

public class FourthLevelActivity extends Activity implements ScoreDialogListener.OnEvaluateListener {

    private long mCurrentOpenedSessionId;
    private int mQuestionNumber = 1;

    private RelativeLayout mLeftAnswerLayout,mRightAnswerLayout;
    private TextView mQuestionTextView,mQuestionNumberTextView,mPreviousQuestionTextView,mNextQuestionTextView,mLeftAnswerTextView,mRightAnswerTextView;
    private ImageView mLeftImageView,mRightImageView,mCenterImageView;
    private Button mNextQuestionButton,mPreviousQuestionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_level);

        mCurrentOpenedSessionId = getIntent().getLongExtra("id_currentOpenedSession", 0);
        long mCurrentSelectedSubjectId = getIntent().getLongExtra("id_subject", 0);
        long mCurrentSelectedTherapistId = getIntent().getLongExtra("id_therapist", 0);

        User mCurrentSelectedSubject = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedSubjectId);
        User mCurrentSelectedTherapist = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedTherapistId);

        ImageView mSubjectAvatarImageView = (ImageView) findViewById(R.id.subjectAvatar);
        ImageView mTherapistAvatarImageView = (ImageView) findViewById(R.id.therapistAvatar);

        TextView mSubjectNameTextView = (TextView) findViewById(R.id.subjectNameTextView);
        TextView mTherapistNameTextView = (TextView) findViewById(R.id.therapistNameTextView);

        mSubjectAvatarImageView.setImageBitmap(Utils.convertBytesToBitmap(mCurrentSelectedSubject.getProfileImage()));
        mTherapistAvatarImageView.setImageBitmap(Utils.convertBytesToBitmap(mCurrentSelectedTherapist.getProfileImage()));

        mSubjectNameTextView.setText(String.format("%s %s", mCurrentSelectedSubject.getFirstName(), mCurrentSelectedSubject.getLastName()));
        mTherapistNameTextView.setText(String.format("%s %s", mCurrentSelectedTherapist.getFirstName(), mCurrentSelectedTherapist.getLastName()));

        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mPreviousQuestionTextView = (TextView) findViewById(R.id.previousQuestionTextView);
        mNextQuestionTextView = (TextView) findViewById(R.id.nextQuestionTextView);
        mQuestionTextView = (TextView) findViewById(R.id.questionContentTextView);
        mLeftAnswerTextView = (TextView) findViewById(R.id.leftAnswerTextView);
        mRightAnswerTextView = (TextView) findViewById(R.id.rightAnswerTextView);

        mQuestionTextView.setText(getResources().getString(R.string.fourthLevelFirstQuestion));

        mLeftAnswerLayout = (RelativeLayout) findViewById(R.id.leftImageLayout);
        mRightAnswerLayout = (RelativeLayout) findViewById(R.id.rightImageLayout);

        mLeftImageView = (ImageView) findViewById(R.id.leftImageView);
        mLeftImageView.setOnClickListener(new ZoomDialogListener(mLeftImageView,mLeftAnswerTextView));
        mCenterImageView = (ImageView) findViewById(R.id.centerImageView);
        mCenterImageView.setOnClickListener(new ZoomDialogListener(mCenterImageView,new TextView(this)));
        mRightImageView = (ImageView) findViewById(R.id.rightImageView);
        mRightImageView.setOnClickListener(new ZoomDialogListener(mRightImageView,mRightAnswerTextView));

        mNextQuestionButton = (Button) findViewById(R.id.nextQuestionButton);
        mNextQuestionButton.setOnClickListener(nextListener);

        mPreviousQuestionButton = (Button) findViewById(R.id.previousQuestionButton);
        mPreviousQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestionNumber == 2) {
                    mQuestionTextView.setText(getResources().getString(R.string.fourthLevelFirstQuestion));
                    mLeftImageView.setImageResource(R.drawable.laura);
                    mRightImageView.setImageResource(R.drawable.ana);
                    mLeftAnswerTextView.setText(getResources().getString(R.string.laura));
                    mRightAnswerTextView.setText(getResources().getString(R.string.ana));
                    mLeftAnswerLayout.setVisibility(View.VISIBLE);
                    mRightAnswerLayout.setVisibility(View.VISIBLE);
                    mPreviousQuestionButton.setVisibility(View.INVISIBLE);
                    mPreviousQuestionTextView.setVisibility(View.INVISIBLE);
                } else if (mQuestionNumber == 3) {
                    mQuestionTextView.setText(getResources().getString(R.string.fourthLevelSecondQuestion));
                    mCenterImageView.setVisibility(View.INVISIBLE);
                    mLeftImageView.setImageResource(R.drawable.laura_canasta);
                    mRightImageView.setImageResource(R.drawable.laura_retirandose);
                } else if(mQuestionNumber == 4) {
                    mQuestionTextView.setText(getResources().getString(R.string.fourthLevelThirdQuestion));
                    mCenterImageView.setVisibility(View.VISIBLE);
                    mLeftImageView.setImageResource(R.drawable.ana_canasta);
                    mRightImageView.setImageResource(R.drawable.ana_retirandose);
                    mLeftAnswerLayout.setVisibility(View.INVISIBLE);
                    mRightAnswerLayout.setVisibility(View.INVISIBLE);
                }

                mQuestionNumber--;
                mQuestionNumberTextView.setText(String.format("%d/4",mQuestionNumber));

                mNextQuestionButton.setVisibility(View.VISIBLE);
                mNextQuestionTextView.setVisibility(View.VISIBLE);
                mNextQuestionButton.setOnClickListener(nextListener);
                mNextQuestionButton.setBackgroundResource(R.drawable.next_btn);
                mNextQuestionTextView.setText(getResources().getString(R.string.next_question));
            }
        });
    }

    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPreviousQuestionButton.setVisibility(View.VISIBLE);
            mPreviousQuestionTextView.setVisibility(View.VISIBLE);

            mQuestionNumber++;
            mQuestionNumberTextView.setText(String.format("%d/4",mQuestionNumber));

            if(mQuestionNumber == 2) {
                mQuestionTextView.setText(getResources().getString(R.string.fourthLevelSecondQuestion));
                mLeftImageView.setImageResource(R.drawable.laura_canasta);
                mRightImageView.setImageResource(R.drawable.laura_retirandose);
                mLeftAnswerLayout.setVisibility(View.INVISIBLE);
                mRightAnswerLayout.setVisibility(View.INVISIBLE);

            } else if(mQuestionNumber == 3) {
                mQuestionTextView.setText(getResources().getString(R.string.fourthLevelThirdQuestion));
                mCenterImageView.setVisibility(View.VISIBLE);
                mLeftImageView.setImageResource(R.drawable.ana_canasta);
                mRightImageView.setImageResource(R.drawable.ana_retirandose);
            } else if(mQuestionNumber == 4) {
                mQuestionTextView.setText(getResources().getString(R.string.fourthLevelFourthQuestion));
                mLeftImageView.setImageResource(R.drawable.canasta);
                mCenterImageView.setVisibility(View.INVISIBLE);
                mRightImageView.setImageResource(R.drawable.vaso);

                mLeftAnswerTextView.setText(getResources().getString(R.string.canasta));
                mRightAnswerTextView.setText(getResources().getString(R.string.vaso));

                mLeftAnswerLayout.setVisibility(View.VISIBLE);
                mRightAnswerLayout.setVisibility(View.VISIBLE);

                mNextQuestionButton.setOnClickListener(new ScoreDialogListener(FourthLevelActivity.this));
                mNextQuestionButton.setBackgroundResource(R.drawable.finish_btn);
                mNextQuestionTextView.setText(getResources().getString(R.string.finish));
            }
        }
    };

    public void back (View v) {
        finish();
    }

    @Override
    public void onEvaluate(String result) {
        Log currentQuestionLog = new Log();
        currentQuestionLog.setCurrentLevel("4");
        currentQuestionLog.setSessionId(mCurrentOpenedSessionId);
        currentQuestionLog.setResult(result);

        LogManager.getsInstance(FourthLevelActivity.this).getDaoSession().getLogDao().insert(currentQuestionLog);
        finish();
    }
}
