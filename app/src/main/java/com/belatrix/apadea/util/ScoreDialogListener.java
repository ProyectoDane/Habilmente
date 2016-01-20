package com.belatrix.apadea.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.belatrix.apadea.FifthLevelQuestionActivity;
import com.belatrix.apadea.FourthLevelActivity;
import com.belatrix.apadea.QuestionActivity;
import com.belatrix.apadea.R;
import com.belatrix.apadea.SixthLevelQuestionActivity;
import com.belatrix.apadea.ThirdLevelActivity;

public class ScoreDialogListener implements View.OnClickListener {

    private Activity activity;

    public ScoreDialogListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Dialog scoreDialog = new Dialog(v.getContext());
        scoreDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        scoreDialog.setContentView(inflater.inflate(R.layout.dialog_socre, null));

        final RadioGroup radioGroup = (RadioGroup) scoreDialog.findViewById(R.id.dialogRadioGroup);

        radioGroup.getChildAt(1).performClick();

        scoreDialog.findViewById(R.id.scoreCancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreDialog.dismiss();
            }
        });

        scoreDialog.findViewById(R.id.scoreOkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnEvaluateListener listener;

                if(activity instanceof QuestionActivity) {
                    listener = (QuestionActivity) activity;
                } else if (activity instanceof ThirdLevelActivity) {
                    listener = (ThirdLevelActivity) activity;
                } else if (activity instanceof FourthLevelActivity) {
                    listener = (FourthLevelActivity) activity;
                } else if (activity instanceof FifthLevelQuestionActivity) {
                    listener = (FifthLevelQuestionActivity) activity;
                } else if (activity instanceof SixthLevelQuestionActivity) {
                    listener = (SixthLevelQuestionActivity) activity;
                } else {
                    scoreDialog.dismiss();
                    return;
                }

                String selection = "";
                if(radioGroup.getCheckedRadioButtonId()!=-1){
                    int id= radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(id);
                    int radioId = radioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                    selection = (String) btn.getText();
                }

                listener.onEvaluate(selection);
                scoreDialog.dismiss();
            }
        });

/*        final EditText observationsEditText = (EditText) scoreDialog.findViewById(R.id.observationsEditText);

        ((CheckBox) scoreDialog.findViewById(R.id.dialogCheckbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                observationsEditText.setEnabled(isChecked);
            }
        });*/

        scoreDialog.show();
    }

    public interface OnEvaluateListener{
        void onEvaluate(String result);
    }
}
