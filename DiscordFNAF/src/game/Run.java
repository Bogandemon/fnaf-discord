/*
 * Classname: Run
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 29/10/2021
 * Description: Main function that is used to run the game and setup screen boundaries/title.
 */

package game;

import engine.GameEngine;
import engine.GameLogic;

public class Run {
	
	//Default width and height values for the screen render.
	static final String title  = "Discord FNAF";
	static int displayWidth = 800;
	static int displayHeight = 600;
	static boolean vSync = true;
	
	public static void main(String[] args) {
		try {
			GameLogic gameLogic = new Test();
			GameEngine gameEngine = new GameEngine(title, displayWidth, displayHeight, vSync, gameLogic);
			gameEngine.run();
			
		} catch (Exception error) {
			System.err.println(error);
			System.exit(0);
		}
	}
}
