/*
 * Classname: Run
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 29/10/2021
 * Description: Main function that is used to run the game and setup screen boundaries/title.
 */

package game;

import engine.GameEngine;
import interfaces.GameLogic;

public class Run {
	
	//Default values for the game engine currently being used.
	static final String title  = "Discord FNAF";
	static int displayWidth = 800;
	static int displayHeight = 600;
	static boolean vSync = true;
	
	public static void main(String[] args) {
		try {
			GameLogic gameLogic = new MainLogic(); //Gamelogic. Swapped for every different 'game'.
			GameEngine gameEngine = new GameEngine(title, displayWidth, displayHeight, vSync, gameLogic);
			gameEngine.run();
			
		} catch (Exception error) {
			System.err.println(error);
			System.exit(0);
		}
	}
}
