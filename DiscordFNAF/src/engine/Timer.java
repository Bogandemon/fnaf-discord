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
	
	public void init() {
		lastLoopTime = getTime();
	}
	
	public double getTime() {
		return System.nanoTime() / 1_000_000_000.0;
	}
	
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
