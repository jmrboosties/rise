package fitness.classmate.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import fitness.classmate.util.Print;
import rx.Observer;
import rx.subjects.PublishSubject;

public class Test extends Activity {

	private PublishSubject<Long> mLongPublishSubject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Observer<Object> observer1 = new Observer<Object>() {

			@Override
			public void onCompleted() {
				Print.log("observer 1 complete");
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Object o) {
				Print.log("observer 1: " + o);
			}

		};

//		Observer<Object> observer2 = new Observer<Object>() {
//
//			@Override
//			public void onCompleted() {
//				System.out.println("observer 2 complete");
//			}
//
//			@Override
//			public void onError(Throwable e) {
//
//			}
//
//			@Override
//			public void onNext(Object o) {
//				System.out.println("observer 2: " + o);
//			}
//
//		};

		mLongPublishSubject = PublishSubject.create();
		mLongPublishSubject.subscribe(observer1);

//		PublishSubject<Object> subject = PublishSubject.create();
//		// observer1 will receive all onNext and onCompleted events
//		subject.subscribe(observer1);
//		subject.onNext("one");
//		subject.onNext("two");
//		// observer2 will only receive "three" and onCompleted
//		subject.subscribe(observer2);
//		subject.onNext("three");
//		subject.onCompleted();

		new Async().execute();
	}

	private class Async extends AsyncTask<Void, Long, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			for(int i = 0; i < 10; i++) {
				try {
					Thread.sleep(1000);
				} catch(InterruptedException e) { }

				publishProgress((long) i);
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Long... values) {
			mLongPublishSubject.onNext(values[0]);
		}

	}


}
