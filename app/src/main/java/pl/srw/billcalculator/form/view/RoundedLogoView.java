package pl.srw.billcalculator.form.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import butterknife.BindColor;
import butterknife.ButterKnife;
import pl.srw.billcalculator.R;

public class RoundedLogoView extends AppCompatImageView {

    public static final float FITTING_SCALE = 0.85f;
    @BindColor(R.color.light) int grayLighter;
    @BindColor(R.color.light2) int grayDarker;

    private Paint bgPaint;
    private RectF drawingSpace;
    private int left;
    private int top;
    private int right;
    private int bottom;

    public RoundedLogoView(Context context) {
        super(context);
        init();
    }

    public RoundedLogoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedLogoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(grayLighter);
        drawingSpace = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getWidth() == 0 || getHeight() == 0) return;

        final int width = getWidth() - getPaddingLeft() - getPaddingRight();
        final int height = getHeight() - getPaddingTop() - getPaddingBottom();

        final int diameter = Math.min(width, height);
        final int cx = getPaddingLeft() + width / 2;
        final int cy = getPaddingTop() + height / 2;
        final int radius = diameter / 2;
        top = cy - radius;
        left = cx - radius;
        right = cx + radius;
        bottom = cy + radius;

        drawBackground(canvas);
        drawLogo(canvas);
    }

    private void drawBackground(Canvas canvas) {
        drawingSpace.set(left, top, right, bottom);
        bgPaint.setColor(grayLighter);
        canvas.drawArc(drawingSpace, 90f, 180f, false, bgPaint);
        bgPaint.setColor(grayDarker);
        canvas.drawArc(drawingSpace, 270f, 180f, false, bgPaint);
    }

    private void drawLogo(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) return;

        Bitmap bitmap =  ((BitmapDrawable)drawable).getBitmap();
//        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap().copy(Bitmap.Config.ARGB_8888, true);

        final float widthFactor = (float) right * FITTING_SCALE / bitmap.getWidth();
        final float heightFactor = (float) bottom * FITTING_SCALE / bitmap.getHeight();
        final float factor = Math.min(widthFactor, heightFactor);

        final float scaledWidth = bitmap.getWidth() * factor;
        final float scaledHeight = bitmap.getHeight() * factor;

        final float paddingTop = (bottom - scaledHeight) / 2;
        final float paddingLeft = (right - scaledWidth) / 2;
        drawingSpace.set(left + paddingLeft, top + paddingTop, scaledWidth + paddingLeft, scaledHeight + paddingTop);
        canvas.drawBitmap(bitmap, null, drawingSpace, bgPaint);
    }

}
