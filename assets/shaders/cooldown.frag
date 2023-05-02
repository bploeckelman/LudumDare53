#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_percent;

varying vec4 v_color;
varying vec2 v_texCoord;

void main() {


    vec4 textColor = texture2D(u_texture, vec2(v_texCoord.x, v_texCoord.y));
    vec4 colorLeft = vec4(v_color.rgb ,1.);
    vec4 colorRight = vec4(.2,0.2,0.2,1.);
    float scaledPercent = u_percent/100.;
    float s = smoothstep(scaledPercent - 0.005, scaledPercent + 0.005, v_texCoord.x);
    vec4 finalColor = mix(colorLeft, colorRight, s);
    gl_FragColor = vec4(finalColor.rgb,textColor.a);

}
