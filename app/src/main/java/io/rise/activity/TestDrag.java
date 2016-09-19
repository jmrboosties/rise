package io.rise.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;

import io.rise.R;
import io.rise.util.Print;

public class TestDrag extends AppCompatActivity {

	private HashMap<String, View> mTagMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_drag_drop);

        initLayout();
    }

    private void initLayout() {
        LinearLayout top = (LinearLayout) findViewById(R.id.top);
        LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom);

	    top.setTag("top");
		bottom.setTag("bottom");

	    top.setOnDragListener(new MyDragListener());
	    bottom.setOnDragListener(new MyDragListener());

        ImageView iv1 = buildImageView();
        ImageView iv2 = buildImageView();

	    addTag(iv1, "iv1");
	    addTag(iv2, "iv2");

        iv1.setImageResource(R.drawable.kappa);
        iv2.setImageResource(R.drawable.pogchamp);

        top.addView(iv1);
        top.addView(iv2);
    }

	private void addTag(View view, String tag) {
		view.setTag(tag);
		mTagMap.put(tag, view);
	}

    private ImageView buildImageView() {
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics());
        int margins = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        ImageView iv = new ImageView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(margins, margins, margins, margins);

        iv.setLayoutParams(params);

        iv.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
	            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

	            ClipData dragData = new ClipData((CharSequence) v.getTag(), new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);

	            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);

	            //Deprecated in 24, leave it for us
	            //noinspection deprecation
	            v.startDrag(dragData,  // the data to be dragged
			            myShadow,  // the drag shadow builder
			            null,      // no need to use local data
			            0          // flags (not currently used, set to 0)
	            );

                return true;
            }

        });

//	    iv.setOnDragListener(new MyDragListener());

        return iv;
    }

	private class MyDragListener implements View.OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();

			View dragging = null;
			if(event.getClipDescription() != null)
				dragging = mTagMap.get(event.getClipDescription().getLabel().toString());

			if(dragging == null) {
				if(action == DragEvent.ACTION_DRAG_ENDED) {
					Print.log("drag event ended");
					return true;
				}
			}
			else {
				switch(action) {
					case DragEvent.ACTION_DRAG_STARTED:
						Print.log("view with tag " + v.getTag() + " ready to receive drop of " + dragging.getTag());
						break;
					case DragEvent.ACTION_DRAG_ENTERED:
						Print.log("view with tag " + dragging.getTag() + " entered " +  v.getTag());
						break;
					case DragEvent.ACTION_DRAG_EXITED:
						Print.log("view with tag " + dragging.getTag() + " exited " +  v.getTag());
						break;
					case DragEvent.ACTION_DROP:
						Print.log("view with tag " + dragging.getTag() + " dropped in " +  v.getTag());

						//Place
						ViewGroup vg = (ViewGroup) v;
						if(vg.indexOfChild(dragging) < 0) {
							//If index is less than 0 means is not present
							ViewGroup currentParent = (ViewGroup) dragging.getParent();
							currentParent.removeView(dragging);

							vg.addView(dragging);
							return false;
						}
						else {
							//Already has item, do nothing
							return false;
						}

					default:
						return false;
				}
			}

			return true;
		}

	}

	private static class RiseDragShadowBuilder extends View.DragShadowBuilder {

//		private static Drawable shadow;

		public RiseDragShadowBuilder(View v) {
			super(v);
//			shadow = new ColorDrawable(Color.LTGRAY);
		}

		@Override
		public void onProvideShadowMetrics (Point size, Point touch) {
			super.onProvideShadowMetrics(size, touch);
		}

		@Override
		public void onDrawShadow(Canvas canvas) {
			super.onDrawShadow(canvas);
			// Draws the ColorDrawable in the Canvas passed in from the system.
//			shadow.draw(canvas);
		}

	}

}
