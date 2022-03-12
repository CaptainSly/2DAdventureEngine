#version 330 core

in vec4 exColor;
in vec2 exUVCoords;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 fragColor;

void main()
{

	if (fTexId > 0) {

		int texId = int(fTexId);
		fragColor = exColor * texture(uTextures[texId], exUVCoords);
	
	} else {
		fragColor = exColor;
	}
}
