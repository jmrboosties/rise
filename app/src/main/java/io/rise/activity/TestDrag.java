package io.rise.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
	    FrameLayout top = (FrameLayout) findViewById(R.id.top);
	    FrameLayout bottom = (FrameLayout) findViewById(R.id.bottom);

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
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 72, getResources().getDisplayMetrics());
        int margins = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        ImageView iv = new ImageView(this);

	    iv.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));

	    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
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

						return dropView((ViewGroup) v, dragging, event);
					case DragEvent.ACTION_DRAG_LOCATION :
//						Print.log("location xy", event.getX(), event.getY());
						break;
					default:
						return false;
				}
			}

			return true;
		}

	}

	private boolean dropView(ViewGroup parent, final View droppedView, DragEvent dragEvent) {
		//Place
		if(parent.indexOfChild(droppedView) < 0) {
			//If index is less than 0 means is not present
			ViewGroup currentParent = (ViewGroup) droppedView.getParent();
			currentParent.removeView(droppedView);

			//Get destination before committing view
			Point destination = getDestinationInParent(parent);

			parent.addView(droppedView);

			droppedView.setTranslationX(dragEvent.getX() - (droppedView.getWidth() / 2) - parent.getPaddingLeft());
			droppedView.setTranslationY(dragEvent.getY() - (droppedView.getHeight() / 2) - parent.getPaddingTop());

			droppedView.setAlpha(.75f);

			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.playTogether(
					ObjectAnimator.ofFloat(droppedView, "translationX", droppedView.getTranslationX(), destination.x),
					ObjectAnimator.ofFloat(droppedView, "translationY", droppedView.getTranslationY(), destination.y)
			);

			double distanceToTravel = Math.hypot(droppedView.getTranslationX(), droppedView.getRotationY());
			animatorSet.setDuration((long) (distanceToTravel));

			animatorSet.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					droppedView.setAlpha(1f);
				}

			});

			animatorSet.start();
			return true;
		}
		else {
			//Already has item, do nothing
			return false;
		}
	}

	private Point getDestinationInParent(ViewGroup parent) {
		Point point = new Point();

		if(parent.getChildCount() > 0) {
			View lastChild = parent.getChildAt(parent.getChildCount() - 1);
			point.x = lastChild.getRight() + ((FrameLayout.LayoutParams) lastChild.getLayoutParams()).rightMargin;
			point.y = lastChild.getTop() - ((FrameLayout.LayoutParams) lastChild.getLayoutParams()).topMargin - parent.getPaddingTop();
		}
		else {
			point.x = 0;
			point.y = 0;
		}

		return point;
	}

}
