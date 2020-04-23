#version 330 core

in vec3 vertexColour;
in vec2 textureCoord;

out vec4 fragColour;

uniform sampler2D tex;

void main() {
	vec4 textureColour = texture(tex, textureCoord);
    fragColour = vec4(vertexColour, 1.0) * textureColour;
}
