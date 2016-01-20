package com.belatrix.apadea.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.belatrix.apadea.R;

public class ZoomDialogListener implements View.OnClickListener{

    private TextView label;
    private ImageView image;

    public ZoomDialogListener(ImageView v, TextView textView) {
        this.image = v;
        this.label = textView;
    }

    @Override
    public void onClick(View v) {
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Dialog zoomDialog = new Dialog(v.getContext());
        zoomDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        zoomDialog.setContentView(inflater.inflate(R.layout.item_zoom, null));

        ImageView imageView = (ImageView) zoomDialog.findViewById(R.id.imageView);
        imageView.setImageDrawable(image.getDrawable());

        TextView textView = (TextView) zoomDialog.findViewById(R.id.dialogTextView);
        textView.setText(label.getText().toString());

        zoomDialog.show();
    }
}