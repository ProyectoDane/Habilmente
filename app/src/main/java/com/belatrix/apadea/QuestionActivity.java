package com.belatrix.apadea;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.belatrix.apadea.datamanager.LogManager;
import com.belatrix.apadea.datamanager.UserManager;
import com.belatrix.apadea.datamodel.Log;
import com.belatrix.apadea.datamodel.ObjectAlternative;
import com.belatrix.apadea.datamodel.ObjectWanted;
import com.belatrix.apadea.datamodel.Person;
import com.belatrix.apadea.datamodel.User;
import com.belatrix.apadea.util.ScoreDialogListener;
import com.belatrix.apadea.util.Utils;
import com.belatrix.apadea.util.ZoomDialogListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends Activity implements ScoreDialogListener.OnEvaluateListener {

    static final String CURRENT_SELECTED_SUBJECT_ID = "id_subject";
    static final String CURRENT_SELECTED_TERAPEUTA_ID = "id_therapist";
    static final String SITUATIONS = "situations";
    static final String FIRST_LEVEL = "firstlevel";
    static final String PERSON = "person";
    static final String LEFT_BUTTON = "left_button";
    static final String CENTER_BUTTON = "center_button";
    static final String RIGHT_BUTTON = "right_button";
    static final String CURRENT_OPENED_SESSION_ID = "id_currentOpenedSession";
    static final String LEVEL_NUMBER = "levelNumber";
    static final String SECOND_LEVEL = "secondlevel";
    static final String THIRD_LEVEL = "thirdlevel";

    private Intent intent;

    private ImageView mLeftAnswerImageView;
    private ImageView mRightAnswerImageView;
    private ImageView mCenterImageView;

    private TextView mLeftAnswerTextView;
    private TextView mCenterAnswerTextView;
    private TextView mRightAnswerTextView;
    private TextView mNextQuestionTextView;
    private TextView mPreviousQuestionTextView;

    private TextView mLevelNumberTextView;
    private TextView mQuestionNumberTextView;
    private TextView mQuestionContentTextView;
    private RelativeLayout mCenterAnswerRelativeLayout;

    private Button mEditCenterAnswerButton;

    private Button mNextQuestionButton;
    private Button mPreviousQuestionButton;

    private View.OnClickListener mNextQuestionClickListener;
    private View.OnClickListener mPreviousQuestionClickListener;

    private int mLevel;

    private int mCurrentQuestion = 1;

    private String mJsonData;
    private List<Person> mPersonList;
    private List<ObjectWanted> mObjectWantedList;
    private List<ObjectAlternative> mObjectAlternativeList;

    private boolean mLeftAnswerChoosedFlag = false;
    private boolean mRightAnswerChoosedFlag = false;

    //private boolean mFirstQuestionActivatedFlag = true;
    //private boolean mSecondQuestionActivatedFlag = false;

    private Long mCurrentOpenedSessionId;

    private String mLeftAnswerCompleteName;
    private String mRightAnswerCompleteName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        intent = getIntent();

        initializeUI();

        mJsonData = Utils.fetchJsonFromFile(this);

        loadLevelData();

        initializeDefaultDataPictures();
    }

    private void initializeUI() {
        ImageView mSubjectAvatarImageView = (ImageView) findViewById(R.id.subjectAvatar);
        ImageView mTherapistAvatarImageView = (ImageView) findViewById(R.id.therapistAvatar);
        TextView mSubjectNameTextView = (TextView) findViewById(R.id.subjectNameTextView);
        TextView mTherapistNameTextView = (TextView) findViewById(R.id.therapistNameTextView);
        mLeftAnswerImageView = (ImageView) findViewById(R.id.leftAnswerImageView);
        mRightAnswerImageView = (ImageView) findViewById(R.id.rightAnswerImageView);
        mCenterImageView = (ImageView) findViewById(R.id.centerImageView);
        mLevelNumberTextView = (TextView) findViewById(R.id.levelNumberTextView);
        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mQuestionContentTextView = (TextView) findViewById(R.id.questionContentTextView);
        Button mEditLeftAnswerButton = (Button) findViewById(R.id.editLeftAnswerButton);
        mEditCenterAnswerButton = (Button) findViewById(R.id.editCenterAnswerButton);
        Button mEditRightAnswerButton = (Button) findViewById(R.id.editRightAnswerButton);

        mLeftAnswerTextView = (TextView) findViewById(R.id.leftAnswerTextView);
        mCenterAnswerTextView = (TextView) findViewById(R.id.centerAnswerTextView);
        mRightAnswerTextView = (TextView) findViewById(R.id.rightAnswerTextView);
        mNextQuestionTextView = (TextView) findViewById(R.id.nextQuestionTextView);
        mPreviousQuestionTextView = (TextView) findViewById(R.id.previousQuestionTextView);
        Button mBackButton = (Button) findViewById(R.id.backButton);
        mCenterAnswerRelativeLayout = (RelativeLayout) findViewById(R.id.centerAnswerRelativeLayout);

        RelativeLayout mLeftAnswerRelativeLayout = (RelativeLayout) findViewById(R.id.leftAnswerRelativeLayout);
        mLeftAnswerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNextQuestionButton.setVisibility(View.VISIBLE);
                mNextQuestionTextView.setVisibility(View.VISIBLE);
                if (mCurrentQuestion == 1) {

                    String alertMessage = "";

                    if (mLevel == 1) {
                        alertMessage = String.format(getString(R.string.firstLevelAlertMessage), mCenterAnswerTextView.getText().toString().toUpperCase(), mRightAnswerCompleteName);
                    } else if (mLevel == 2) {
                        alertMessage = String.format(getString(R.string.secondLevelAlertMessage), mCenterAnswerTextView.getText().toString().toUpperCase(), mRightAnswerCompleteName);
                    }

                    openAlert(alertMessage);

                    mLeftAnswerChoosedFlag = true;
                    //mCurrentQuestion = 2;

                }
            }
        });
        RelativeLayout mRightAnswerRelativeLayout = (RelativeLayout) findViewById(R.id.rightAnswerRelativeLayout);


        mRightAnswerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNextQuestionButton.setVisibility(View.VISIBLE);
                mNextQuestionTextView.setVisibility(View.VISIBLE);
                if (mCurrentQuestion == 1) {


                    String alertMessage = "";
                    if (mLevel == 1) {
                        alertMessage = String.format(getString(R.string.firstLevelAlertMessage), mCenterAnswerTextView.getText().toString().toUpperCase(), mLeftAnswerTextView.getText().toString().toUpperCase());
                    } else if (mLevel == 2) {
                        alertMessage = String.format(getString(R.string.secondLevelAlertMessage), mCenterAnswerTextView.getText().toString().toUpperCase(), mLeftAnswerTextView.getText().toString().toUpperCase());
                    }

                    openAlert(alertMessage);

                    mRightAnswerChoosedFlag = true;
                    //mCurrentQuestion = 2;

                }
            }
        });

        mNextQuestionButton = (Button) findViewById(R.id.nextQuestionButton);
        mPreviousQuestionButton = (Button) findViewById(R.id.previousQuestionButton);

        mQuestionNumberTextView.setText(String.format("%d/2", mCurrentQuestion));

        mLevel = intent.getIntExtra(LEVEL_NUMBER, 0);

        mLevelNumberTextView.setText(String.format("%d", mLevel));

        mCurrentOpenedSessionId = intent.getLongExtra(CURRENT_OPENED_SESSION_ID, 0);
        Long mCurrentSelectedSubjectId = intent.getLongExtra(CURRENT_SELECTED_SUBJECT_ID, 0);
        Long mCurrentSelectedTherapistId = intent.getLongExtra(CURRENT_SELECTED_TERAPEUTA_ID, 0);

        User mCurrentSelectedSubject = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedSubjectId);
        User mCurrentSelectedTherapist = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedTherapistId);

        mSubjectAvatarImageView.setImageBitmap(Utils.convertBytesToBitmap(mCurrentSelectedSubject.getProfileImage()));
        mTherapistAvatarImageView.setImageBitmap(Utils.convertBytesToBitmap(mCurrentSelectedTherapist.getProfileImage()));

        mSubjectNameTextView.setText(String.format("%s %s", mCurrentSelectedSubject.getFirstName(), mCurrentSelectedSubject.getLastName()));
        mTherapistNameTextView.setText(String.format("%s %s", mCurrentSelectedTherapist.getFirstName(), mCurrentSelectedTherapist.getLastName()));

        mNextQuestionTextView.setClickable(true);
        mNextQuestionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLevel == 1) {
                    updateToNextQuestionContentFirstLevel(mLeftAnswerTextView.getText().toString(), mCenterAnswerTextView.getText().toString(), mRightAnswerCompleteName);
                } else if (mLevel == 2) {
                    updateToNextQuestionContentSecondLevel(mLeftAnswerTextView.getText().toString(), mCenterAnswerTextView.getText().toString(), mRightAnswerCompleteName);
                }

                mCurrentQuestion = 2;
                mQuestionNumberTextView.setText(String.format("%d/2", mCurrentQuestion));

                mNextQuestionButton.setVisibility(View.INVISIBLE);
                mNextQuestionTextView.setVisibility(View.INVISIBLE);
                mPreviousQuestionButton.setVisibility(View.VISIBLE);
                mPreviousQuestionTextView.setVisibility(View.VISIBLE);
            }
        };


        mPreviousQuestionClickListener =
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCurrentQuestion == 2) {
                            mNextQuestionButton.setVisibility(View.INVISIBLE);
                            mNextQuestionTextView.setVisibility(View.INVISIBLE);
                            mPreviousQuestionButton.setVisibility(View.INVISIBLE);
                            mPreviousQuestionTextView.setVisibility(View.INVISIBLE);
                            mCurrentQuestion = 1;
                            mQuestionNumberTextView.setText(String.format("%d/2", mCurrentQuestion));

                            /*String alertMessage = "";
                            if (mLevel == 1) {
                                alertMessage = String.format(getString(R.string.firstLevelAlertMessage), mCenterAnswerTextView.getText().toString().toUpperCase(), mLeftAnswerTextView.getText().toString().toUpperCase());
                            } else if (mLevel == 2) {
                                alertMessage = String.format(getString(R.string.secondLevelAlertMessage), mCenterAnswerTextView.getText().toString().toUpperCase(), mLeftAnswerTextView.getText().toString().toUpperCase());
                            }*/
                            updateQuestionContent();
                            //openAlert(alertMessage);
                        }
                    }
                };
        mPreviousQuestionButton.setOnClickListener(mPreviousQuestionClickListener);
        mPreviousQuestionTextView.setClickable(true);
        mPreviousQuestionTextView.setOnClickListener(mPreviousQuestionClickListener);


        mLeftAnswerImageView.setOnClickListener(new ZoomDialogListener(mLeftAnswerImageView, mLeftAnswerTextView));
        mCenterImageView.setOnClickListener(new ZoomDialogListener(mCenterImageView, mCenterAnswerTextView));
        mRightAnswerImageView.setOnClickListener(new ZoomDialogListener(mRightAnswerImageView, mRightAnswerTextView));

        mEditLeftAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog(LEFT_BUTTON);
            }
        });

        mEditCenterAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentQuestion == 1)
                    startDialog(CENTER_BUTTON);
            }
        });

        mEditRightAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog(RIGHT_BUTTON);
            }
        });

