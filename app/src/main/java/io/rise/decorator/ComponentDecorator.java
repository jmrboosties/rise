package io.rise.decorator;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class ComponentDecorator extends RecyclerView.ItemDecoration {

	private int mSpacing;

	public ComponentDecorator(Context context) {
		mSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		super.getItemOffsets(outRect, view, parent, state);

		outRect.left = mSpacing;
		outRect.right = mSpacing;
		outRect.top = mSpacing;
		outRect.bottom = mSpacing;
	}

	public int getSpacing() {
		return mSpacing;
	}

}
