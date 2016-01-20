package com.belatrix.apadea;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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

public class FifthLevelQuestionActivity extends Activity implements ScoreDialogListener.OnEvaluateListener {

    private Intent intent;

    private TextView mQuestionNumberTextView;
    private TextView mQuestionContentTextView;

    private ImageView mLeftImageView;
    private ImageView mRightImageView;
    private ImageView mCenterImageView;

    private Button mNextQuestionButton;
    private Button mPreviousQuestionButton;

    private int mLevelNumber;
    private int mQuestionNumber = 1;

    private long mCurrentOpenedSessionId;

    private String mJsonData;

    private List<Scenario> mScenarioList;

    private TextView mNextQuestionTextView;
    private TextView mPreviousQuestionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_level_question);

        intent = getIntent();

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

        TextView mRightImageNameTextView = (TextView) findViewById(R.id.rightImageNameTextView);
        TextView mLeftImageNameTextView = (TextView) findViewById(R.id.leftImageNameTextView);

        mLeftImageView = (ImageView) findViewById(R.id.leftImageView);
        mRightImageView = (ImageView) findViewById(R.id.rightImageView);
        mCenterImageView = (ImageView) findViewById(R.id.centerImageView);

        mLeftImageView.setOnClickListener(new ZoomDialogListener(mLeftImageView, mLeftImageNameTextView));
        mCenterImageView.setOnClickListener(new ZoomDialogListener(mCenterImageView, new TextView(this)));
        mRightImageView.setOnClickListener(new ZoomDialogListener(mRightImageView, mRightImageNameTextView));

        Button mBackButton = (Button) findViewById(R.id.backButton);

        mNextQuestionButton = (Button) findViewById(R.id.nextQuestionButton);
        mPreviousQuestionButton = (Button) findViewById(R.id.previousQuestionButton);

        mQuestionNumberTextView.setText(String.format("%d", mQuestionNumber));

        mLevelNumber = intent.getIntExtra("levelNumber", 0);

        mLevelNumberTextView.setText(String.format("%d", mLevelNumber));

        mCurrentOpenedSessionId = intent.getLongExtra("id_currentOpenedSession", 0);
        Long mCurrentSelectedSubjectId = intent.getLongExtra("id_subject", 0);
        Long mCurrentSelectedTherapistId = intent.getLongExtra("id_therapist", 0);

        User mCurrentSelectedSubject = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedSubjectId);
        User mCurrentSelectedTherapist = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedTherapistId);

        mSubjectAvatarImageView.setImageBitmap(Utils.convertBytesToBitmap(mCurrentSelectedSubject.getProfileImage()));
        mTherapistAvatarImageView.setImageBitmap(Utils.convertBytesToBitmap(mCurrentSelectedTherapist.getProfileImage()));

        mSubjectNameTextView.setText(mCurrentSelectedSubject.getFirstName() + " " + mCurrentSelectedSubject.getLastName());
        mTherapistNameTextView.setText(mCurrentSelectedTherapist.getFirstName() + " " + mCurrentSelectedTherapist.getLastName());

        mNextQuestionButton.setOnClickListener(nextListener);

        mPreviousQuestionButton.setOnClickListener(previousListener);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeFirstQuestion() {
        mQuestionNumber = 1;
        mQuestionNumberTextView.setText(String.format("%d/5",mQuestionNumber));

        mNextQuestionButton.setVisibility(View.VISIBLE);

        int mSelectedScenario = 0;
        int id = getApplicationContext().getResources().getIdentifier(mScenarioList.get(mSelectedScenario).getLeftPersonPicture(), "drawable", getApplicationContext().getPackageName());
        mLeftImageView.setImageResource(id);

        id = getApplicationContext().getResources().getIdentifier(mScenarioList.get(mSelectedScenario).getRightPersonPicture(), "drawable", getApplicationContext().getPackageName());
        mRightImageView.setImageResource(id);

        mCenterImageView.setVisibility(View.INVISIBLE);

        Resources resources = FifthLevelQuestionActivity.this.getResources();
        String questionContent = resources.getString(R.string.fifthLevelFirstQuestion);

        mQuestionContentTextView.setText(questionContent);
    }

    private void initializeSecondQuestion() {
        mQuestionNumber = 2;
        mQuestionNumberTextView.setText(String.format("%d/5",mQuestionNumber));

        mCenterImageView.setVisibility(View.VISIBLE);

        Resources resources = FifthLevelQuestionActivity.this.getResources();
        String questionContent = resources.getString(R.string.fifthLevelSecondQuestion);

        mQuestionContentTextView.setText(questionContent);
    }

    private void initializeThirdQuestion() {
        mQuestionNumber = 3;
        mQuestionNumberTextView.setText(String.format("%d/5",mQuestionNumber));
        mCenterImageView.setVisibility(View.INVISIBLE);

        Resources resources = FifthLevelQuestionActivity.this.getResources();
        String questionContent = resources.getString(R.string.fifthLevelThirdQuestion);

        mQuestionContentTextView.setText(questionContent);
    }

    private void initializeFourthQuestion() {
        mQuestionNumber = 4;
        mQuestionNumberTextView.setText(String.format("%d/5",mQuestionNumber));
        mCenterImageView.setVisibility(View.VISIBLE);

        String questionContent = getResources().getString(R.string.fifthLevelFourthQuestion);

        mQuestionContentTextView.setText(questionContent);

        mNextQuestionButton.setOnClickListener(nextListener);
        mNextQuestionButton.setBackgroundResource(R.drawable.next_btn);
        mNextQuestionTextView.setText(getResources().getString(R.string.next_question));
    }

    private void initializeFifthQuestion() {
        mQuestionNumber = 5;
        mQuestionNumberTextView.setText(String.format("%d/5",mQuestionNumber));

        mNextQuestionButton.setOnClickListener(new ScoreDialogListener(FifthLevelQuestionActivity.this));
        mNextQuestionButton.setBackgroundResource(R.drawable.finish_btn);
        mNextQuestionTextView.setText(getResources().getString(R.string.finish));

        String questionContent = getResources().getString(R.string.fifthLevelFifthQuestion);

        mQuestionContentTextView.setText(questionContent);
    }

