package com.ioavthiago.engine.rendering.shader;

import com.somegame.engine.rendering.ShaderProgram;
import com.ioavthiago.engine.util.datastructure.Matrix4f;

public class BasicTransformShader extends ShaderProgram {

	private static final BasicTransformShader instance = new BasicTransformShader();
	private String vertexShader;
	private String fragmentShader;
	
	public BasicTransformShader() {
		super();
		vertexShader = 	"#version 120\n"
					  + "uniform mat4 transform;\n"
					  + "varying vec4 vertexColor;\n"
					  + "\n"
					  + "void main() {\n"
					  + "	gl_TexCoord[0].st = gl_MultiTexCoord0.xy;\n"
					  + "	vertexColor = gl_Color;\n"
					  + "	float rotation = gl_Normal.x;\n"
					  + "	vec2 anchor = gl_Normal.yz; \n"
					  + "	vec2 scaling = gl_MultiTexCoord0.zw;\n"
					  + "	\n"
					  + "	mat2 rotationMatrix = mat2(\n"
					  + "		cos(rotation),  sin(rotation),\n"
					  + "	   -sin(rotation),  cos(rotation) \n"
					  + "	);\n"
					  + "	\n"
					  + "	mat2 scalingMatrix = mat2(\n"
					  + "		scaling.x,  0,\n"
					  + "	    0,  scaling.y \n"
					  + "	);\n"
					  + "	\n"
					  + "	vec2 preTransformed = rotationMatrix * scalingMatrix * (gl_Vertex.xy - anchor);\n"
					  + "	\n"
					  + "	gl_Position = transform * vec4((preTransformed + anchor), 1.0, 1.0);\n"
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
	
	public static BasicTransformShader getInstance() {
		return instance;
	}
	
	public void updateUniforms(Matrix4f projectedMatrix) {
		setUniform("transform", projectedMatrix);
	}
	
}
