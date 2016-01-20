package com.belatrix.apadea;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.belatrix.apadea.datamanager.LogManager;
import com.belatrix.apadea.datamanager.UserManager;
import com.belatrix.apadea.datamodel.Log;
import com.belatrix.apadea.datamodel.Scenario;
import com.belatrix.apadea.datamodel.User;
import com.belatrix.apadea.util.ScoreDialogListener;
import com.belatrix.apadea.util.Utils;
import com.belatrix.apadea.util.ZoomDialogListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SixthLevelQuestionActivity extends Activity implements ScoreDialogListener.OnEvaluateListener {

    static final String CURRENT_SELECTED_SUBJECT_ID = "id_subject";
    static final String CURRENT_SELECTED_TERAPEUTA_ID = "id_therapist";

    private Intent intent;

    private TextView mQuestionNumberTextView;
    private TextView mQuestionContentTextView;

    private Button mNextQuestionButton;
    private Button mPreviousQuestionButton;

    private int mLevelNumber;

    private int mQuestionNumber = 1;

    private String mJsonData;

    private TextView mNextQuestionTextView;
    private TextView mPreviousQuestionTextView;

    private ImageView mCenterImageView;

    private boolean mGoToSecondQuestionActivated = false;
    private boolean mGoToThirdQuestionActivated = false;
    private boolean mGoToFourthQuestionActivated = false;
    private boolean mGoToFifthQuestionActivated = false;

    private Long mCurrentOpenedSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixth_level_question);

        intent = getIntent();

        mCurrentOpenedSessionId = intent.getLongExtra("id_currentOpenedSession", 0);

        initializeUI();

        mJsonData = Utils.fetchJsonFromFile(this);

        loadLevelData();

        initializeFirstQuestion();
    }

    public void back(View v) {
        finish();
    }

    private void initializeUI() {
        ImageView mSubjectAvatarImageView = (ImageView) findViewById(R.id.subjectAvatar);
        ImageView mTherapistAvatarImageView = (ImageView) findViewById(R.id.therapistAvatar);
        TextView mSubjectNameTextView = (TextView) findViewById(R.id.subjectNameTextView);
        TextView mTherapistNameTextView = (TextView) findViewById(R.id.therapistNameTextView);

        TextView mLevelNumberTextView = (TextView) findViewById(R.id.levelNumberTextView);
        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mQuestionContentTextView = (TextView) findViewById(R.id.questionContentTextView);
        mPreviousQuestionTextView = (TextView) findViewById(R.id.previousQuestionTextView);
        mNextQuestionTextView = (TextView) findViewById(R.id.nextQuestionTextView);

        Button mBackButton = (Button) findViewById(R.id.backButton);

        mNextQuestionButton = (Button) findViewById(R.id.nextQuestionButton);
        mPreviousQuestionButton = (Button) findViewById(R.id.previousQuestionButton);

        mQuestionNumberTextView.setText(String.format("%d",mQuestionNumber));

        mLevelNumber = intent.getIntExtra("levelNumber", 0);

        mLevelNumberTextView.setText(String.format("%d", mLevelNumber));

        mCenterImageView = (ImageView) findViewById(R.id.centerImageView);
        mCenterImageView.setOnClickListener(new ZoomDialogListener(mCenterImageView,new TextView(this)));

        Long mCurrentSelectedSubjectId = intent.getLongExtra(CURRENT_SELECTED_SUBJECT_ID, 0);
        Long mCurrentSelectedTherapistId = intent.getLongExtra(CURRENT_SELECTED_TERAPEUTA_ID, 0);

        User mCurrentSelectedSubject = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedSubjectId);
        User mCurrentSelectedTherapist = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedTherapistId);

        mSubjectAvatarImageView.setImageBitmap(Utils.convertBytesToBitmap(mCurrentSelectedSubject.getProfileImage()));
        mTherapistAvatarImageView.setImageBitmap(Utils.convertBytesToBitmap(mCurrentSelectedTherapist.getProfileImage()));

        mSubjectNameTextView.setText(String.format("%s %s",mCurrentSelectedSubject.getFirstName(),mCurrentSelectedSubject.getLastName()));
        mTherapistNameTextView.setText(String.format("%s %s", mCurrentSelectedTherapist.getFirstName(), mCurrentSelectedTherapist.getLastName()));

        mNextQuestionButton.setOnClickListener(nextListener);
        mPreviousQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestionNumber > 1) {
                    mNextQuestionButton.setVisibility(View.VISIBLE);
                    mNextQuestionTextView.setVisibility(View.VISIBLE);
                }
                if(mQuestionNumber == 2) {
                    mPreviousQuestionButton.setVisibility(View.INVISIBLE);
                    mPreviousQuestionTextView.setVisibility(View.INVISIBLE);
                }
                switch (mQuestionNumber) {
                    case 2:
                        initializeFirstQuestion();
                        break;
                    case 3:
                        initializeSecondQuestion();
                        break;
                    case 4:
                        initializeThirdQuestion();
                        break;
                    case 5:
                        initializeFourthQuestion();
                        break;
                    case 6:
                        initializeFifthQuestion();
                        break;
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

    View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPreviousQuestionButton.setVisibility(View.VISIBLE);
            mPreviousQuestionTextView.setVisibility(View.VISIBLE);
            if (mGoToSecondQuestionActivated) {
                initializeSecondQuestion();
                mGoToSecondQuestionActivated = false;
                mGoToThirdQuestionActivated = true;
            } else if (mGoToThirdQuestionActivated) {
                initializeThirdQuestion();
                mGoToThirdQuestionActivated = false;
                mGoToFourthQuestionActivated = true;
            } else if (mGoToFourthQuestionActivated) {
                initializeFourthQuestion();
                mGoToFourthQuestionActivated = false;
                mGoToFifthQuestionActivated = true;
            } else if (mGoToFifthQuestionActivated) {
                initializeFifthQuestion();
                mGoToFifthQuestionActivated = false;
            }
        }
    };

    private void initializeFirstQuestion() {
        mQuestionNumber = 1;
        mQuestionNumberTextView.setText(String.format("%d/5", mQuestionNumber));
        mGoToSecondQuestionActivated = true;

        mCenterImageView.setImageResource(R.drawable.picnic1);

        String questionContent = getResources().getString(R.string.sixthLevelFirstQuestion);

        mQuestionContentTextView.setText(questionContent);
    }

    private void initializeSecondQuestion() {
        mQuestionNumber = 2;
        mQuestionNumberTextView.setText(String.format("%d/5", mQuestionNumber));

        mCenterImageView.setImageResource(R.drawable.picnic1);

        String questionContent = getResources().getString(R.string.sixthLevelSecondQuestion);

        mQuestionContentTextView.setText(questionContent);
    }

    private void initializeThirdQuestion() {
        mQuestionNumber = 3;
        mQuestionNumberTextView.setText(String.format("%d/5", mQuestionNumber));

        mCenterImageView.setImageResource(R.drawable.picnic2);

        String questionContent = getResources().getString(R.string.sixthLevelThirdQuestion);

        mQuestionContentTextView.setText(questionContent);
    }

    private void initializeFourthQuestion() {
        mQuestionNumber = 4;
        mQuestionNumberTextView.setText(String.format("%d/5", mQuestionNumber));

        mNextQuestionButton.setOnClickListener(nextListener);
        mNextQuestionButton.setBackgroundResource(R.drawable.next_btn);
        mNextQuestionTextView.setText(getResources().getString(R.string.next_question));

        mCenterImageView.setImageResource(R.drawable.picnic3);

        String questionContent = getResources().getString(R.string.sixthLevelFourthQuestion);

        mQuestionContentTextView.setText(questionContent);
    }

    private void initializeFifthQuestion() {
        mQuestionNumber = 5;
        mQuestionNumberTextView.setText(String.format("%d/5", mQuestionNumber));

        mNextQuestionButton.setOnClickListener(new ScoreDialogListener(SixthLevelQuestionActivity.this));
        mNextQuestionButton.setBackgroundResource(R.drawable.finish_btn);
        mNextQuestionTextView.setText(getResources().getString(R.string.finish));

        /*int id = getApplicationContext().getResources().getIdentifier("picnic4", "drawable", getApplicationContext().getPackageName());
        Drawable d = getResources().getDrawable(id);
        mCenterImageView.setImageDrawable(d);*/
        mCenterImageView.setImageResource(R.drawable.picnic4);

        String questionContent = getResources().getString(R.string.sixthLevelFifthQuestion);

        mQuestionContentTextView.setText(questionContent);
    }

    private void loadLevelData() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonData);
            JSONObject situationsJSONObject = jsonObject.getJSONObject("situations");

            JSONObject levelJSONObject = null;

            if (mLevelNumber == 6) {
                levelJSONObject = situationsJSONObject.getJSONObject("sixthlevel");
            }

            JSONArray scenarioJSONArray = levelJSONObject.getJSONArray("scenario");

            List<Scenario> mScenarioList = new ArrayList<>();

            for (int i = 0; i < scenarioJSONArray.length(); i++) {
                JSONObject scenarioJSONObject = scenarioJSONArray.getJSONObject(i);

                Scenario scenario = new Scenario();
                scenario.setScenarioName(scenarioJSONObject.getString("scenario_name"));
                scenario.setFirstStatement(scenarioJSONObject.getString("first_statement"));
                scenario.setSecondStatement(scenarioJSONObject.getString("second_statement"));
                scenario.setThirdStatement(scenarioJSONObject.getString("third_statement"));
                scenario.setFourthStatement(scenarioJSONObject.getString("fourth_statement"));
                scenario.setLeftPersonName(scenarioJSONObject.getString("left_person_name"));
                scenario.setLeftPersonPicture(scenarioJSONObject.getString("left_person_picture"));
                scenario.setRightPersonName(scenarioJSONObject.getString("right_person_name"));
                scenario.setRightPersonPicture(scenarioJSONObject.getString("right_person_picture"));

                mScenarioList.add(scenario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvaluate(String result) {
        Log currentQuestionLog = new Log();

        currentQuestionLog.setCurrentLevel("6");
        currentQuestionLog.setSessionId(mCurrentOpenedSessionId);
        currentQuestionLog.setResult(result);

        LogManager.getsInstance(SixthLevelQuestionActivity.this).getDaoSession().getLogDao().insert(currentQuestionLog);
        finish();
    }
}
