package net.towerdefender.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import net.towerdefender.activity.GameActivity;
import net.towerdefender.image.ARObject;
import net.towerdefender.image.MarkerVisibilityListener;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

/**
 * This class implements our custom renderer. Note that the GL10 parameter
 * passed in is unused for OpenGL ES 2.0 renderers -- the static class GLES20 is
 * used instead.
 */
public class LessonOneRenderer implements MarkerVisibilityListener,
		GLSurfaceView.Renderer {
	/**
	 * Store the model matrix. This matrix is used to move models from object
	 * space (where each model can be thought of being located at the center of
	 * the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];

	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix
	 * transforms world space to eye space; it positions things relative to our
	 * eye.
	 */
	private float[] mViewMatrix = new float[16];
	private float[] mViewMatrixProjTopDown = new float[16];
	/**
	 * Store the projection matrix. This is used to project the scene onto a 2D
	 * viewport.
	 */
	private float[] mProjectionMatrixTopDown = new float[16];
	private float[] mProjectionMatrixAR = new float[16];

	/**
	 * Allocate storage for the final combined matrix. This will be passed into
	 * the shader program.
	 */
	private float[] mMVPMatrix = new float[16];

	/** Store our model data in a float buffer. */
	//private final FloatBuffer mTriangle1Vertices;
	private final FloatBuffer mTriangle2Vertices;
	private final FloatBuffer mTriangle3Vertices;

	/** This will be used to pass in the transformation matrix. */
	private int mMVMatrixHandle;
	private int mPMatrixHandle;

	/** This will be used to pass in model position information. */
	private int mPositionHandle;

	/** This will be used to pass in model color information. */
	private int mColorHandle;

	/** How many bytes per float. */
	private final int mBytesPerFloat = 4;

	/** How many elements per vertex. */
	private final int mStrideBytes = 7 * mBytesPerFloat;

	/** Offset of the position data. */
	private final int mPositionOffset = 0;

	/** Size of the position data in elements. */
	private final int mPositionDataSize = 3;

	/** Offset of the color data. */
	private final int mColorOffset = 3;

	/** Size of the color data in elements. */
	private final int mColorDataSize = 4;

	private int idRepere = 1;

	/**
	 * Initialize the model data.
	 */
	public LessonOneRenderer() {
		// Define points for equilateral triangles.

		// This triangle is red, green, and blue.
		/*final float[] triangle1VerticesData = {
				// X, Y, Z, 
				// R, G, B, A
				-0.5f, -0.25f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,

				0.5f, -0.25f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,

				0.0f, 0.559016994f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f };*/

		// This triangle is yellow, cyan, and magenta.
		final float[] triangle2VerticesData = {
				// X, Y, Z, 
				// R, G, B, A
				0f, 0f, 25f, 1.0f, 0f, 0f, 1f,

				-25f, -25f, 0f, 1.0f, 1.0f, 1.0f, 1.0f,

				-25f, 25f, 0f, 0.0f, 1.0f, 1.0f, 1.0f,

				25f, 25f, 0f, 1.0f, 0.0f, 1.0f, 1.0f,

				25f, -25f, 0f, 1.0f, 1.0f, 0.0f, 1.0f,

				-25f, -25f, 0f, 1.0f, 1.0f, 1.0f, 1.0f

		};

		// This triangle is white, gray, and black.
		final float[] triangle3VerticesData = {
				// X, Y, Z, 
				// R, G, B, A
				0f, 0f, 25f, 1.0f, 0f, 0f, 1f,

				-25f, -25f, 0f, 0.0f, 0.0f, 1.0f, 1.0f,

				-25f, 25f, 0f, 0.0f, 0.0f, 1.0f, 1.0f,

				25f, 25f, 0f, 0.0f, 0.0f, 1.0f, 1.0f,

				25f, -25f, 0f, 0.0f, 0.0f, 1.0f, 1.0f,

				-25f, -25f, 0f, 0.0f, 0.0f, 1.0f, 1.0f };

		// Initialize the buffers.
		/*mTriangle1Vertices = ByteBuffer
				.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();*/
		mTriangle2Vertices = ByteBuffer
				.allocateDirect(triangle2VerticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTriangle3Vertices = ByteBuffer
				.allocateDirect(triangle3VerticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		//mTriangle1Vertices.put(triangle1VerticesData).position(0);
		mTriangle2Vertices.put(triangle2VerticesData).position(0);
		mTriangle3Vertices.put(triangle3VerticesData).position(0);
		Matrix.setIdentityM(mProjectionMatrixAR, 0);
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		// Set the background clear color to gray.
		GLES20.glClearColor(1f, 0f, 0f, 0.5f);

		final String vertexShader = "precision mediump float; \n"
				+ "attribute vec3 a_Position; \n"
				+ "uniform mat4 u_MVMatrix; \n" + "uniform mat4 u_PMatrix; \n"
				+ "attribute vec4 a_Color; \n"

				+ "varying vec4 v_Color; \n" + "varying vec3 fPosition; \n" +

				"void main() \n" + "{ \n" + "  v_Color = a_Color;"
				+ "  vec4 pos = u_MVMatrix * vec4(a_Position, 1.0); \n"
				+ "  fPosition = pos.xyz; \n"
				+ "  gl_Position = u_PMatrix * pos; \n" + "} \n";

		final String fragmentShader = "precision mediump float; \n"
				+ "varying vec3 fPosition; \n" + "varying vec4 v_Color; " +

				"void main() \n" + "{ \n" + "  gl_FragColor = v_Color; \n"
				+ "} \n";

		// Load in the vertex shader.
		int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

		if (vertexShaderHandle != 0) {
			// Pass in the shader source.
			GLES20.glShaderSource(vertexShaderHandle, vertexShader);

			// Compile the shader.
			GLES20.glCompileShader(vertexShaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS,
					compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) {
				GLES20.glDeleteShader(vertexShaderHandle);
				vertexShaderHandle = 0;
			}
		}

		if (vertexShaderHandle == 0) {
			throw new RuntimeException("Error creating vertex shader.");
		}

		// Load in the fragment shader shader.
		int fragmentShaderHandle = GLES20
				.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		if (fragmentShaderHandle != 0) {
			// Pass in the shader source.
			GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

			// Compile the shader.
			GLES20.glCompileShader(fragmentShaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(fragmentShaderHandle,
					GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) {
				GLES20.glDeleteShader(fragmentShaderHandle);
				fragmentShaderHandle = 0;
			}
		}

		if (fragmentShaderHandle == 0) {
			throw new RuntimeException("Error creating fragment shader.");
		}

		// Create a program object and store the handle to it.
		int programHandle = GLES20.glCreateProgram();

		if (programHandle != 0) {
			// Bind the vertex shader to the program.
			GLES20.glAttachShader(programHandle, vertexShaderHandle);

			// Bind the fragment shader to the program.
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);

			// Bind attributes
			GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
			//GLES20.glBindAttribLocation(programHandle, 1, "a_Color");

			// Link the two shaders together into a program.
			GLES20.glLinkProgram(programHandle);

			// Get the link status.
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS,
					linkStatus, 0);

			// If the link failed, delete the program.
			if (linkStatus[0] == 0) {
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}

		if (programHandle == 0) {
			throw new RuntimeException("Error creating program.");
		}

		// Set program handles. These will later be used to pass in values to the program.
		mMVMatrixHandle = GLES20.glGetUniformLocation(programHandle,
				"u_MVMatrix");
		mPMatrixHandle = GLES20
				.glGetUniformLocation(programHandle, "u_PMatrix");

		mPositionHandle = GLES20.glGetAttribLocation(programHandle,
				"a_Position");
		mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

		// Tell OpenGL to use this program when rendering.
		GLES20.glUseProgram(programHandle);
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);

		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		//final float ratio = (float) width / height;
		final float left = -width / 2;
		final float right = width / 2;
		final float bottom = -height / 2;
		final float top = height / 2;
		final float near = 1.0f;
		final float far = 50.0f;
		Matrix.orthoM(mProjectionMatrixTopDown, 0, left, right, bottom, top,
				near, far);
		//Matrix.frustumM(mProjectionMatrixTopDown, 0, left, right, bottom, top, near, far);

		GameActivity.getInstance().getArtoolkit().setScreenSize(width, height);

		// Position the eye behind the origin.
		float eyeX = 0f;
		float eyeY = 0f;
		float eyeZ = -0.1f;

		// We are looking toward the distance
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = 1f;

		// Set our up vector. This is where our head would be pointing were we holding the camera.
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		// Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		Matrix.setLookAtM(mViewMatrixProjTopDown, 0, eyeX, eyeY, eyeZ, lookX,
				lookY, lookZ, upX, upY, upZ);

	}

	@Override
	public void onDrawFrame(GL10 glUnused) {

		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glClearColor(0, 0, 0, 0);

		ARObject markerRepere = null;
		ArrayList<ARObject> listeVisible = new ArrayList<ARObject>();

		Vector<ARObject> vec = GameActivity.getInstance().getArtoolkit()
				.getObjects();
		for (ARObject ob : vec) {
			if (ob.isVisible()) {

				mModelMatrix = ob.getModelMatrix();
				drawTriangleDirect(mTriangle2Vertices);

				// On choisis le repere en fonction de l'ID
				if (markerRepere == null && ob.getId() == this.idRepere)
					markerRepere = ob;

				// Dans tout les cas on l'ajoute dans la liste de visible
				listeVisible.add(ob);

			}
		}

		if (markerRepere != null) {
			// Récupération des matrices de modelView
			// La matrice mDeprojection est utilisée pour déprojeter les autres
			float[] mDeprojection = markerRepere.getModelMatrix();
			// On inverse la matrice de mDeprojection pour calculer la déprojection
			Matrix.invertM(mDeprojection, 0, mDeprojection, 0);

			// On recupere les matrice de chacun des marqueurs
			float[] mModelMarker = new float[16];

			for (ARObject ob : listeVisible) {
				// Puis on multiplie la matrice du marqueur pour obtenir la matrice "model" du markeur par rapport au marqueur "repere"
				Matrix.multiplyMM(mModelMarker, 0, mDeprojection, 0, ob.getModelMatrix(),
						0);

				// On reintitialise la matrice mModelMatrix a une matrice identity
				Matrix.setIdentityM(mModelMatrix, 0);
				// On ne copie que les coordonées en X et Y (Z n'est pas utilisé)
				mModelMatrix[13] = mModelMarker[13];
				mModelMatrix[12] = mModelMarker[12];
				// On dessine le marqueur
				drawTriangle(mTriangle3Vertices);

			}
		}
	}

	private void drawTriangleDirect(FloatBuffer aTriangleBuffer) {
		// Pass in the position information
		aTriangleBuffer.position(mPositionOffset);
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize,
				GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer);

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Pass in the color information
		aTriangleBuffer.position(mColorOffset);
		GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize,
				GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer);

		GLES20.glEnableVertexAttribArray(mColorHandle);

		mMVPMatrix = mModelMatrix.clone();
		mProjectionMatrixAR = ARObject.getProjMatrix();

		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(mPMatrixHandle, 1, false,
				mProjectionMatrixAR, 0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

	}

	/**
	 * Draws a triangle from the given vertex data.
	 * 
	 * @param aTriangleBuffer
	 *            The buffer containing the vertex data.
	 */
	private void drawTriangle(final FloatBuffer aTriangleBuffer) {
		// Pass in the position information
		aTriangleBuffer.position(mPositionOffset);
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize,
				GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer);

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Pass in the color information
		aTriangleBuffer.position(mColorOffset);
		GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize,
				GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer);

		GLES20.glEnableVertexAttribArray(mColorHandle);

		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrixProjTopDown, 0,
				mModelMatrix, 0);

		/*mProjectionMatrix = ARObject.getProjMatrix();
		mProjectionMatrix[10] = -mProjectionMatrix[10];
		mProjectionMatrix[11] = -mProjectionMatrix[11];*/

		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(mPMatrixHandle, 1, false,
				mProjectionMatrixTopDown, 0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
	}

	@Override
	public void makerVisibilityChanged(boolean visible) {
		if (visible)
			mProjectionMatrixAR = ARObject.getProjMatrix();
		else
			Matrix.setIdentityM(mProjectionMatrixAR, 0);

	}
}
