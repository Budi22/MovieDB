package ramzy.task.utils;

import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewMargin extends RecyclerView.ItemDecoration {

    private final int mColumns;
    private int mMargin;

    public RecyclerViewMargin(@IntRange(from = 0) final int margin,
                              @IntRange(from = 0) final int columns) {
        mMargin = margin;
        mColumns = columns;
    }

    @Override
    public void getItemOffsets(final Rect outRect,
                               final View view,
                               final RecyclerView parent,
                               final RecyclerView.State state) {
        final int position = parent.getChildLayoutPosition(view);
        outRect.right = mMargin;
        outRect.bottom = mMargin;
        if (position < mColumns) {
            outRect.top = mMargin;
        }
        if (position % mColumns == 0) {
            outRect.left = mMargin;
        }
    }
}