/*
 * Classname: GameLogic
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 29/10/2021
 * Description: Interface used for different game logics (mainly used for different games/projects).
 */

package engine;

import render.DisplayManager;

public interface GameLogic {
	void init() throws Exception; //Initialisation function for creating any basic variables.
	void input(DisplayManager displayWindow); //Input function for any form of input (such as escape for getting out of the game, or movement).
	void update(float interval); //Update function which will be used to update graphic placements and respond to inputs.
	void render(DisplayManager displayWindow); //Render function which will render all of the information given to the screen.
}
