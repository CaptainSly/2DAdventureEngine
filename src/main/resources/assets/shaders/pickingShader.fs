#version 330 core

in vec4 outColor;
in vec2 outUVCoords;
in float outTexId;
in float outEntityId;

uniform sampler2D uTextures[8];

out vec3 fragColor;

void main() {
	
	vec4 texColor = vec4(1, 1, 1, 1);
	
	if (outTexId > 0) {
		int id = int(outTexId);
		texColor = outColor * texture(uTextures[id], outUVCoords);
	} 
	
	if (texColor.a < 0.5) discard;
	
	fragColor = vec3(outEntityId, outEntityId, outEntityId);
	
}