/*        mZoomLeftAnswerButton.setOnClickListener(new ZoomDialogListener(mLeftAnswerImageView, mLeftAnswerTextView));
        mZoomCenterAnswerButton.setOnClickListener(new ZoomDialogListener(mCenterImageView, mCenterAnswerTextView));
        mZoomRightAnswerButton.setOnClickListener(new ZoomDialogListener(mRightAnswerImageView, mRightAnswerTextView));*/

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeDefaultDataPictures() {
        int id = getApplicationContext().getResources().getIdentifier(mObjectWantedList.get(0).getObjectWantedPicture(), "drawable", getApplicationContext().getPackageName());
        mLeftAnswerImageView.setImageResource(id);
        mLeftAnswerTextView.setText(mObjectWantedList.get(0).getObjectWantedName().toUpperCase());
        mLeftAnswerCompleteName = mObjectWantedList.get(0).getObjectWantedNameComplete().toUpperCase();

        id = getApplicationContext().getResources().getIdentifier(mPersonList.get(0).getPersonPicture(), "drawable", getApplicationContext().getPackageName());
        mCenterImageView.setImageResource(id);
        mCenterAnswerTextView.setText(mPersonList.get(0).getPersonName().toUpperCase());

        if (mLevel == 3 || mLevel == 4) {
            mCenterAnswerTextView.setVisibility(View.GONE);
            mCenterImageView.setVisibility(View.GONE);
            mEditCenterAnswerButton.setVisibility(View.GONE);
            mCenterAnswerRelativeLayout.setVisibility(View.GONE);

            mRightAnswerImageView.setImageResource(id);
        } else {
            id = getApplicationContext().getResources().getIdentifier(mObjectAlternativeList.get(0).getObjectAlternativePicture(), "drawable", getApplicationContext().getPackageName());
            mRightAnswerImageView.setImageResource(id);
            mRightAnswerTextView.setText(mObjectAlternativeList.get(0).getObjectAlternativeName().toUpperCase());
            mRightAnswerCompleteName = mObjectAlternativeList.get(0).getObjectAlternativeNameComplete().toUpperCase();
        }
        updateQuestionContent();
    }

    private void startDialog(final String typeButton) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(QuestionActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

        builderSingle.setIcon(R.drawable.apadea_logo);
        builderSingle.setTitle("Seleccione una imagen:");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(QuestionActivity.this, R.layout.alert_dialog_row);

        switch (typeButton) {
            case LEFT_BUTTON:
                for (int i = 0; i < mObjectWantedList.size(); i++) {
                    arrayAdapter.add(mObjectWantedList.get(i).getObjectWantedName());
                }
                break;
            case CENTER_BUTTON:
                for (int i = 0; i < mPersonList.size(); i++) {
                    arrayAdapter.add(mPersonList.get(i).getPersonName());
                }
                break;
            case RIGHT_BUTTON:
                for (int i = 0; i < mObjectAlternativeList.size(); i++) {
                    arrayAdapter.add(mObjectAlternativeList.get(i).getObjectAlternativeName());
                }
                break;
        }

        builderSingle.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        //String strName = arrayAdapter.getItem(position);
                        switch (typeButton) {
                            case LEFT_BUTTON: {
                                int id = getApplicationContext().getResources().getIdentifier(mObjectWantedList.get(position).getObjectWantedPicture(), "drawable", getApplicationContext().getPackageName());
                                mLeftAnswerImageView.setImageResource(id);
                                mLeftAnswerTextView.setText(mObjectWantedList.get(position).getObjectWantedName());
                                mLeftAnswerCompleteName = mObjectWantedList.get(position).getObjectWantedNameComplete();
                                break;
                            }
                            case CENTER_BUTTON: {
                                int id = getApplicationContext().getResources().getIdentifier(mPersonList.get(position).getPersonPicture(), "drawable", getApplicationContext().getPackageName());
                                mCenterImageView.setImageResource(id);
                                mCenterAnswerTextView.setText(mPersonList.get(position).getPersonName());
                                break;
                            }
                            case RIGHT_BUTTON: {
                                int id = getApplicationContext().getResources().getIdentifier(mObjectAlternativeList.get(position).getObjectAlternativePicture(), "drawable", getApplicationContext().getPackageName());
                                mRightAnswerImageView.setImageResource(id);
                                mRightAnswerTextView.setText(mObjectAlternativeList.get(position).getObjectAlternativeName());
                                mRightAnswerCompleteName = mObjectAlternativeList.get(position).getObjectAlternativeNameComplete();
                                break;
                            }
                        }
                        updateQuestionContent();
                    }
                });
        builderSingle.show();
    }

    private void openAlert(String message) {
        mQuestionContentTextView.setText(message.toUpperCase());
    }

    private void loadLevelData() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonData);
            JSONObject situationsJSONObject = jsonObject.getJSONObject(SITUATIONS);
            JSONObject levelJSONObject = null;

            if (mLevel == 1) {
                levelJSONObject = situationsJSONObject.getJSONObject(FIRST_LEVEL);
            } else if (mLevel == 2) {
                levelJSONObject = situationsJSONObject.getJSONObject(SECOND_LEVEL);
            } else if (mLevel == 3) {
                levelJSONObject = situationsJSONObject.getJSONObject(THIRD_LEVEL);
            }

            JSONArray personJSONArray = levelJSONObject.getJSONArray(PERSON);

            mPersonList = new ArrayList<>();

            for (int i = 0; i < personJSONArray.length(); i++) {
                JSONObject personJSONObject = personJSONArray.getJSONObject(i);
                Person person = new Person();

                person.setPersonName(personJSONObject.getString("name"));
                person.setPersonPicture(personJSONObject.getString("picture"));

                mPersonList.add(person);
            }

            JSONArray objectWantedJSONArray = levelJSONObject.getJSONArray("object_wanted");

            mObjectWantedList = new ArrayList<>();

            for (int i = 0; i < objectWantedJSONArray.length(); i++) {
                JSONObject objectWantedJSONObject = objectWantedJSONArray.getJSONObject(i);

                ObjectWanted objectWanted = new ObjectWanted();

                objectWanted.setObjectWantedName(objectWantedJSONObject.getString("name"));
                objectWanted.setObjectWantedNameComplete(objectWantedJSONObject.getString("complete_name"));
                objectWanted.setObjectWantedPicture(objectWantedJSONObject.getString("picture"));

                mObjectWantedList.add(objectWanted);
            }

            JSONArray objectAlternativeJSONArray = levelJSONObject.getJSONArray("object_alternative");

            mObjectAlternativeList = new ArrayList<>();

            for (int i = 0; i < objectAlternativeJSONArray.length(); i++) {
                JSONObject objectAlternativeJSONObject = objectAlternativeJSONArray.getJSONObject(i);

                ObjectAlternative objectAlternative = new ObjectAlternative();
                objectAlternative.setObjectAlternativeName(objectAlternativeJSONObject.getString("name"));
                objectAlternative.setObjectAlternativeNameComplete(objectAlternativeJSONObject.getString("complete_name"));
                objectAlternative.setObjectAlternativePicture(objectAlternativeJSONObject.getString("picture"));

                mObjectAlternativeList.add(objectAlternative);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetChoosedOptions() {
        mRightAnswerChoosedFlag = false;
        mLeftAnswerChoosedFlag = false;
        mCurrentQuestion = 1;
        mQuestionNumberTextView.setText(String.format("%d/2", mCurrentQuestion));
    }

    private void updateQuestionContent() {
        if (mCurrentQuestion == 1) {
            if (mLevel == 1) {
                updateQuestionContentFirstLevel(mLeftAnswerTextView.getText().toString().toUpperCase(), mRightAnswerTextView.getText().toString().toUpperCase());
            } else if (mLevel == 2) {
                updateQuestionContentSecondLevel(mCenterAnswerTextView.getText().toString().toUpperCase(), mLeftAnswerCompleteName, mRightAnswerCompleteName);
            } else if (mLevel == 3) {
                showFirstQuestionContentThirdLevel(mLeftAnswerCompleteName);
            }
        }
    }

    private void showFirstQuestionContentThirdLevel(String leftImageName) {
        Resources resources = QuestionActivity.this.getResources();
        String questionContent = String.format(resources.getString(R.string.thirdLevelFirstQuestion), leftImageName);
        mQuestionContentTextView.setText(questionContent.toUpperCase());
    }

    //muestra tarea 1, paso 1
    private void updateQuestionContentFirstLevel(String leftImageName, String rightImageName) {
        Resources resources = QuestionActivity.this.getResources();
        String questionContent = String.format(resources.getString(R.string.firstLevelFirstQuestion), leftImageName, rightImageName);
        mQuestionContentTextView.setText(questionContent.toUpperCase());
        mNextQuestionTextView.setText(getResources().getString(R.string.next_question));
        mNextQuestionButton.setBackgroundResource(R.drawable.next_btn);
        mNextQuestionTextView.setOnClickListener(mNextQuestionClickListener);
        mNextQuestionButton.setOnClickListener(mNextQuestionClickListener);
    }

    //muestra tarea 2, paso 1
    private void updateQuestionContentSecondLevel(String personName, String leftImageName, String rightImageName) {
        String format = getResources().getString(R.string.secondLevelFirstQuestion);
        String questionContent = String.format(format, personName, leftImageName, rightImageName);
        mQuestionContentTextView.setText(questionContent.toUpperCase());
        mNextQuestionTextView.setText(getResources().getString(R.string.next_question));
        mNextQuestionButton.setBackgroundResource(R.drawable.next_btn);
        mNextQuestionTextView.setOnClickListener(mNextQuestionClickListener);
        mNextQuestionButton.setOnClickListener(mNextQuestionClickListener);
    }

    //muestra tarea 1, paso 2
    private void updateToNextQuestionContentFirstLevel(String leftImageName, String personName, String rightImageName) {
        Resources resources = QuestionActivity.this.getResources();
        String questionContent = String.format(resources.getString(R.string.firstLevelSecondQuestion), personName.toUpperCase(), leftImageName.toUpperCase(), rightImageName);
        mQuestionContentTextView.setText(questionContent.toUpperCase());

        View.OnClickListener finishListener = new ScoreDialogListener(QuestionActivity.this);
        mNextQuestionButton.setOnClickListener(finishListener);
        mNextQuestionTextView.setOnClickListener(finishListener);

        mNextQuestionButton.setBackgroundResource(R.drawable.finish_btn);
        mNextQuestionTextView.setText(getResources().getString(R.string.finish));
    }


    //muestra tarea 2, paso 2
    private void updateToNextQuestionContentSecondLevel(String leftImageName, String personName, String rightImageName) {
        Resources resources = QuestionActivity.this.getResources();
        String questionContent = String.format(resources.getString(R.string.secondLevelSecondQuestion), personName.toUpperCase(), leftImageName.toUpperCase(), rightImageName);
        mQuestionContentTextView.setText(questionContent.toUpperCase());

        View.OnClickListener finishListener = new ScoreDialogListener(QuestionActivity.this);
        mNextQuestionButton.setOnClickListener(finishListener);
        mNextQuestionTextView.setOnClickListener(finishListener);

        mNextQuestionButton.setBackgroundResource(R.drawable.finish_btn);
        mNextQuestionTextView.setText(getResources().getString(R.string.finish));
    }

    @Override
    public void onEvaluate(String result) {
        Log currentQuestionLog = new Log();

        currentQuestionLog.setCurrentLevel(mLevelNumberTextView.getText().toString());
        currentQuestionLog.setSessionId(mCurrentOpenedSessionId);
        currentQuestionLog.setResult(result);

        LogManager.getsInstance(QuestionActivity.this).getDaoSession().getLogDao().insert(currentQuestionLog);
        finish();
    }

}
