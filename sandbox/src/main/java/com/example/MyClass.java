package com.example;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;

public class MyClass {

	public static void main(String[] args) {
		ArrayList<String> fuck = new ArrayList<>();
		fuck.add("me");
		fuck.add("you");
		fuck.add("together");

		Observable.from(fuck).subscribe(new Action1<String>() {

			@Override
			public void call(String s) {
				System.out.println(s);
			}

		});
	}

}
