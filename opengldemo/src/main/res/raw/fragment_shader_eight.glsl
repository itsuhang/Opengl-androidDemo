#version 300 es
precision mediump float;  //lowp, mediump, highp精度,顶点着色器有默认精度(highp),而片段着色器没有,所以必须指定一个精度
out vec4 color;
struct Material{
    sampler2D diffuse;
    sampler2D specular;
    float shininess;
};


struct Light {
    vec3 position;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

in vec2 TexCoords;

uniform Material material;
uniform Light light;
uniform vec3 viewPos;

in vec3 Normal;
in vec3 FragPos;


void main()
{
    //环境光照
    //此处的向量相乘,不是叉乘也不是点乘,就是按位相乘(Ax * Bx,Ay * By,Az * Bz)
    vec3 amb = light.ambient*vec3(texture(material.diffuse,TexCoords));

    //漫反射
    vec3 nor = normalize(Normal);
    vec3 lightDir = normalize(light.position-FragPos);
    float diff = max(dot(nor,lightDir),0.0f);
    vec3 diffuse = diff*light.diffuse*vec3(texture(material.diffuse,TexCoords));

    vec3 viewDir = normalize(viewPos-FragPos);
    vec3 reflectDir = reflect(-lightDir,nor);
    float spec = pow(max(dot(viewDir,reflectDir),0.0),material.shininess);
    vec3 specular =vec3(texture(material.specular,TexCoords))*spec*light.specular;
    vec3 result = diffuse+amb+specular;
    color = vec4(result, 1.0f);
}