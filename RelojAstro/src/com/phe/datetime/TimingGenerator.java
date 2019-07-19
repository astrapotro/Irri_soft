package com.phe.datetime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class TimingGenerator {

	/**
	 * Get a time interval being a random of number minutes between the 2 supplied values
	 * @return
	 */
	public int getNextInterval(int from, int to) {
		
		/*
		 * If from >= to, its an error, so set something just in case
		 */
		if(to < from) {
			from = to + 30; 
		}
		
		// Build a list
		ArrayList<Integer> al = new ArrayList<Integer>();
		for(int i = from; i < to; i ++) {
			al.add(new Integer(i));
		}
		
		// Select 1 at random
		Random generator = new Random( Calendar.getInstance().getTimeInMillis() );
		int index = generator.nextInt(al.size());
		
		return al.get(index).intValue();
	}
	
	public static void main(String args[]) {
		
		for(int i = 0; i < 100; i++) {
			System.out.println(new TimingGenerator().getNextInterval(10, 120));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
