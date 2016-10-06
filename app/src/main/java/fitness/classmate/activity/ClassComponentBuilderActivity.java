package fitness.classmate.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.TextView;
import fitness.classmate.R;
import fitness.classmate.adapter.ClassComponentPoolAdapter;
import fitness.classmate.adapter.ClassGraphAdapter;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.constant.Constants;
import fitness.classmate.decorator.ComponentDecorator;
import fitness.classmate.model.ClassmateClass;
import fitness.classmate.model.ClassmateClassComponent;
import fitness.classmate.model.ComponentNote;
import fitness.classmate.util.Print;
import fitness.classmate.view.ClassGraphLayoutManager;
import rx.Observable;
import rx.functions.Func2;
import rx.subjects.PublishSubject;

import java.util.ArrayList;

public class ClassComponentBuilderActivity extends BaseActivity {

	private FrameLayout mClassMask;
	private RecyclerView mClassGraph;

	private ClassGraphAdapter mClassGraphAdapter;

	private ClassGraphLayoutManager mClassLayoutManager;
	private ComponentDecorator mComponentDecorator;

	private int mCurrentClassDx;

	private ClassmateClass mClassmateClass;

	@Override
	protected void prepareActivity() {
		mClassmateClass = getIntent().getParcelableExtra(Constants.CLASSMATE_CLASS);
	}

	@Override
	protected void initLayout() {
		final RecyclerView componentPool = (RecyclerView) findViewById(R.id.accb_components);

		RecyclerView.LayoutManager componentManager = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
		componentPool.setLayoutManager(componentManager);

		mComponentDecorator = new ComponentDecorator(this);

		componentPool.addItemDecoration(mComponentDecorator);

		mClassMask = (FrameLayout) findViewById(R.id.accb_class_mask);
		mClassMask.setVisibility(View.GONE);

		mClassGraph = (RecyclerView) findViewById(R.id.accb_class);

		mClassLayoutManager = new ClassGraphLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		mClassGraph.setLayoutManager(mClassLayoutManager);
		mClassGraph.addItemDecoration(mComponentDecorator);

		ClassComponentPoolAdapter classComponentAdapter = new ClassComponentPoolAdapter(this);

		ArrayList<String> components = new ArrayList<>();
		components.add("Raine");
		components.add("Finn");
		components.add("Apple");
		components.add("Carrot");
		components.add("Raine");
		components.add("Finn");
		components.add("Apple");
		components.add("Carrot");
		components.add("Raine");
		components.add("Finn");

		final PublishSubject<Integer> widthCalcSubject = PublishSubject.create();

		classComponentAdapter.setItems(components);
		classComponentAdapter.setWidthCalculatedCallback(new ClassComponentPoolAdapter.OnComponentWidthCalculatedCallback() {

			@Override
			public void onComponentWidthCalculated(int width) {
				widthCalcSubject.onNext(width);
			}

		});

		componentPool.setAdapter(classComponentAdapter);

		//Block can calculate width of component
//		{
//			final int verticalPadding = componentPool.getPaddingTop() + componentPool.getPaddingBottom();
//			final int spacing = mComponentDecorator.getSpacing();
//
//			componentPool.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//				@Override
//				public void onGlobalLayout() {
//					int poolHeight = componentPool.getHeight();
//					int minusPaddingAndSpacing = poolHeight - verticalPadding - (spacing * 4);
//
//					Print.log("final value", minusPaddingAndSpacing);
//
//					componentPool.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//				}
//
//			});
//		}

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
			}

		});

		setupDragListener();
	}

	private void createClassGraphAdapter(int width, int height) {
		mClassGraphAdapter = new ClassGraphAdapter(this, mComponentDecorator.getSpacing(), width, height);
		mClassGraphAdapter.setOnComponentDraggingCallback(new ClassGraphAdapter.OnComponentDraggingCallback() {

			@Override
			public void onComponentDragging(boolean dragging) {
				((ClassGraphLayoutManager) mClassGraph.getLayoutManager()).setScrollingDisabled(dragging);
			}

		});

		mClassGraph.setAdapter(mClassGraphAdapter);

		if(mClassmateClass != null) {
			mClassGraphAdapter.setItems(mClassmateClass.getComponents());
		}

		ArrayList<String> componentStrings = new ArrayList<>();
		componentStrings.add("Apple");
		componentStrings.add("Carrot");
		componentStrings.add("Dog");
		componentStrings.add("EightCha");
		componentStrings.add("Rats");
		componentStrings.add("Apple");
		componentStrings.add("Carrot");
		componentStrings.add("Dog");
		componentStrings.add("EightCha");
		componentStrings.add("Rats");
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

//		int placeholderPosition = mClassGraphAdapter.getIndexOfPlaceholder();
//		if(placeholderPosition > mClassLayoutManager.findLastCompletelyVisibleItemPosition()) {
//			Print.log("scrolling to the placeholder");
//			mClassLayoutManager.smoothScrollToPosition(mClassGraph, null, placeholderPosition);
//		}
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

		float f = s.length() / 8f;
		int intensity = Math.round(f * 4);

		classmateClassComponent.setIntensity(intensity);

		for(int i = 0; i < intensity; i++) {
			ComponentNote note = new ComponentNote();
			note.setMessage("Note #" + i + 1);

			classmateClassComponent.addComponentNote(note);
		}

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

	@Override
	protected int getMenuResId() {
		return R.menu.class_component_builder;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.ccb_next)
			buildClassAndGoNext();

		return super.onOptionsItemSelected(item);
	}

	private void buildClassAndGoNext() {
		ArrayList<ClassmateClassComponent> components = mClassGraphAdapter.buildComponents();

		if(mClassmateClass == null) {
			mClassmateClass = new ClassmateClass();
		}

		mClassmateClass.setComponents(components);

		//Go to music
		Intent intent = new Intent(this, SpotifyPlaylistActivity.class);
		intent.putExtra(Constants.CLASSMATE_CLASS, mClassmateClass);

		startActivity(intent);
	}

}
