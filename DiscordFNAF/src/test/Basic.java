/*
 * Classname: Basic
 * Programmer: Kyle Dryden
 * Version: Java 14 (JDK and JRE), LWJGL 3.2.3
 * Date: 28/10/2021
 */

package test;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Basic {
	
	private static final int WIDTH = 800; //Width of the display window, currently final.
	private static final int HEIGHT = 600; //Height of the display window, currently final.
	
	private long window; //Long window variable.
	
	public void run() {
		
		try {
			init();
			loop();
			
			//Releases callbacks and destroys window/context.
			glfwFreeCallbacks(window);
			glfwDestroyWindow(window);
		}
		
		finally {
			
			//Terminates window and releases error callback.
			glfwTerminate();
			glfwSetErrorCallback(null).free();
		}
	}
	
	//Initialisation function for the window.
	private void init() {
		//Sets up the error callback.
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialise GLFW");
		}
		
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		
		window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
		
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
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
			(vidmode.width() - WIDTH) / 2,
			(vidmode.height() - HEIGHT) / 2
		);
		
		glfwMakeContextCurrent(window); //Makes openGL context current.
		glfwSwapInterval(1); //V-sync.
		
		glfwShowWindow(window);
	}
	
	//Main game loop function. Currently used for creating window with colour and polling for events.
	private void loop() {
		//Important line for setting the context for GLFW and openGL. Allows for openGL bindings to be used.
		GL.createCapabilities();
		
		glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
		
		//Main while loop.
		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}
	
	public static void main(String[] args) {
		new Basic().run();
	}
}
