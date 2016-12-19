package com.example;

import java.util.ArrayList;

public class MyClass {

	public static void main(String[] args) {
		int i = 5;

		Long l = new Long(5);
		ArrayList<Long> longs = new ArrayList<>();
		longs.add(l);

		boolean containsI = false;
		for(long lon : longs) {
			if(lon == i) {
				containsI = true;
				break;
			}
		}

		System.out.println(containsI);
	}

}
