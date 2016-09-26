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
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fitness.classmate.adapter.ClassComponentPoolAdapter;
import fitness.classmate.adapter.ClassGraphAdapter;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.decorator.ComponentDecorator;
import fitness.classmate.model.ClassmateClassComponent;
import fitness.classmate.util.Print;
import fitness.classmate.R;
import rx.Observable;
import rx.functions.Func2;
import rx.subjects.PublishSubject;

public class ClassComponentBuilderActivity extends BaseActivity {

	private FrameLayout mClassMask;
	private RecyclerView mClassGraph;

	private ClassGraphAdapter mClassGraphAdapter;

	private ComponentDecorator mComponentDecorator;

	private int mCurrentClassDx;

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

		mClassGraph = (RecyclerView) findViewById(R.id.accb_class);

		RecyclerView.LayoutManager classManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		mClassGraph.setLayoutManager(classManager);
		mClassGraph.addItemDecoration(mComponentDecorator);

		ClassComponentPoolAdapter classComponentAdapter = new ClassComponentPoolAdapter(this);

		ArrayList<String> components = new ArrayList<>();
		components.add("Apple");
		components.add("Carrot");
		components.add("Apple");
		components.add("Carrot");
		components.add("Apple");
		components.add("Carrot");
		components.add("Apple");
		components.add("Carrot");
		components.add("Apple");
		components.add("Carrot");
//		components.add("Raine");
//		components.add("Finn");
//		components.add("Apple");
//		components.add("Carrot");
//		components.add("Raine");
//		components.add("Finn");
//		components.add("Apple");
//		components.add("Carrot");
//		components.add("Raine");
//		components.add("Finn");

		final PublishSubject<Integer> widthCalcSubject = PublishSubject.create();

		classComponentAdapter.setItems(components);
		classComponentAdapter.setWidthCalculatedCallback(new ClassComponentPoolAdapter.OnComponentWidthCalculatedCallback() {

			@Override
			public void onComponentWidthCalculated(int width) {
				widthCalcSubject.onNext(width);
			}

		});

		componentPool.setAdapter(classComponentAdapter);

		final PublishSubject<Integer> heightCalcSubject = PublishSubject.create();

		mClassGraph.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				heightCalcSubject.onNext(mClassGraph.getHeight());
				mClassGraph.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}

		});

		Observable.zip(widthCalcSubject, heightCalcSubject, new Func2<Integer, Integer, Void>() {

			@Override
			public Void call(Integer width, Integer height) {
				createClassGraphAdapter(width, height);

				widthCalcSubject.onCompleted();
				heightCalcSubject.onCompleted();
				return null;
			}

		}).subscribe();

		//noinspection deprecation
		mClassGraph.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				mCurrentClassDx += dx;
				mCurrentClassDx = Math.max(0, mCurrentClassDx);
				mCurrentClassDx = Math.min(mCurrentClassDx, mClassGraphAdapter.getEstimatedFullWidth());

				Print.log("current class dx", mCurrentClassDx);
			}

		});

		setupDragListener();
	}

	private void createClassGraphAdapter(int width, int height) {
		mClassGraphAdapter = new ClassGraphAdapter(this, mComponentDecorator.getSpacing(), width, height);

		mClassGraph.setAdapter(mClassGraphAdapter);

		ArrayList<String> componentStrings = new ArrayList<>();
		componentStrings.add("Apple");
		componentStrings.add("Carrot");
		componentStrings.add("Dog");
		componentStrings.add("EightCha");
		componentStrings.add("Rats");

		ArrayList<ClassmateClassComponent> components = new ArrayList<>();
		for(String s : componentStrings)
			components.add(buildComponentFromString(s));

		mClassGraphAdapter.setItems(components);
	}

	private void setupDragListener() {
		mClassGraph.setOnDragListener(new View.OnDragListener() {

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
							mClassGraphAdapter.onDragEnd();
							break;
						case DragEvent.ACTION_DROP:
							Print.log("view with tag " + dragging.getTag() + " dropped in " +  v.getTag());
							return dropView(dragging, event);
						case DragEvent.ACTION_DRAG_LOCATION:
							handleDragLocation(event);
							break;
						default:
							return false;
					}
				}

				return true;
			}

		});
	}

	private void handleDragLocation(DragEvent event) {
		int positionInFullGraph = (int) (event.getX() + mCurrentClassDx);
		mClassGraphAdapter.handleDragEnter(positionInFullGraph);
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
			Point destination = getDestinationInParent(droppedView.getHeight());

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
					mClassMask.setVisibility(View.GONE);
					mClassMask.removeAllViews();

					mClassGraphAdapter.addItem(buildComponentFromString(cloned.getText().toString()));
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

	//TODO remove this
	private ClassmateClassComponent buildComponentFromString(String s) {
		ClassmateClassComponent classmateClassComponent = new ClassmateClassComponent();
		classmateClassComponent.setName(s);

		float intensity = s.length() / 8f;
		classmateClassComponent.setIntensity(intensity);

		return classmateClassComponent;
	}

	private Point getDestinationInParent(int draggingShadowHeight) {
		Point point = new Point();

		point.x = mClassGraphAdapter.getPositionOfPlaceholder(mCurrentClassDx);
		point.y = mClassGraphAdapter.getGraphSectionHeight() - draggingShadowHeight - mComponentDecorator.getSpacing();

		Print.log("calculated position", point);

		return point;
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_class_component_builder;
	}

}
