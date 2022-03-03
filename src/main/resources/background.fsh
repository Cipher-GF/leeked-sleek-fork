//precision highp float;
//uniform vec2      resolution;
//uniform float     time;
//uniform vec2      speed;
//uniform float     cd;
//uniform float     cb;
//uniform float     sr;
//uniform float     sg;
//uniform float     sb;
//uniform float     shift;
//uniform sampler2D 	uSampler;
//varying vec2 		vTextureCoord;
//
//float rand(vec2 n) {
//    return fract(cos(dot(n, vec2(2.9898, 20.1414))) * 5.5453);
//}
//
//float noise(vec2 n) {
//    const vec2 d = vec2(0.0, 1.0);
//    vec2 b = floor(n), f = smoothstep(vec2(.1), vec2(1.0), fract(n));
//    return mix(mix(rand(b), rand(b + d.yx), f.x), mix(rand(b + d.xy), rand(b + d.yy), f.x), f.y);
//}
//
//float fbm(vec2 n){
//    float total=0.,amplitude=.8;
//    for(int i=0;i<20;i++){
//        total+=noise(n)*amplitude;
//        n+=n;
//        amplitude*=0.40;
//    }
//    return total;
//}
//
//
//void main(void){
//    const vec3 c1=vec3(0.0, 0.0, 0.0);
//    const vec3 c2=vec3(0./255.,0./255.,150./255.);
//    const vec3 c3=vec3(0.4902, 0., 0.4902);
//    const vec3 c4=vec3(0.0, 0.0, 0.0);
//    const vec3 c5=vec3(0.3176, 0., 0.4);
//    const vec3 c6=vec3(.0, 0.3569, 0.2);
//
//    vec2 p=gl_FragCoord.xy*10./resolution.xx;
//    float q=fbm(p-time*.5);
//    vec2 r=vec2(fbm(p+q+time*speed.x-p.x-p.y),fbm(p+q-time*speed.y));
//    vec3 c=mix(c1,c2,fbm(p+r))+mix(c3,c4,r.x)-mix(c5,c6,r.y);
//    float grad=gl_FragCoord.y/resolution.y;
//    gl_FragColor=vec4(c*cos(shift*gl_FragCoord.y/resolution.y),0.5);
//    gl_FragColor.xyz*=1.;
//}
#ifdef GL_ES
precision mediump float;
#endif
//jms
#extension GL_OES_standard_derivatives : enable

#define NUM_OCTAVES 4

uniform float time;
uniform vec2 resolution;

mat3 rotX(float a) {
    float c = cos(a);
    float s = sin(a);
    return mat3(
    1, 0, 0,
    0, c, -s,
    0, s, c
    );
}
mat3 rotY(float a) {
    float c = cos(a);
    float s = sin(a);
    return mat3(
    c, 0, -s,
    0, 1, 0,
    s, 0, c
    );
}

float random(vec2 pos) {
    return fract(1.0 * sin(pos.y + fract(100.0 * sin(pos.x)))); // http://www.matteo-basei.it/noise
}

float noise(vec2 pos) {
    vec2 i = floor(pos);
    vec2 f = fract(pos);
    float a = random(i + vec2(0.0, 0.0));
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

float fbm(vec2 pos) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100.0);
    mat2 rot = mat2(cos(0.15), sin(0.15), -sin(0.25), cos(0.5));
    for (int i=0; i<NUM_OCTAVES; i++) {
        v += a * noise(pos);
        pos = rot * pos * 2.0 + shift;
        a *= 0.55;
    }
    return v;
}

void main(void) {
    vec2 p = (gl_FragCoord.xy * 2.0 - resolution.xy) / min(resolution.x, resolution.y);

    float t = 0.0, d;

    float time2 = 3.0 * time / 2.0;

    vec2 q = vec2(0.0);
    q.x = fbm(p + 0.00 * time2);
    q.y = fbm(p + vec2(1.0));
    vec2 r = vec2(0.0);
    r.x = fbm(p + 1.0 * q + vec2(1.7, 9.2) + 0.15 * time2);
    r.y = fbm(p + 1.0 * q + vec2(8.3, 2.8) + 0.126 * time2);
    float f = fbm(p + r);
    vec3 color = mix(
    vec3(0.9, 0.3, 0.3),
    vec3(.9, 0.3, 0.3),
    clamp((f * f) * 4.0, 0.0, 1.0)
    );

    color = mix(
    color,
    vec3(0., 0.3, 0.93),
    clamp(length(q), 0.0, 1.0)
    );


    color = mix(
    color,
    vec3(0.2, 0.01, 0.4),
    clamp(length(r.x), 0.0, 1.0)
    );

    color = (f *f * f + 0.8 * f * f + 0.8 * f) * color;

    gl_FragColor = vec4(color, 1.0);
}



