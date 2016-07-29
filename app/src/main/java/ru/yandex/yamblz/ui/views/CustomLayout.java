package ru.yandex.yamblz.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by grin3s on 28.07.16.
 */

public class CustomLayout extends ViewGroup {

    int mMaxHeight = 0;

    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Any layout manager that doesn't scroll will want this.
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View matchParentView = null;
        int count = getChildCount();

        int widthOccupied = 0;

        mMaxHeight = 0;

        int childState = 0;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.width == LayoutParams.MATCH_PARENT) {
                    // there can only be one match_parent view
                    matchParentView = child;
                }
                else {
//                // Measure the child.
//
                    measureChildWithMargins(child, widthMeasureSpec, widthOccupied, heightMeasureSpec, 0);

                    // Update our size information based on the layout params.  Children
                    // that asked to be positioned on the left or right go in those gutters.

                    widthOccupied = widthOccupied + child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                    mMaxHeight = Math.max(mMaxHeight,
                            child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                    childState = combineMeasuredStates(childState, child.getMeasuredState());
                }
            }
        }

        if (matchParentView != null) {
            final LayoutParams lp = (LayoutParams) matchParentView.getLayoutParams();
            measureChildWithMargins(matchParentView, widthMeasureSpec, widthOccupied, heightMeasureSpec, 0);

            // Update our size information based on the layout params.  Children
            // that asked to be positioned on the left or right go in those gutters.

            widthOccupied = widthOccupied + matchParentView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            mMaxHeight = Math.max(mMaxHeight,
                    matchParentView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            childState = combineMeasuredStates(childState, matchParentView.getMeasuredState());
        }


        // Check against our minimum height and width
        mMaxHeight = Math.max(mMaxHeight, getSuggestedMinimumHeight());
        widthOccupied = Math.max(widthOccupied, getSuggestedMinimumWidth());

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(widthOccupied, widthMeasureSpec, childState),
                resolveSizeAndState(mMaxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        int curLeft = getPaddingLeft();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                int child_left = curLeft + lp.leftMargin;
                int child_top = top + lp.topMargin;
                int child_right = child_left + width - lp.rightMargin;
                int child_bottom = child_top + height - lp.bottomMargin;

                child.layout(child_left, child_top, child_right, child_bottom);
                curLeft +=  width;
            }
        }
    }

    // ----------------------------------------------------------------------
    // The rest of the implementation is for custom per-child layout parameters.
    // If you do not need these (for example you are writing a layout manager
    // that does fixed positioning of its children), you can drop all of this.

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /**
     * Custom per-child layout information.
     */
    public static class LayoutParams extends MarginLayoutParams {
        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         */
//        public int gravity = Gravity.TOP | Gravity.START;
//
//        public static int POSITION_MIDDLE = 0;
//        public static int POSITION_LEFT = 1;
//        public static int POSITION_RIGHT = 2;
//
//        public int position = POSITION_MIDDLE;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            // Pull the layout param values from the layout XML during
            // inflation.  This is not needed if you don't care about
            // changing the layout behavior in XML.
//            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CustomLayoutLP);
//            gravity = a.getInt(R.styleable.CustomLayoutLP_android_layout_gravity, gravity);
//            position = a.getInt(R.styleable.CustomLayoutLP_layout_position, position);
//            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
