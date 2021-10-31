/*
 * Classname: Timer
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 29/10/2021
 * Description: Utility class used for calculating elapsed time between frames.
 */

package engine;

public class Timer {
	private double lastLoopTime;
	
	//Initialises the time.
	public void init() {
		lastLoopTime = getTime();
	}
	
	//Gets the current time of the timeslice.
	public double getTime() {
		return System.nanoTime() / 1_000_000_000.0;
	}
	
	//Gets the amount of time that has passed in a given time slice.
	public float getElapsedTime() {
		double time = getTime();
		float elapsedTime = (float) (time - lastLoopTime);
		lastLoopTime = time;
		return elapsedTime;
	}
	
	public double getLastLoopTime() {
		return lastLoopTime;
	}
}
