package engine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
	private final Vector2d previousPosition;
	private final Vector2d currentPosition;
	private final Vector2f displayVec;
	private boolean inWindow = false;
	private boolean leftButton = false;
	private boolean rightButton = false;
	
	public MouseInput() {
		previousPosition = new Vector2d(-1, -1);
		currentPosition = new Vector2d(0, 0);
		displayVec = new Vector2f();
	}
	
	public void init(DisplayManager displayWindow) {
		glfwSetCursorPosCallback(displayWindow.getHandle(), (windowHandle, xpos, ypos) -> {
			currentPosition.x = xpos;
			currentPosition.y = ypos;
		});
		
		glfwSetCursorEnterCallback(displayWindow.getHandle(), (windowHandle, entered) -> {
			inWindow = entered;
		});
		
		glfwSetMouseButtonCallback(displayWindow.getHandle(), (windowHandle, button, action, mode) -> {
			leftButton = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
			rightButton = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
		});
	}
	
	public void input(DisplayManager displayWindow) {
		displayVec.x = 0;
		displayVec.y = 0;
		
		if (previousPosition.x > 0 && previousPosition.y > 0 && inWindow) {
			double deltaX = currentPosition.x - previousPosition.x;
			double deltaY = currentPosition.y - previousPosition.y;
			boolean rotateX = deltaX != 0;
			boolean rotateY = deltaY != 0;
			
			if (rotateX) {
				displayVec.y = (float) deltaX;
			}
			
			if (rotateY) {
				displayVec.x = (float) deltaY;
			}
		}
		
		previousPosition.x = currentPosition.x;
		previousPosition.y = currentPosition.y;
	}
	
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
