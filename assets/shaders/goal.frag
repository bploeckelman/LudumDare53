#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_time;

varying vec4 v_color;
varying vec2 v_texCoord;

const float stepSize = .1;


float cubicPulse( float c, float w, float x )
{
    x = abs(x - c);
    if( x>w ) return 0.0;
    x /= w;
    return 1.0 - x*x*(3.0-2.0*x);
}


void main() {
    vec2 dist = v_texCoord - .5;
//    float squares = mod((1. - dot(dist, dist)) + u_time, .05);
    float squares = mod(max(abs(dist.x), abs( dist.y)) - u_time, .05);

    float fade = 1. - 3.9 * dot(dist, dist);

    vec4 backgroundColor = v_color * .7;
    vec4 circleColor = mix(vec4(0), v_color * .5, cubicPulse(.025, .0025, squares));
    vec4 finalColor = circleColor + backgroundColor;
    finalColor.a *= fade;

    gl_FragColor = finalColor;

}
