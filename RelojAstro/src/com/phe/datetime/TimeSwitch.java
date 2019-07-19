package com.phe.datetime;

import java.io.IOException;

import com.pi4j.device.piface.PiFace;
import com.pi4j.device.piface.impl.PiFaceDevice;
import com.pi4j.gpio.extension.piface.PiFaceGpioProvider;
import com.pi4j.wiringpi.Spi;

/**
 * Function as a time switch - turn a light on and off at random intervals
 * @author Peter Earle
 *
 */
public class TimeSwitch {

	int pin;  // THe pin to be turned off and on
	ToggleOnOff toggler;
	
	final PiFaceGpioProvider pifaceprovide = new PiFaceGpioProvider(PiFace.DEFAULT_ADDRESS, Spi.CHANNEL_0);
	final PiFace piface = new PiFaceDevice(PiFace.DEFAULT_ADDRESS, Spi.CHANNEL_0);

	public TimeSwitch(int pin) throws InterruptedException, IOException{
		this.pin = pin;
		
		toggler = new ToggleOnOff(2,20, ToggleOnOff.USE_SECONDS);
				
	}
	
	public void process() {
		
		boolean wasOn = false; 
		
		while(true) {
			while(true) {
				//System.out.println(toggler);
				toggler.check();
				
				if(toggler.isOn()) {
					if(!wasOn) {
						piface.getLed(pin).on();
						wasOn = true;
						System.out.println(toggler);
					}
				}
				if(toggler.isOff()) {
					if(wasOn) {
						piface.getLed(pin).off();
						wasOn = false;
						System.out.println(toggler);
					}
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
	
	public static void main(String atgs[]) {
		
		try {
			new TimeSwitch(0).process();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
