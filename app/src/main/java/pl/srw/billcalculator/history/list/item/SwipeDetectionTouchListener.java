package pl.srw.billcalculator.history.list.item;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Kamil Seweryn.
 */
public class SwipeDetectionTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private final SwipeExecutor executor;

    public SwipeDetectionTouchListener(final Context context, final SwipeExecutor executor) {
        this.executor = executor;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY))
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0)
                        executor.onSwipeDetected(Direction.RIGHT);
                    else
                        executor.onSwipeDetected(Direction.LEFT);
                    return true;
                }
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0)
                        executor.onSwipeDetected(Direction.BOTTOM);
                    else
                        executor.onSwipeDetected(Direction.TOP);
                    return true;
                }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            executor.onTap();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            executor.onLongPress();
        }
    }

    public interface SwipeExecutor {
        void onSwipeDetected(Direction direction);
        void onTap();
        void onLongPress();
    }

    public enum Direction {
        LEFT, RIGHT, BOTTOM, TOP
    }
}
