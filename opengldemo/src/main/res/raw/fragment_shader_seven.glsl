#version 300 es
precision mediump float;  //lowp, mediump, highp精度,顶点着色器有默认精度(highp),而片段着色器没有,所以必须指定一个精度
out vec4 color;
struct Material{
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

struct Light {
    vec3 position;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform Material material;
uniform Light light;
uniform vec3 viewPos;

in vec3 Normal;
in vec3 FragPos;


void main()
{
    //环境光照
    vec3 amb = material.ambient*light.ambient;

    //漫反射
    vec3 nor = normalize(Normal);
    vec3 lightDir = normalize(light.position-FragPos);
    float diff = max(dot(nor,lightDir),0.0f);
    vec3 diffuse = (diff*material.diffuse)*light.diffuse;

    float specularStrength = 0.5f;
    vec3 viewDir = normalize(viewPos-FragPos);
    vec3 reflectDir = reflect(-lightDir,nor);
    float spec = pow(max(dot(viewDir,reflectDir),0.0),material.shininess);
    vec3 specular =(material.specular*spec)*light.specular;
    vec3 result = diffuse+amb+specular;
    color = vec4(result, 1.0f);
}