/*    private void initializeSixthQuestion() {
        mQuestionNumber = 6;
        mQuestionNumberTextView.setText(Integer.toString(mQuestionNumber) + "/5");

        Resources resources = FifthLevelQuestionActivity.this.getResources();
        String questionContent = String.format(resources.getString(R.string.fifthLevelSixthQuestion), mCurrentRightPersonSelected, mScenarioList.get(0).getFourthStatement());

        mQuestionContentTextView.setText(questionContent);

        mNextQuestionButton.setOnClickListener(nextListener);
        mNextQuestionButton.setBackgroundResource(R.drawable.next_btn);
        mNextQuestionTextView.setText("SIGUIENTE PREGUNTA");
    }*/

/*    private void initializeSeventhQuestion() {
        mNextQuestionButton.setOnClickListener(new ScoreDialogListener(FifthLevelQuestionActivity.this));
        mNextQuestionButton.setBackgroundResource(R.drawable.finish_btn);
        mNextQuestionTextView.setText("FINALIZAR EVALUACIÓN");

        mQuestionNumber = 7;
        mQuestionNumberTextView.setText(Integer.toString(mQuestionNumber) + "/5");

        Resources resources = FifthLevelQuestionActivity.this.getResources();
        String questionContent = String.format(resources.getString(R.string.fifthLevelSeventhQuestion), mCurrentRightPersonSelected);

        mQuestionContentTextView.setText(questionContent);
    }*/

    View.OnClickListener previousListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (mQuestionNumber) {
                case 2:
                    mPreviousQuestionButton.setVisibility(View.INVISIBLE);
                    mPreviousQuestionTextView.setVisibility(View.INVISIBLE);
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
                default:
                    mNextQuestionButton.setVisibility(View.VISIBLE);
                    mNextQuestionTextView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPreviousQuestionButton.setVisibility(View.VISIBLE);
            mPreviousQuestionTextView.setVisibility(View.VISIBLE);
            switch (mQuestionNumber) {
                case 1:
                    initializeSecondQuestion();
                    break;
                case 2:
                    initializeThirdQuestion();
                    break;
                case 3:
                    initializeFourthQuestion();
                    break;
                case 4:
                    initializeFifthQuestion();
                    break;
                default:
                    mNextQuestionButton.setVisibility(View.VISIBLE);
                    mNextQuestionTextView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private void loadLevelData() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonData);
            JSONObject situationsJSONObject = jsonObject.getJSONObject("situations");

            JSONObject levelJSONObject = null;

            if (mLevelNumber == 5) {
                levelJSONObject = situationsJSONObject.getJSONObject("fifthlevel");
            }

            JSONArray scenarioJSONArray = levelJSONObject.getJSONArray("scenario");

            mScenarioList = new ArrayList<>();

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

        currentQuestionLog.setCurrentLevel("5");
        currentQuestionLog.setSessionId(mCurrentOpenedSessionId);
        currentQuestionLog.setResult(result);

        LogManager.getsInstance(FifthLevelQuestionActivity.this).getDaoSession().getLogDao().insert(currentQuestionLog);
        finish();
    }
}
