#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec4 color;

uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

out vec4 exColor;

void main()
{
    exColor = color;
    gl_Position = uProjectionMatrix * uViewMatrix * vec4(position, 1.0);
}
