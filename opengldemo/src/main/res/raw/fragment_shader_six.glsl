#version 300 es
precision mediump float;  //lowp, mediump, highp精度,顶点着色器有默认精度(highp),而片段着色器没有,所以必须指定一个精度
out vec4 color;
uniform vec3 objectColor;
uniform vec3 lightColor;
uniform vec3 lightPos;
uniform vec3 viewPos;

in vec3 Normal;
in vec3 FragPos;

void main()
{
    //环境光照
    float ambient = 0.1f;
    vec3 amb = ambient*lightColor;

    //漫反射
    vec3 nor = normalize(Normal);
    vec3 lightDir = normalize(lightPos-FragPos);
    float diff = max(dot(nor,lightDir),0.0f);
    vec3 diffuse = diff*lightColor;

    //镜面光照
    float specularStrength = 0.5f;
    vec3 viewDir = normalize(viewPos-FragPos);
    vec3 reflectDir = reflect(-lightDir,nor);
    float spec = pow(max(dot(viewDir,reflectDir),0.0),128.0);
    vec3 specular =specularStrength*spec*lightColor;
    vec3 result = (diffuse+amb+specular)*objectColor;
    color = vec4(result, 1.0f);
}