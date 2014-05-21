/*
 * package net.towerdefender.image;
 * 
 * import jp.co.cyberagent.android.gpuimage.GPUImageFilter; import
 * android.opengl.GLES20;
 * 
 * public class GPUImageThresholdFilter extends GPUImageFilter { public static
 * final String THRESHOLD_DETECTION = "" + "precision mediump float;\n" + "\n" +
 * "varying vec2 textureCoordinate;\n" + "\n" +
 * "uniform sampler2D inputImageTexture;\n" + "uniform lowp float threshold;\n"
 * + "\n" + "void main()\n" + "{\n" +
 * "    float intensity = texture2D(inputImageTexture, textureCoordinate).r;\n"
 * + "    float mag = step(threshold, intensity);\n" + "\n" +
 * "    gl_FragColor = vec4(vec3(mag), 1.0);\n" + "}\n";
 * 
 * private int mUniformThresholdLocation; private float mThreshold = 0.2f;
 * 
 * public GPUImageThresholdFilter() { this(0.2f); }
 * 
 * public GPUImageThresholdFilter(float threshold) {
 * super(NO_FILTER_VERTEX_SHADER, THRESHOLD_DETECTION); mThreshold = threshold;
 * }
 * 
 * @Override public void onInit() { super.onInit(); mUniformThresholdLocation =
 * GLES20.glGetUniformLocation(getProgram(), "threshold"); }
 * 
 * @Override public void onInitialized() { super.onInitialized();
 * setThreshold(mThreshold); }
 * 
 * public void setThreshold(final float threshold) { mThreshold = threshold;
 * setFloat(mUniformThresholdLocation, threshold); } }
 */