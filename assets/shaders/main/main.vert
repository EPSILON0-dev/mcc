#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 2) in vec2 aTexCoord;
layout (location = 3) in float aBlockType;
  
uniform mat4 cameraViewProjection;
uniform mat4 modelMatrix;
out vec2 texCoord;
out float blockType;

void main()
{
    gl_Position = cameraViewProjection * modelMatrix * vec4(aPos, 1.0);
    texCoord = aTexCoord;
    blockType = aBlockType;
}
