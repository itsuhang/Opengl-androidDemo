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
    vec3 direction;
    float cutoff;
    float outcutoff;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};

in vec2 TexCoords;

uniform Material material;
uniform Light light;
uniform vec3 viewPos;

in vec3 Normal;
in vec3 FragPos;


void main()
{
    vec3 lightDir = normalize(light.position-FragPos);
    float theta = dot(lightDir,normalize(-light.direction));
    float epsilon = light.cutoff-light.outcutoff;
    //clamp(x,min,max);若x在min与max之间则返回x,若小与min则返回min,若大于max则返回max
    float intensity = clamp((theta-light.outcutoff)/epsilon,0.0f,1.0f);

    float distance = length(light.position-FragPos);
    float attenuation = 1.0f/(light.constant+light.linear*distance+light.quadratic*(distance*distance));
    //环境光照
    //此处的向量相乘,不是叉乘也不是点乘,就是按位相乘(Ax * Bx,Ay * By,Az * Bz)
    vec3 amb = light.ambient*vec3(texture(material.diffuse,TexCoords));
    //漫反射
    vec3 nor = normalize(Normal);
    float diff = max(dot(nor,lightDir),0.0f);
    vec3 diffuse = diff*light.diffuse*vec3(texture(material.diffuse,TexCoords));
    vec3 viewDir = normalize(viewPos-FragPos);
    vec3 reflectDir = reflect(-lightDir,nor);
    float spec = pow(max(dot(viewDir,reflectDir),0.0),material.shininess);
    vec3 specular =vec3(texture(material.specular,TexCoords))*spec*light.specular;
    diffuse*= attenuation;
    specular*= attenuation;
    diffuse*=intensity;
    specular*=intensity;
    vec3 result = diffuse+amb+specular;
    color = vec4(result, 1.0f);
}