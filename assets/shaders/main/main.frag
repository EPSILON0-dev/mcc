#version 330 core
out vec4 FragColor;
  
const int atlasSize = 4;

in vec2 texCoord;
in float blockType;

uniform sampler2D atlasTexture;

void main()
{
    int blockTypeI = int(floor(blockType + 0.5));
    ivec2 atlasPos = ivec2(blockTypeI % atlasSize, blockTypeI / atlasSize);
    vec2 finalTexCoord = vec2(atlasPos) / vec2(float(atlasSize)) + texCoord / vec2(float(atlasSize));
    FragColor = texture(atlasTexture, finalTexCoord);
} 
