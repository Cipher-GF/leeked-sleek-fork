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
#extension GL_OES_standard_derivatives : enable

precision mediump float;

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

void main( void ) {

    vec2 p = ( gl_FragCoord.xy / resolution.xy );
    // Image
    float aspect_ratio = 16.0 / 9.0;
    // Camera
    float viewport_height = 2.0;
    float viewport_width = aspect_ratio * viewport_height;
    float focal_length = 1.0;

    vec3 origin = vec3(0, 0, 0);
    vec3 horizontal = vec3(viewport_width, 0, 0);
    vec3 vertical = vec3(1, viewport_height, 0);
    vec3  col=vec3(1);
    col  = vec3(p.x+0.25,0,0);
    gl_FragColor = vec4( col*vec3(p.y)+vec3(0.3, 0.7, 1.0), 0.5 );

}




