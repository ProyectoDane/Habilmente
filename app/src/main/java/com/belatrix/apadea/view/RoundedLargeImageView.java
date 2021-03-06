package com.belatrix.apadea.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedLargeImageView extends ImageView {

    Path clipPath = new Path();

    RectF rectF = new RectF();

    public RoundedLargeImageView(Context context) {
        super(context);
    }

    public RoundedLargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedLargeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float radius = 36.0f; // angle of round corners

        //RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());

        rectF.set(0, 0, this.getWidth(), this.getHeight());

        clipPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);

        try {
            canvas.clipPath(clipPath);
        } catch(UnsupportedOperationException e){
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDraw(canvas);
    }
}
