/*
 * Classname: GameLogic
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 16/11/2021
 * Description: Interface used for different game logics (mainly used for different games/projects).
 */

package interfaces;

import engine.DisplayManager;
import engine.MouseInput;

public interface GameLogic {
	void init(DisplayManager displayWindow) throws Exception; //Initialisation function for creating any basic variables.
	void input(DisplayManager displayWindow, MouseInput mouseInput); //Input function for any form of input (such as escape for getting out of the game, or movement).
	void update(float interval, MouseInput mouseInput); //Update function which will be used to update graphic placements and respond to inputs.
	void render(DisplayManager displayWindow); //Render function which will render all of the information given to the screen.
	void cleanup(); //Cleanup function that will cleanup the gamelogic and rendering when everything is finished (the game is closed).
}
