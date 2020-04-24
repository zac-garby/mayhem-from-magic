package uk.co.zacgarby.mhm.engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

public class Window {
	public static int POSITION = 1;
	public static int COLOUR = 2;
	public static int TEXCOORD = 3;
	public static int TEX;
	
	private long window;
	private Game delegate;
	public int shader;
	private double lastFrame;
	
	public Window(Game game) {
		delegate = game;
	}

	public void run() {
		init();
		GL.createCapabilities();
		setup();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		org.lwjgl.system.Configuration.STACK_SIZE.set(1024 * 4);
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		// Configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

		// Create the window
		window = glfwCreateWindow(1000, 800, "Game", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window, true);
			}
		});

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}
	
	private void setup() {
		int vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		setupShaders();
		delegate.setup();
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	private void loop() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		lastFrame = glfwGetTime();

		while (!glfwWindowShouldClose(window)) {			
			double now = glfwGetTime();
			double dt = now - lastFrame;
			lastFrame = now;
			 
			glfwPollEvents();
			delegate.update(dt);
			
			glClear(GL_COLOR_BUFFER_BIT);
			delegate.render();
			
			glfwSwapBuffers(window);
		}
	}
	
	private void setupShaders() {
		String fragSource = null, vertSource = null;
		
		try {
			fragSource = readFile("resources/shaders/shader.frag", StandardCharsets.UTF_8);
			vertSource = readFile("resources/shaders/shader.vert", StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load shader files.");
		}
				
		int vertShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertShader, vertSource);
		glCompileShader(vertShader);
		if (glGetShaderi(vertShader, GL_COMPILE_STATUS) != GL_TRUE) {
			throw new RuntimeException(glGetShaderInfoLog(vertShader));
		}
		
		int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragShader, fragSource);
		glCompileShader(fragShader);
		if (glGetShaderi(fragShader, GL_COMPILE_STATUS) != GL_TRUE) {
			throw new RuntimeException(glGetShaderInfoLog(fragShader));
		}
		
		shader = glCreateProgram();
		glAttachShader(shader, vertShader);
		glAttachShader(shader, fragShader);
		glBindFragDataLocation(shader, 0, "fragColour");
		glBindAttribLocation(shader, 1, "position");
		glBindAttribLocation(shader, 2, "colour");
		glBindAttribLocation(shader, 3, "texcoord");
		glLinkProgram(shader);
		if (glGetProgrami(shader, GL_LINK_STATUS) != GL_TRUE) {
			throw new RuntimeException(glGetProgramInfoLog(shader));
		}
		
		glUseProgram(shader);
		
		int uniModel = glGetUniformLocation(shader, "model");
		Matrix4f model = new Matrix4f();
		glUniformMatrix4fv(uniModel, false, model.get(new float[16]));
		
		int uniView = glGetUniformLocation(shader, "view");
		Matrix4f view = new Matrix4f();
		glUniformMatrix4fv(uniView, false, view.get(new float[16]));
		
		int uniProj = glGetUniformLocation(shader, "projection");
		Matrix4f proj = new Matrix4f().ortho(0f, 250f, 0f, 200f, -1f, 1f);
		glUniformMatrix4fv(uniProj, false, proj.get(new float[16]));
		
		Window.TEX = glGetUniformLocation(shader, "tex");
		glUniform1i(Window.TEX, 0);
	}
	
	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}