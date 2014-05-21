/*
 * package net.towerdefender.image;
 * 
 * import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
 * 
 * public class GPUImageSuperImposeDebugFilter extends GPUImageFilter { static
 * final String SUPERIMPOSE_DEBUG_FRAGMENT_SHADER = "" +
 * "precision highp float;\n" + "\n" + "varying vec2 textureCoordinate;\n" +
 * "\n" + "uniform sampler2D inputImageTexture;\n" + "\n" + "void main()\n" +
 * "{\n" +
 * "  	lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n"
 * + "  	if (textureColor.r != 0.0){ \n" + "  		gl_FragColor = textureColor;\n"
 * + "	 	} \n" + "		else { \n" + "			gl_FragColor = vec4(0,0,0,0.5);\n" +
 * "		} \n" + "}";
 * 
 * public GPUImageSuperImposeDebugFilter() { super(NO_FILTER_VERTEX_SHADER,
 * SUPERIMPOSE_DEBUG_FRAGMENT_SHADER); } }
 */