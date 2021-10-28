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
	void init() throws Exception;
	void input(DisplayManager displayWindow);
	void update(float interval);
	void render(DisplayManager displayWindow);
}
