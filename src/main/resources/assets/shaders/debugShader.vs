#version 330 core

layout (location=0) in vec3 aPosition;
layout (location=1) in vec3 aColor;

uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

out vec3 fragColor;

void main() {
	fragColor = aColor;
	gl_Position = uProjectionMatrix * uViewMatrix * vec4(aPosition, 1.0);
}