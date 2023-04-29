#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_time;
uniform vec4 u_color1;
uniform vec4 u_color2;

varying vec4 v_color;
varying vec2 v_texCoord;


void main() {

    vec4 noise1 = texture2D(u_texture, vec2(v_texCoord.x + u_time * .5, v_texCoord.y + u_time));
    vec4 noise2= texture2D(u_texture, vec2(v_texCoord.x * .9 - u_time * .5, v_texCoord.y*.8 + u_time * .6));
    float noise = (noise1.r*.9 + noise2.r*.9 + noise1.g*.2 + noise2.g*.2) * smoothstep(.1, 1., v_texCoord.y);
    float edgeFade = smoothstep(.01, .2, v_texCoord.x) * smoothstep(.99, .8, v_texCoord.x);

    vec4 color = mix(u_color1, u_color2, smoothstep(.5, 1.6, noise));

    color.a *= clamp(0., 1., noise) * edgeFade * noise;
    gl_FragColor = color;
}