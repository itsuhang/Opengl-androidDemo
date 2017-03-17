#version 300 es
layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 texCoords;
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec2 TexCoords;
out vec3 Normal;
out vec3 FragPos;

void main()
{
    gl_Position =projection* view * model * vec4(position, 1.0f);
    FragPos = vec3(model * vec4(position, 1.0f));
    //transpose(inverse(model))生成正规矩阵,防止不成比例缩放造成的法向量不垂直与表面, 并转换成3x3矩阵,避免位移对结果产生影响
    Normal = mat3(transpose(inverse(model)))*normal;
    TexCoords = texCoords;
}