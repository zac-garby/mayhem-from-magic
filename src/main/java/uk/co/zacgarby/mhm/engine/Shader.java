package uk.co.zacgarby.mhm.engine;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL33.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;

public class Shader {
	public static int POSITION_LOC = 1;
	public static int COLOUR_LOC = 2;
	public static int TEXCOORD_LOC = 3;
	public static int ATLAS_LOC, LIGHTMAP_LOC;
	
	private int program;
	
	public Shader(String fragPath, String vertPath) {
		String fragSrc = null, vertSrc = null;
		
		try {
			fragSrc = readFile(fragPath, StandardCharsets.UTF_8);
			vertSrc = readFile(vertPath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load shader files.");
		}
		
		int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragShader, fragSrc);
		glCompileShader(fragShader);
		if (glGetShaderi(fragShader, GL_COMPILE_STATUS) != GL_TRUE) {
			throw new RuntimeException(glGetShaderInfoLog(fragShader));
		}
		
		int vertShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertShader, vertSrc);
		glCompileShader(vertShader);
		if (glGetShaderi(vertShader, GL_COMPILE_STATUS) != GL_TRUE) {
			throw new RuntimeException(glGetShaderInfoLog(vertShader));
		}
		
		program = glCreateProgram();
		glAttachShader(program, fragShader);
		glAttachShader(program, vertShader);
		glBindFragDataLocation(program, 0, "fragColour");
		glBindAttribLocation(program, 1, "position");
		glBindAttribLocation(program, 2, "colour");
		glBindAttribLocation(program, 3, "texcoord");
		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
			throw new RuntimeException(glGetProgramInfoLog(program));
		}
		
		Shader.ATLAS_LOC = glGetUniformLocation(program, "atlas");
		Shader.LIGHTMAP_LOC = glGetUniformLocation(program, "lightmap");
		
		use();
		
		int uniModel = glGetUniformLocation(program, "model");
		Matrix4f model = new Matrix4f();
		glUniformMatrix4fv(uniModel, false, model.get(new float[16]));
		
		int uniView = glGetUniformLocation(program, "view");
		Matrix4f view = new Matrix4f();
		glUniformMatrix4fv(uniView, false, view.get(new float[16]));
		
		int uniProj = glGetUniformLocation(program, "projection");
		Matrix4f proj = new Matrix4f().ortho(0f, 250f, 0f, 200f, -1f, 1f);
		glUniformMatrix4fv(uniProj, false, proj.get(new float[16]));
	}
	
	public void use() {
		glUseProgram(program);
	}
	
	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
