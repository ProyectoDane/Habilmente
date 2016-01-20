package com.belatrix.apadea.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.belatrix.apadea.R;
import com.belatrix.apadea.datamodel.Log;

public class DetailSessionView extends RelativeLayout {

    //private LinearLayout mBottomRelativeLayout;

    private TextView resultTextView;

    public DetailSessionView(Context context,int number,String description) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View root = inflater.inflate(R.layout.item_level, this, true);

        TextView mNumberTextView = (TextView) root.findViewById(R.id.taskTextView);
        mNumberTextView.setText(String.format("Tarea %d",number));

        TextView mDescriptionTextView = (TextView) root.findViewById(R.id.descriptionTextView);
        mDescriptionTextView.setText(description);

        resultTextView = (TextView) root.findViewById(R.id.resultadoTextView);

        //final ImageView arrow = (ImageView) findViewById(R.id.expandImageView);

/*        RelativeLayout mTopRelativeLayout = (RelativeLayout) root.findViewById(R.id.topRelativelayout);
        mTopRelativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomRelativeLayout.getVisibility() == View.VISIBLE) {
                    mBottomRelativeLayout.setVisibility(View.GONE);
                    arrow.setImageDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float));
                } else {
                    mBottomRelativeLayout.setVisibility(View.VISIBLE);
                    arrow.setImageDrawable(getResources().getDrawable(android.R.drawable.arrow_up_float));
                }
            }
        });*/

//        mBottomRelativeLayout = (LinearLayout) root.findViewById(R.id.bottomRelativeLayout);
    }

    public void addLog(Log log) {
        TextView txt = new TextView(getContext());
        txt.setTextColor(Color.WHITE);
        txt.setTextSize(10);
        txt.setText(log.getObservations());
        //mBottomRelativeLayout.addView(txt);
/*        if(log.getResult() != null) {
            if (log.getResult().equals("Correcta")) {
                resultTextView.setTextColor(Color.GREEN);
            } else if (log.getResult().equals("Incorrecta")) {
                resultTextView.setTextColor(Color.RED);
            } else if (log.getResult().equals("Emergente")) {
                resultTextView.setTextColor(Color.YELLOW);
            } else {
                resultTextView.setText("Sin evaluar");
                resultTextView.setTextColor(Color.LTGRAY);
            }
        }*/
        resultTextView.setText(log.getResult());
    }
}
