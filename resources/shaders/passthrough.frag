#version 330 core

in vec3 vertexColour;
in vec2 textureCoord;

out vec4 fragColour;

uniform sampler2D atlas;
uniform sampler2D lightmap;

void main() {
	vec4 a = texture(lightmap, vec2(0, 0));
	vec4 b = texture(atlas, vec2(0, 0));

	vec4 textureColour = texture(atlas, textureCoord);
    fragColour = vec4(vertexColour, 1.0) * textureColour;
}
