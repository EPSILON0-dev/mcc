#version 330 core
layout (location = 0) in vec3 aPos;
  
uniform mat4 cameraViewProjection;
uniform mat4 modelMatrix;
out vec3 pos;

void main()
{
    gl_Position = cameraViewProjection * modelMatrix * vec4(aPos, 1.0);
    pos = aPos;
}
