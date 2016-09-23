package fitness.classmate.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fitness.classmate.adapter.ClassComponentPoolAdapter;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.decorator.ComponentDecorator;
import fitness.classmate.util.Print;
import fitness.classmate.R;

public class ClassComponentBuilderActivity extends BaseActivity {

	private FrameLayout mClassMask;

	private ComponentDecorator mComponentDecorator;

	@Override
	protected void prepareActivity() {

	}

	@Override
	protected void initLayout() {
		RecyclerView componentPool = (RecyclerView) findViewById(R.id.accb_components);

		RecyclerView.LayoutManager componentManager = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
		componentPool.setLayoutManager(componentManager);

		mComponentDecorator = new ComponentDecorator(this);

		componentPool.addItemDecoration(mComponentDecorator);

		mClassMask = (FrameLayout) findViewById(R.id.accb_class_mask);
		mClassMask.setVisibility(View.GONE);

		RecyclerView classChart = (RecyclerView) findViewById(R.id.accb_class);

		RecyclerView.LayoutManager classManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		classChart.setLayoutManager(classManager);

		ClassComponentPoolAdapter classComponentAdapter = new ClassComponentPoolAdapter(this);

		ArrayList<String> components = new ArrayList<>();
		components.add("Sprint");
		components.add("Climb");
		components.add("Jumps");
		components.add("Sprint");
		components.add("Climb");
		components.add("Jumps");
		components.add("Sprint");
		components.add("Climb");
		components.add("Jumps");
		components.add("Sprint");
		components.add("Climb");
		components.add("Jumps");
		components.add("Sprint");
		components.add("Climb");
		components.add("Jumps");
		components.add("Sprint");
		components.add("Climb");
		components.add("Jumps");
		components.add("Sprint");
		components.add("Climb");
		components.add("Jumps");

		classComponentAdapter.setItems(components);
		classComponentAdapter.setWidthCalculatedCallback(new ClassComponentPoolAdapter.OnComponentWidthCalculatedCallback() {

			@Override
			public void onComponentWidthCalculated(int width) {

			}

		});

		componentPool.setAdapter(classComponentAdapter);

		setupDragListener(classChart);
	}

	private void setupDragListener(ViewGroup viewGroup) {
		viewGroup.setOnDragListener(new View.OnDragListener() {

			@Override
			public boolean onDrag(View v, DragEvent event) {
				int action = event.getAction();

				View dragging = (View) event.getLocalState();

				if(dragging == null) {
					if(action == DragEvent.ACTION_DRAG_ENDED) {
						Print.log("drag event ended");
						return true;
					}
					else
						Print.log("drag view could not be found!");
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
							return dropView(dragging, event);
						default:
							return false;
					}
				}

				return true;
			}

		});
	}

	private boolean dropView(final View droppedView, DragEvent dragEvent) {
		//Place
		if(mClassMask.indexOfChild(droppedView) < 0) {
			//Enable the mask
			mClassMask.setVisibility(View.VISIBLE);

			//If index is less than 0 means is not present. Need to clone the droppedView since it's a copy action, not removal
			//TODO this will need to be done a bit more intelligently
			final TextView cloned = (TextView) LayoutInflater.from(this).inflate(R.layout.item_class_component, mClassMask, false);
			cloned.setText(((TextView) droppedView).getText());

			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(droppedView.getWidth(), droppedView.getHeight());
			cloned.setLayoutParams(params);

			//Get destination before committing view
			Point destination = getDestinationInParent(mClassMask);

			mClassMask.addView(cloned);

			//Use dropped view w/h here since it's already fully inflated
			cloned.setTranslationX(dragEvent.getX() - (droppedView.getWidth() / 2) - mClassMask.getPaddingLeft());
			cloned.setTranslationY(dragEvent.getY() - (droppedView.getHeight() / 2) - mClassMask.getPaddingTop());

			cloned.setAlpha(.75f);

			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.playTogether(
					ObjectAnimator.ofFloat(cloned, "translationX", cloned.getTranslationX(), destination.x),
					ObjectAnimator.ofFloat(cloned, "translationY", cloned.getTranslationY(), destination.y)
			);

			double distanceToTravel = Math.hypot(cloned.getTranslationX(), cloned.getRotationY());
			animatorSet.setDuration((long) (distanceToTravel));

			animatorSet.addListener(new AnimatorListenerAdapter() {

				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					cloned.setAlpha(1f);
//					mClassMask.setVisibility(View.GONE);
//					mClassMask.removeAllViews();
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
			point.x = (int) (lastChild.getX() + lastChild.getRight() + mComponentDecorator.getSpacing());
			point.y = lastChild.getTop() - parent.getPaddingTop();
		}
		else {
			point.x = 0;
			point.y = 0;
		}

		return point;
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_class_component_builder;
	}

}
