#version 330 core

in vec2 position;
in vec3 color;
in vec2 texcoord;

out vec3 vertexColor;
out vec2 textureCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vertexColor = color;
    textureCoord = texcoord;
    gl_Position = projection * view * model * vec4(position, 0.0, 1.0);
}
