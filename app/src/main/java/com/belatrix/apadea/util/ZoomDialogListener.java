package com.belatrix.apadea.util;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.belatrix.apadea.R;

public class ZoomDialogListener implements View.OnClickListener{

    private View mContent;
    private TextView label;
    private ImageView image;
    private Button mSelectButton;

    Dialog zoomDialog;

    private OptionSelectedListener mOptionSelectedListener = null;

    public ZoomDialogListener(ImageView v, TextView textView) {
        this.image = v;
        this.label = textView;
    }

    public ZoomDialogListener(ImageView v, TextView textView, OptionSelectedListener optionSelectedListener) {
        this.image = v;
        this.label = textView;
        this.mOptionSelectedListener = optionSelectedListener;
    }

    @Override
    public void onClick(View v) {
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        zoomDialog = new Dialog(v.getContext());
        zoomDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mContent = inflater.inflate(R.layout.item_zoom, null);
        zoomDialog.setContentView(mContent);

        ImageView imageView = (ImageView) zoomDialog.findViewById(R.id.imageView);
        imageView.setImageDrawable(image.getDrawable());

        TextView textView = (TextView) zoomDialog.findViewById(R.id.dialogTextView);
        textView.setText(label.getText().toString());

        initializeButton();

        zoomDialog.show();
    }

    private void initializeButton(){
        if (mOptionSelectedListener != null) {
            mSelectButton = (Button)mContent.findViewById(R.id.selectButton);
            mSelectButton.setVisibility(View.VISIBLE);
            mSelectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zoomDialog.cancel();
                    mOptionSelectedListener.onOptionSelected();
                }
            });
        }
    }


    public interface OptionSelectedListener {
        public void onOptionSelected();
    }


}