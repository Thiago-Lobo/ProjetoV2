package com.somegame.engine.rendering.shader;

import com.somegame.engine.rendering.ShaderProgram;
import com.somegame.engine.util.datastructure.Matrix4f;

public class BasicShader extends ShaderProgram {

	private static final BasicShader instance = new BasicShader();
	private String vertexShader;
	private String fragmentShader;
	
	public BasicShader() {
		super();
		vertexShader = 	"#version 120\n"
					  + "uniform mat4 transform;\n"
					  + "varying vec4 vertexColor;\n"
					  + "void main() {\n"
					  + "	gl_TexCoord[0].st = gl_MultiTexCoord0.st;\n"
					  + "	vertexColor = gl_Color;\n"
					  + "	vec4 vert = vec4(gl_Vertex.xy, 0.0, 1.0);\n"
					  + "	gl_Position = transform * gl_Vertex;\n"
					  + "}\n";
		fragmentShader = 	"#version 120\n"
						  + "varying vec4 vertexColor;\n"
						  + "uniform sampler2D sampler;\n"
						  + "void main() {\n"
						  + "	vec4 texSample = texture2D(sampler, gl_TexCoord[0].st);\n"
						  + "	gl_FragColor = texSample * vertexColor;\n"
						  + "}\n";
		addVertexShader(vertexShader);
		addFragmentShader(fragmentShader);
		compileShader();
		addUniform("transform");
	}
	
	public static BasicShader getInstance() {
		return instance;
	}
	
	public void updateUniforms(Matrix4f projectedMatrix) {
		setUniform("transform", projectedMatrix);
	}
	
}
