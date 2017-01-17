package com.example;

import rx.Observer;
import rx.subjects.PublishSubject;

public class MyClass {

	public static void main(String[] args) {
		Observer<Object> observer1 = new Observer<Object>() {

			@Override
			public void onCompleted() {
				System.out.println("observer 1 complete");
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Object o) {
				System.out.println("observer 1: " + o);
			}

		};

		Observer<Object> observer2 = new Observer<Object>() {

			@Override
			public void onCompleted() {
				System.out.println("observer 2 complete");
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Object o) {
				System.out.println("observer 2: " + o);
			}

		};

		PublishSubject<Object> subject = PublishSubject.create();
		// observer1 will receive all onNext and onCompleted events
		subject.subscribe(observer1);
		subject.onNext("one");
		subject.onNext("two");
		// observer2 will only receive "three" and onCompleted
		subject.subscribe(observer2);
		subject.onNext("three");
		subject.onCompleted();
	}

}
