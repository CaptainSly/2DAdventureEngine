#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 color;
layout (location=2) in vec2 uvCoords;

uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

out vec3 exColor;
out vec2 outCoords;

void main()
{
    gl_Position = uProjectionMatrix * uViewMatrix * vec4(position, 1.0);
    exColor = color;
    outCoords = uvCoords;
}
