#version 330 core

layout (location=0) in vec3 aPosition;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aUVCoords;
layout (location=3) in float aTexId;

uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

out vec4 exColor;
out vec2 exUVCoords;
out float fTexId;

void main()
{
    exUVCoords = aUVCoords;
    fTexId = aTexId;
    exColor = aColor;
    gl_Position = uProjectionMatrix * uViewMatrix * vec4(aPosition, 1.0);
}
