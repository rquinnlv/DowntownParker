package com.zappos.downtown.parker.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * This class was copied from https://groups.google.com/forum/#!topic/android-developers/ksS0cBFmxxE
 */
public class FlowLayout extends ViewGroup {
    private int[] rowHeights;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int maxInternalWidth = MeasureSpec.getSize(widthMeasureSpec) - getHorizontalPadding();
        int maxInternalHeight = MeasureSpec.getSize(heightMeasureSpec) - getVerticalPadding();
        List<RowMeasurement> rows = new ArrayList<RowMeasurement>();
        RowMeasurement currentRow = null;
        for (View child : getLayoutChildren()) {
            LayoutParams childLayoutParams = (LayoutParams) child.getLayoutParams();
            int childWidthSpec = createChildMeasureSpec(childLayoutParams.width, maxInternalWidth, widthMode);
            int childHeightSpec = createChildMeasureSpec(childLayoutParams.height, maxInternalHeight, heightMode);
            child.measure(childWidthSpec, childHeightSpec);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if ((currentRow == null) || currentRow.isWouldExceedMax(childWidth)) {
                currentRow = new RowMeasurement(maxInternalWidth, widthMode);
                rows.add(currentRow);
            }
            currentRow.addChildDimensions(childWidth, childHeight);
        }
        int longestRowWidth = 0;
        int totalRowHeight = 0;
        int rowCount = rows.size();
        this.rowHeights = new int[rowCount];
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            RowMeasurement row = rows.get(rowIndex);
            int rowHeight = row.getHeight();
            this.rowHeights[rowIndex] = rowHeight;
            totalRowHeight = totalRowHeight + rowHeight;
            longestRowWidth = Math.max(longestRowWidth, row.getWidth());
        }
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? MeasureSpec.getSize(widthMeasureSpec) : (longestRowWidth + getHorizontalPadding()), (heightMode == MeasureSpec.EXACTLY) ? MeasureSpec.getSize(heightMeasureSpec) : (totalRowHeight + getVerticalPadding()));
    }

    private int createChildMeasureSpec(int childLayoutParam, int max, int parentMode) {
        int spec;
        if (childLayoutParam == ViewGroup.LayoutParams.FILL_PARENT)
            spec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
        else if (childLayoutParam == ViewGroup.LayoutParams.WRAP_CONTENT)
            spec = MeasureSpec.makeMeasureSpec(max, ((parentMode == MeasureSpec.UNSPECIFIED) ? MeasureSpec.UNSPECIFIED : MeasureSpec.AT_MOST));
        else
            spec = MeasureSpec.makeMeasureSpec(childLayoutParam, MeasureSpec.EXACTLY);
        return spec;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected void onLayout(boolean changed, int leftPosition, int topPosition, int rightPosition, int bottomPosition) {
        int widthOffset = getMeasuredWidth() - getPaddingRight();
        int x = getPaddingLeft();
        int y = getPaddingTop();
        int rowIndex = 0;
        for (View child : getLayoutChildren()) {
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if ((x + childWidth) > widthOffset) {
                x = getPaddingLeft();
                y = y + this.rowHeights[rowIndex];
                rowIndex = rowIndex + 1;
            }
            int _y;
            if (layoutParams.centerVertical)
                _y = y + ((this.rowHeights[rowIndex] - childHeight) / 2);
            else
                _y = y;
            child.layout(x, _y, x + childWidth, _y + childHeight);
            x = x + childWidth;
        }
    }

    private Collection<View> getLayoutChildren() {
        int count = getChildCount();
        Collection<View> children = new ArrayList<View>(count);
        for (int index = 0; index < count; index++) {
            View child = getChildAt(index);
            if (child.getVisibility() != View.GONE)
                children.add(child);
        }
        return children;
    }

    private int getVerticalPadding() {
        return getPaddingTop() + getPaddingBottom();
    }

    private int getHorizontalPadding() {
        return getPaddingLeft() + getPaddingRight();
    }

    public static class LayoutParams extends MarginLayoutParams {
        boolean centerVertical;

        public LayoutParams() {
            this(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            setCenterVertical(false);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray attributes = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.layout_centerVertical});
            setCenterVertical(attributes.getBoolean(0, false));
            attributes.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            this(source.width, source.height);
        }

        public void setCenterVertical(boolean centerVertical) {
            this.centerVertical = centerVertical;
        }
    }

    private static class RowMeasurement {
        private int maxWidth;
        private int widthMode;
        private int width;
        private int height;

        RowMeasurement(int maxWidth, int widthMode) {
            this.maxWidth = maxWidth;
            this.widthMode = widthMode;
        }

        int getWidth() {
            return this.width;
        }

        int getHeight() {
            return this.height;
        }

        boolean isWouldExceedMax(int childWidth) {
            return ((this.widthMode != MeasureSpec.UNSPECIFIED) && (getNewWidth(childWidth) > this.maxWidth));
        }

        void addChildDimensions(int childWidth, int childHeight) {
            this.width = getNewWidth(childWidth);
            this.height = Math.max(this.height, childHeight);
        }

        private int getNewWidth(int childWidth) {
            return ((this.width == 0) ? childWidth : (this.width + childWidth));
        }
    }
}
