package com.phe.datetime;

import java.util.Calendar;

/**
 * Toggle between on and off state.
 * 
 * Each time the process is called it looks to see if the next switch time has been reached.
 * When this occurs, the state is toggled, and then the next switch time is calcalated as a random
 * interval between the values supplied in the contructor
 * 
 * @author PEarle
 *
 */
public class ToggleOnOff {

	boolean on = false;
	long nextSwitchTime;
	
	private int minTime;
	private int maxTime;
	private int useage;		// determine whether to calculate in seconds, minutes or hours
	
	public final static int USE_SECONDS = 0;
	public final static int USE_MINUTES = 1;
	public final static int USE_HOURS = 2;

	public ToggleOnOff(int minTime, int maxTime, int useage) {
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.useage = useage;
	}
	
	/**
	 * Look to see if its time to toggle - i.e. has the 'next switch time' been past?
	 */
	public void check() {
		if(isPastSwitchTime()) {
			toggle();
			
			setNextSwitchTime();
		}
	}
	
	private void setNextSwitchTime() {
		
		// Get interval to wait in minutes
		int nextInterval = new TimingGenerator().getNextInterval(minTime, maxTime);
		
		long millis = nextInterval * 1000;
		if(useage==USE_MINUTES) {
			millis = millis * 60;
		}
		else if(useage==USE_HOURS) {
			millis = millis * 60 * 60;
		}
		nextSwitchTime = Calendar.getInstance().getTimeInMillis() + millis;
	}
	
	/**
	 * See if we have past the 'next switch time'
	 * @return
	 */
	private boolean isPastSwitchTime() {
		long now = Calendar.getInstance().getTimeInMillis();
		return (now > nextSwitchTime);	// true if now is greater than next switch time
	}
	
	private void toggle() {
		if(on) {
			on = false;
		}
		else {
			on = true;
		}
	}
	
	public boolean isOn() {
		if(on) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean isOff() {
		if(!on) {
			return true;
		}
		else {
			return false;
		}
	}

	public String toString() {
		
		String s = (on) ? "ON" : "OFF";
		return "Current status = " + s
		+ " - time to next switch = " 
		+ ((nextSwitchTime - Calendar.getInstance().getTimeInMillis())/1000)
		+ " seconds ...";
		
		
	}
	
	/**
	 * main(0 method shows sample implementation
	 * @param args
	 */
	public static void main(String args[]) {
		
		//ToggleOnOff toggler = new ToggleOnOff(2,20, ToggleOnOff.USE_MINUTES);
		ToggleOnOff toggler = new ToggleOnOff(2,20, ToggleOnOff.USE_SECONDS);
		while(true) {
			System.out.println(toggler);
			toggler.check();
			
			if(toggler.isOn()) {
				System.out.println("ON !!!!!");
			}
			if(toggler.isOff()) {
				System.out.println("OFF !!!!");
			}
			
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
