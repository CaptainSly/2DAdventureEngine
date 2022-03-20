#version 330 core

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aUVCoords;
layout (location = 3) in float aTexId;
layout (location = 4) in float aEntityId;

uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

out vec4 outColor;
out vec2 outUVCoords;
out float outTexId;
out float outEntityId;

void main() {

	outColor = aColor;
	outUVCoords = aUVCoords;
	outTexId = aTexId;
	outEntityId = aEntityId;
	
	gl_Position = uProjectionMatrix * uViewMatrix * vec4(aPosition, 1.0);
}