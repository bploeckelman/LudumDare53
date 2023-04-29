#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

varying vec4 v_color;
varying vec2 v_texCoord;

void main() {

    float margin = (v_texCoord.y) * .4;
    vec2 mapped = vec2(smoothstep(margin, 1.-margin, v_texCoord.x), v_texCoord.y);
    vec4 color = texture2D(u_texture, mapped);

    gl_FragColor = color;

}