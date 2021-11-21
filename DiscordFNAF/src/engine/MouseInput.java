/*
 * Classname: MouseInput
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 22/11/2021
 * Description: MouseInput class that handles how the game reacts to actual input from the mouse in any capacity.
 */

package engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
	private final Vector2d previousPosition; //Vector variable that keeps track of the previous mouse position.
	private final Vector2d currentPosition; //Vector variable that keeps track of the current position the mouse ends up being in.
	private final Vector2f displayVec; //Vector used to actually display the movement and positioning to the display window.
	private boolean inWindow = false; //Boolean that checks if the mouse cursor is within the display window. If it is, then take the inputs.
	private boolean leftButton = false; //Boolean variable for if the left mouse button is pressed or not.
	private boolean rightButton = false; //Boolean variable for if the right mouse button is pressed or not.
	
	public MouseInput() {
		previousPosition = new Vector2d(-1, -1);
		currentPosition = new Vector2d(0, 0);
		displayVec = new Vector2f();
	}
	
	//Method used to initialise the actual mouse input class.
	public void init(DisplayManager displayWindow) {
		
		//Callback function used for when the cursor actually moves.
		glfwSetCursorPosCallback(displayWindow.getHandle(), (windowHandle, xpos, ypos) -> {
			currentPosition.x = xpos;
			currentPosition.y = ypos;
		});
		
		//Callback function used to check if the mouse is present in the display window or not.
		glfwSetCursorEnterCallback(displayWindow.getHandle(), (windowHandle, entered) -> {
			inWindow = entered;
		});
		
		//Callback function used to check if a button is pressed on the mouse.
		glfwSetMouseButtonCallback(displayWindow.getHandle(), (windowHandle, button, action, mode) -> {
			leftButton = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
			rightButton = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
		});
	}
	
	//Method used to get any input from the mouse.
	public void input(DisplayManager displayWindow) {
		displayVec.x = 0;
		displayVec.y = 0;
		
		//If statement that changes the display vector variables if they have in fact been changed (keeps it updated).
		if (previousPosition.x > 0 && previousPosition.y > 0 && inWindow) {
			double deltaX = currentPosition.x - previousPosition.x;
			double deltaY = currentPosition.y - previousPosition.y;
			boolean rotateX = deltaX != 0;
			boolean rotateY = deltaY != 0;
			
			//If statements for rotation is needed.
			if (rotateX) {
				displayVec.y = (float) deltaX;
			}
			
			if (rotateY) {
				displayVec.x = (float) deltaY;
			}
		}
		
		//After taking input, updates the previous position variables to be the current position.
		previousPosition.x = currentPosition.x;
		previousPosition.y = currentPosition.y;
	}
	
	//Getters for the display vectors and buttons being pressed.
	public Vector2f getDisplayVector() {
		return displayVec;
	}
	
	public boolean isLeftPressed() {
		return leftButton;
	}
	
	public boolean isRightPressed() {
		return rightButton;
	}
}
