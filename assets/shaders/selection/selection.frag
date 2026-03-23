#version 330 core

const float borderSize = 1.0 / 100.0;
const vec3 selectionColor = vec3(0.3);

in vec3 pos;
out vec4 FragColor;

void main()
{
    vec3 absPos = abs(pos * 2.0 - 1.0);
    vec3 borderThreshold = vec3(1.0 - borderSize * 2.0);
    bvec3 cond1 = greaterThanEqual(absPos, borderThreshold);
    bvec3 cond2 = lessThanEqual(absPos, vec3(0.9999));
    bool draw = any(bvec3(cond1.x && cond2.x, cond1.y && cond2.y, cond1.z && cond2.z));
    if (!draw) { discard; }
    FragColor = vec4(selectionColor, 1.0);
} 
