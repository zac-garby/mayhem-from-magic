package uk.co.zacgarby.mhm;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class App {
	private long window;
	private float angle;
	private int shader;
	private int texture;

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

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
		System.out.println(System.getProperty("user.dir"));
		
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
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

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
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
		
		try (MemoryStack stack = stackPush()) {
			FloatBuffer verts = stack.mallocFloat(4 * 7);
			verts.put(10f).put(10f).put(1f).put(1f).put(1f).put(0f).put(0f);
			verts.put(50f).put(10f).put(1f).put(1f).put(1f).put(1f).put(0f);
			verts.put(50f).put(50f).put(1f).put(1f).put(1f).put(1f).put(1f);
			verts.put(10f).put(50f).put(1f).put(1f).put(1f).put(0f).put(1f);
			verts.flip();
			
			IntBuffer elems = stack.mallocInt(2 * 3);
			elems.put(0).put(1).put(2);
			elems.put(2).put(3).put(0);
			elems.flip();
			
			int vbo = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
			
			int ebo = glGenBuffers();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, elems, GL_STATIC_DRAW);
		}
		
		String fragSource = null, vertSource = null;
		
		try {
			fragSource = readFile("shader.frag", StandardCharsets.UTF_8);
			vertSource = readFile("shader.vert", StandardCharsets.UTF_8);
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
		
		int shader = glCreateProgram();
		glAttachShader(shader, vertShader);
		glAttachShader(shader, fragShader);
		glBindFragDataLocation(shader, 0, "fragColor");
		glLinkProgram(shader);
		if (glGetProgrami(shader, GL_LINK_STATUS) != GL_TRUE) {
			throw new RuntimeException(glGetProgramInfoLog(shader));
		}
		
		glUseProgram(shader);
		
		int floatSize = 4;
		
		int posAttrib = glGetAttribLocation(shader, "position");
		glEnableVertexAttribArray(posAttrib);
		glVertexAttribPointer(posAttrib, 2, GL_FLOAT, false, 7 * floatSize, 0);
		
		int colAttrib = glGetAttribLocation(shader, "color");
		glEnableVertexAttribArray(colAttrib);
		glVertexAttribPointer(colAttrib, 3, GL_FLOAT, false, 7 * floatSize, 2 * floatSize);
		
		int texAttrib = glGetAttribLocation(shader, "texcoord");
		glEnableVertexAttribArray(texAttrib);
		glVertexAttribPointer(texAttrib, 2, GL_FLOAT, false, 7 * floatSize, 5 * floatSize);
		
		int uniModel = glGetUniformLocation(shader, "model");
		Matrix4f model = new Matrix4f();
		glUniformMatrix4fv(uniModel, false, model.get(new float[16]));
		
		int uniView = glGetUniformLocation(shader, "view");
		Matrix4f view = new Matrix4f();
		glUniformMatrix4fv(uniView, false, view.get(new float[16]));
		
		int uniProj = glGetUniformLocation(shader, "projection");
		Matrix4f proj = new Matrix4f().ortho(0f, 250f, 0f, 200f, -1f, 1f);
		glUniformMatrix4fv(uniProj, false, proj.get(new float[16]));
		
		int uniTex = glGetUniformLocation(shader, "tex");
		glUniform1i(uniTex, 0);
		
		texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		try (MemoryStack stack = stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			
			stbi_set_flip_vertically_on_load(true);
			ByteBuffer image = stbi_load("sun.png", w, h, comp, 4);
			if (image == null) {
				throw new RuntimeException("Failed to load a texture file!\n" + stbi_failure_reason());
			}
						
			int width = w.get();
			int height = h.get();
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		}
	}

	private void loop() {
		GL.createCapabilities();

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT);

			render();
			
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}
	
	private void render() {
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	}
	
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void main(String[] args) {
		new App().run();
	}

}