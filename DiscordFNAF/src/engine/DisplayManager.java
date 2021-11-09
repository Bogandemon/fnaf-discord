/*
 * Classname: DisplayManager
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 10/11/2021
 * Description: Initialises and keeps track of display window properties, such as resizing and vsync.
 */

package engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DisplayManager {
	
	private static String title;
	private static boolean vSync;
	private static int width;
	private static int height;
	private static long window;
	private static boolean resized;
	
	public DisplayManager(String title, int width, int height, boolean vSync) {
		DisplayManager.title = title;
		DisplayManager.width = width;
		DisplayManager.height = height;
		DisplayManager.vSync = vSync;
		DisplayManager.resized = false;
	}
	
	//Initialisation function for the window.
	public static void init() {
		//Sets up the error callback.
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialise GLFW");
		}
		
		//Collection of window hints for things such as resizing and compatability for all operating systems. Needs to be done before creating.
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		glfwWindowHint(GLFW_FLOATING, GL_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			DisplayManager.width = width;
			DisplayManager.height = height;
			DisplayManager.resized = true;
		});
		
		//Sets up key callback, which is used for keyboard functionality (pressing, holding, releasing, etc).
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window, true);
			}
		});
		
		//Sets up the window in the middle of the screen according to the width and height of both the variables and the users screen.
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(
			window,
			(vidmode.width() - width) / 2,
			(vidmode.height() - height) / 2
		);
		
		glfwMakeContextCurrent(window); //Makes openGL context current.
		
		//If statement for vSync.
		if (vSync) {
			glfwSwapInterval(1);
		}
		
		
		glfwShowWindow(window); //Shows the window result.
		GL.createCapabilities(); //Important line for setting the context for GLFW and openGL. Allows for openGL bindings to be used.
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f); //Clears colour buffer.
		glEnable(GL_DEPTH_TEST); //Important line for enabling the depth order and depth testing.
		
	}
	
	//Sets the appropriate clear colour.
	public void setClearColor(float r, float g, float b, float alpha) {
		glClearColor(r, g, b, alpha);
	}
	
	//Method for checking if a key is pressed and what key.
	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(window, keyCode) == GLFW_PRESS;
	}
	
	//Function for closing the window.
	public boolean windowShouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	//Updates the window appropriately and polls for events
	public void update() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	//Checks if vSync is active.
	public boolean isVsync() {
		return vSync;
	}
	
	//Checks if window has been resized.
	public boolean isResized() {
		return resized;
	}
	
	//Changes boolean value for if the window is resized or not.
	public void setResized(boolean newResized) {
		resized = newResized;
	}
	
	//Obtains the width value of the screen.
	public int getWidth() {
		return width;
	}
	
	//Obtains the height value of the screen.
	public int getHeight() {
		return height;
	}
}
