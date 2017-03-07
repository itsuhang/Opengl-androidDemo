#version 310 es
layout (location = 0) in vec3 position;
layout (location = 1) in vec3 color;
layout (location = 2) in vec2 texPosition;
out vec3 fragcolor;
out vec2 outTex;
uniform mat4 transform;
void main()
{
    gl_Position = transform * vec4(position, 1.0f);
    fragcolor = color;
    outTex = vec2(texPosition.x,1.0f-texPosition.y);
}