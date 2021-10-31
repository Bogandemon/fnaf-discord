package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import engine.GameLogic;
import render.DisplayManager;
import render.MasterRenderer;

public class Test implements GameLogic {
	
	private float color = 0.0f;
	private final MasterRenderer renderer;
	private int direction = 0;
	
	public Test() {
		renderer = new MasterRenderer();
	}
	
	@Override
	public void init() throws Exception {
		renderer.init();
	}
	
	@Override
	public void input(DisplayManager displayWindow) {
		if (displayWindow.isKeyPressed(GLFW_KEY_UP)) {
			direction = 1;
		}
		
		else if (displayWindow.isKeyPressed(GLFW_KEY_DOWN)) {
			direction = -1;
		}
		
		else {
			direction = 0;
		}
	}
	
	@Override
	public void update(float interval) {
		color += direction * 0.01f;
		if (color > 1) {
			color = 1.0f;
		}
		
		else if (color < 0) {
			color = 0.0f;
		}
	}
	
	@Override
	public void render(DisplayManager displayWindow) {
		displayWindow.setClearColor(color, color, color, 0.0f);
		renderer.render(displayWindow);
	}
	
	@Override
	public void cleanup() {
		renderer.cleanup();
	}
}
