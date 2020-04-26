#version 330 core

in vec2 position;
in vec3 colour;
in vec2 texcoord;

out vec3 vertexColour;
out vec2 textureCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vertexColour = colour;
    textureCoord = texcoord;
    gl_Position = projection * view * model * vec4(position, 0.0, 1.0);
}
