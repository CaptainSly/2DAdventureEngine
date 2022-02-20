#version 330

in vec3 exColor;
in vec2 outCoords;

uniform sampler2D TEX_SAMPLER;

out vec4 fragColor;

void main()
{
    fragColor = texture(TEX_SAMPLER, outCoords);
}
