#version 330 core

in vec3 vertexColor;
in vec2 textureCoord;

out vec4 fragColor;

uniform sampler2D tex;

void main() {
	vec4 textureColor = texture(tex, textureCoord);
    fragColor = vec4(vertexColor, 1.0) * textureColor;
}
