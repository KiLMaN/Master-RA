package net.towerdefender.opengl;

import gameplay.Enemie;
import gameplay.Game;
import gameplay.Position;
import gameplay.Tower;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import net.towerdefender.activity.GameActivity;
import net.towerdefender.image.ARObject;
import net.towerdefender.image.MarkerDetectedListener;
import net.towerdefender.image.ScreenObject;
import net.towerdefender.image.ScreenObject.ScreenObectType;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import comon.EnemieUpdateListener;

/**
 * This class implements our custom renderer. Note that the GL10 parameter
 * passed in is unused for OpenGL ES 2.0 renderers -- the static class GLES20 is
 * used instead.
 */
public class LessonOneRenderer implements MarkerDetectedListener,
		GLSurfaceView.Renderer, EnemieUpdateListener {

	private ArrayList<ScreenObject> _screenObject = new ArrayList<ScreenObject>();

	/**
	 * Store the model matrix. This matrix is used to move models from object
	 * space (where each model can be thought of being located at the center of
	 * the universe) to world space.
	 */
	//private float[] mModelMatrix = new float[16];

	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix
	 * transforms world space to eye space; it positions things relative to our
	 * eye.
	 */
	private float[] mViewMatrixProjTopDown = new float[16];
	private float[] mViewMatrixTower = new float[16];
	/**
	 * Store the projection matrix. This is used to project the scene onto a 2D
	 * viewport.
	 */
	private float[] mProjectionMatrixTopDown = new float[16];
	private float[] mProjectionMatrixAR = new float[16];
	private float[] mProjectionMatrixTower = new float[16];

	/** Store our model data in a float buffer. */
	//private final FloatBuffer mTriangle1Vertices;
	private final FloatBuffer mTowerVertices;
	private final FloatBuffer mStartVertices;
	private final FloatBuffer mObjectifVertices;
	private final FloatBuffer mEnemieVertices;

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

	private int idObjectif = 9;

	private int idStart = 10;

	private boolean bMarkerVisible = false;
	private boolean bStart = false, bObjective = false, bTower = false;
	private int nbTowerOk = 1;

	public LessonOneRenderer() {
		GameActivity.getInstance().getGame().addEnemieListener(this);

		final float[] tourVerticesData = {
				// X, Y, Z, 
				// R, G, B, A
				0f, 0f, 25f, 1.0f, 0f, 0f, 1f,

				-25f, -25f, 0f, 1.0f, 1.0f, 1.0f, 1.0f,

				-25f, 25f, 0f, 0.0f, 1.0f, 1.0f, 1.0f,

				25f, 25f, 0f, 1.0f, 0.0f, 1.0f, 1.0f,

				25f, -25f, 0f, 1.0f, 1.0f, 0.0f, 1.0f,

				-25f, -25f, 0f, 1.0f, 1.0f, 1.0f, 1.0f

		};

		final float[] startVerticesData = {
				// X, Y, Z, R, G, B, A
				0f, 0f, 25f, 1.0f, 0.9f, 0f, 1f,

				-25f, -25f, 0f, 1.0f, 0.5f, 0.5f, 1f,

				-25f, 25f, 0f, 1.0f, 0.5f, 0.5f, 1f,

				25f, 25f, 0f, 1.0f, 0.5f, 0.5f, 1f,

				25f, -25f, 0f, 1.0f, 0.5f, 0.5f, 1f,

				-25f, -25f, 0f, 1.0f, 0.5f, 0.5f, 1f,

		};

		final float[] objectifVerticesData = {
				// X, Y, Z, R, G, B, A
				0f, 0f, 25f, 0.0f, 1.0f, 0.0f, 1f,

				-25f, -25f, 0f, 0.0f, 0.7f, 0.33f, 1f,

				-25f, 25f, 0f, 0.0f, 0.7f, 0.33f, 1f,

				25f, 25f, 0f, 0.0f, 0.7f, 0.3f, 1f,

				25f, -25f, 0f, 0.0f, 0.7f, 0.33f, 1f,

				-25f, -25f, 0f, 0.0f, 0.7f, 0.33f, 1f,

		};

		final float[] enemieVerticesData = {
				// X, Y, Z, R, G, B, A
				0f, 0f, 10f, 1.0f, 0.0f, 0.0f, 1f,

				-10f, -10f, 0f, 1.0f, 0.0f, 0.5f, 1f,

				-10f, 10f, 0f, 1.0f, 0.0f, 0.5f, 1f,

				10f, 10f, 0f, 1.0f, 0.0f, 0.5f, 1f,

				10f, -10f, 0f, 1.0f, 0.0f, 0.5f, 1f,

				-10f, -10f, 0f, 1.0f, 0.0f, 0.5f, 1f,

		};

		// Initialize the buffers.
		mTowerVertices = ByteBuffer
				.allocateDirect(tourVerticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mStartVertices = ByteBuffer
				.allocateDirect(startVerticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mObjectifVertices = ByteBuffer
				.allocateDirect(objectifVerticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mEnemieVertices = ByteBuffer
				.allocateDirect(enemieVerticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		//mTriangle1Vertices.put(triangle1VerticesData).position(0);
		mTowerVertices.put(tourVerticesData).position(0);
		mStartVertices.put(startVerticesData).position(0);
		mObjectifVertices.put(objectifVerticesData).position(0);
		mEnemieVertices.put(enemieVerticesData).position(0);
		Matrix.setIdentityM(mProjectionMatrixAR, 0);
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		GLES20.glClearColor(0, 0, 0, 0);

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

		GameActivity.getInstance().getArtoolkit().setScreenSize(width, height);

		/*// Position the eye behind the origin.
		float eyeX = 0f;
		float eyeY = 0f;
		float eyeZ = 0f;

		// We are looking toward the distance
		float lookX = 0.0f;
		float lookY = 0.0f;
		float lookZ = 1f;

		// Set our up vector. This is where our head would be pointing were we holding the camera.
		float upX = 0.0f;
		float upY = 1.0f;
		float upZ = 0.0f;
		Matrix.setLookAtM(mViewMatrixProjTopDown, 0, eyeX, eyeY, eyeZ, lookX,
				lookY, lookZ, upX, upY, upZ);*/

		final float left = -width / 2;
		final float right = width / 2;
		final float bottom = -height / 2;
		final float top = height / 2;
		final float near = 1.0f;
		final float far = 50.0f;

		// Projection Ortho, on n'as pas de perspective
		Matrix.orthoM(mProjectionMatrixTopDown, 0, left, right, bottom, top,
				near, far);
		float ratio = (float) width / height;
		//Matrix.orthoM(mProjectionMatrixTower, 0, left, right, bottom, top,
		//		near, far);
		Matrix.perspectiveM(mProjectionMatrixTower, 0, 30, ratio, 1, 5000);
		//Matrix.frustumM(mProjectionMatrixTopDown, 0, left, right, bottom, top, near, far);

	}

	boolean bTowerView = false;
	ScreenObject _TowerView = null;

	public void setTowerView(Tower tower) {
		if (tower != null) {
			int i = 0;
			for (i = 0; i < _screenObject.size(); i++) {
				if (_screenObject.get(i).getARObject().getIdRA() == tower
						.getIdTower()) {
					_TowerView = _screenObject.get(i);
					break;
				}
			}
			bTowerView = true;
		} else {
			_TowerView = null;
			bTowerView = false;
		}
	}

	@Override
	public void onDrawFrame(GL10 glUnused) {
		// Si on a pas assez d'information pour le calcul alors on affiche un fond rouge en plus
		if (bMarkerVisible && (bStart && bObjective && bTower))
			GLES20.glClearColor(0f, 0f, 0f, 0f);
		else
			GLES20.glClearColor(0.5f, 0f, 0f, 0.5f);

		// netoyer l'ecran
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

		if (!bTowerView || _TowerView == null) {
			float[] mMVmatrix = new float[16];
			float[] mMatricProj = null;
			if (markerRepere != null) {
				mMatricProj = markerRepere.getModelMatrix();
			}
			for (int i = 0; i < _screenObject.size(); i++) {
				ScreenObject screenObj = _screenObject.get(i);
				if ((screenObj.getARObject() != null && screenObj.getARObject()
						.isVisible()) || (screenObj.getARObject() == null)) {

					/*Matrix.setIdentityM(mMVmatrix, 0);
					mMVmatrix[12] = screenObj.getPosX();
					mMVmatrix[13] = screenObj.getPosY();

					// Project model in projection topDown
					Matrix.multiplyMM(mMVmatrix, 0, mViewMatrixProjTopDown, 0,
							mMVmatrix, 0);*/

					// On dessine le marqueur
					//drawTriangle(mTriangle3Vertices);
					switch (screenObj.getType()) {
					case SCREEN_OBJECT_ENEMIE:
						if (mMatricProj != null) {
							draw(screenObj.getModelMatrix(mMatricProj),
									mEnemieVertices, mProjectionMatrixAR);
						}
						break;
					case SCREEN_OBJECT_OBJECTIVE:
						if (mMatricProj != null) {
							draw(screenObj.getModelMatrix(mMatricProj),
									mObjectifVertices, mProjectionMatrixAR);
						}
						//draw(screenObj.getARObject().getModelMatrix(),
						//		mObjectifVertices, mProjectionMatrixAR);
						break;
					case SCREEN_OBJECT_START:
						draw(screenObj.getARObject().getModelMatrix(),
								mStartVertices, mProjectionMatrixAR);
						break;
					case SCREEN_OBJECT_TOWER:
						if (mMatricProj != null) {
							draw(screenObj.getModelMatrix(mMatricProj),
									mTowerVertices, mProjectionMatrixAR);
						}
						//draw(screenObj.getARObject().getModelMatrix(),
						//		mTowerVertices, mProjectionMatrixAR);
						break;
					default:
						break;

					}
				}
			}
		} else {
			float[] mViewMatrixTowerView = new float[16];
			// Position the eye behind the origin.
			float eyeX = _TowerView.getPosXTower(null);
			float eyeY = 0f;
			float eyeZ = _TowerView.getPosYTower(null);

			// We are looking toward the distance
			float lookX = _TowerView.getPosXTower(null) - 1;
			float lookY = 0f;
			float lookZ = _TowerView.getPosYTower(null);

			// Set our up vector. This is where our head would be pointing were we holding the camera.
			float upX = 0.0f;
			float upY = 0.0f;
			float upZ = 1.0f;

			Matrix.setLookAtM(mViewMatrixTowerView, 0, eyeX, eyeY, eyeZ, lookX,
					lookY, lookZ, upX, upY, upZ);

			for (int i = 0; i < _screenObject.size(); i++) {
				ScreenObject screenObj = _screenObject.get(i);

				if (_TowerView == screenObj)
					continue;
				if ((screenObj.getARObject() != null && screenObj.getARObject()
						.isVisible()) || (screenObj.getARObject() == null)) {

					//draw(screenObj.getARObject().getModelMatrix(),
					//		mObjectifVertices, mProjectionMatrixAR);

					/*Matrix.setIdentityM(mMVmatrix, 0);
					mMVmatrix[12] = screenObj.getPosX();
					mMVmatrix[13] = screenObj.getPosY();

					// Project model in projection topDown
					Matrix.multiplyMM(mMVmatrix, 0, mViewMatrixProjTopDown, 0,
							mMVmatrix, 0);*/

					// On dessine le marqueur
					//drawTriangle(mTriangle3Vertices);
					switch (screenObj.getType()) {
					case SCREEN_OBJECT_ENEMIE:
						draw(screenObj.getModelMatrixTower(
								mViewMatrixTowerView, Repere), mEnemieVertices,
								mProjectionMatrixTower);
						break;
					case SCREEN_OBJECT_OBJECTIVE:

						draw(screenObj.getModelMatrixTower(
								mViewMatrixTowerView, null), mObjectifVertices,
								mProjectionMatrixTower);

						//draw(screenObj.getARObject().getModelMatrix(),
						//		mObjectifVertices, mProjectionMatrixAR);
						break;
					case SCREEN_OBJECT_START:
						//draw(mMVmatrix, mStartVertices, mProjectionMatrixTopDown);
						draw(screenObj.getModelMatrixTower(
								mViewMatrixTowerView, null), mStartVertices,
								mProjectionMatrixTower);
						break;
					case SCREEN_OBJECT_TOWER:
						//draw(mMVmatrix, mTowerVertices, mProjectionMatrixTopDown);
						draw(screenObj.getModelMatrixTower(
								mViewMatrixTowerView, null), mTowerVertices,
								mProjectionMatrixTower);
						//draw(screenObj.getARObject().getModelMatrix(),
						//		mTowerVertices, mProjectionMatrixAR);
						break;
					default:
						break;

					}
				}
			}
		}
	}

	private void draw(float[] mMVMatrix, FloatBuffer aTriangleBuffer,
			float[] mProjectionMatrix) {
		// Informations de position directement au vertex
		aTriangleBuffer.position(mPositionOffset);
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize,
				GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		// End Position

		// Informations de couleur directement au vertex
		aTriangleBuffer.position(mColorOffset);
		GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize,
				GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer);
		GLES20.glEnableVertexAttribArray(mColorHandle);
		// End Couleurs

		// On passe les matrices de modelview et de projection
		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVMatrix, 0);
		GLES20.glUniformMatrix4fv(mPMatrixHandle, 1, false, mProjectionMatrix,
				0);
		// On dessines
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

	}

	@Override
	public void makerDetected(boolean visible) {
		bMarkerVisible = visible;
		if (visible) {
			mProjectionMatrixAR = ARObject.getProjMatrix();
		}
	}

	ARObject markerRepere = null;
	ARObject markerTower = null;
	ScreenObject Repere = null;

	@Override
	public void makerUpdated() {
		// Update de la matrice de projectionAR // Pas necessaire mais au cas ou 
		mProjectionMatrixAR = ARObject.getProjMatrix();
		bObjective = false;
		bStart = false;
		bTower = false;
		markerRepere = null;
		markerTower = null;
		ArrayList<ARObject> listeVisible = new ArrayList<ARObject>();

		Vector<ARObject> vec = GameActivity.getInstance().getArtoolkit()
				.getObjects();
		for (ARObject ob : vec) {
			if (ob.isVisible()) {

				//mModelMatrix = ob.getModelMatrix();
				//drawTriangleDirect(mTriangle2Vertices);

				// On choisis le repere en fonction de l'ID
				if (markerRepere == null && ob.getIdRA() == this.idStart) {
					markerRepere = ob;
				}

				if (markerTower == null && _TowerView != null
						&& ob.getIdRA() == _TowerView.getId()) {
					markerTower = ob;
				}
				// Dans tout les cas on l'ajoute dans la liste de visible
				listeVisible.add(ob);

			}
		}

		float[] mDeprojection = null;
		float[] mDeprojectionTower = null;
		// Il nous faut le marquer de repere pour tout les calculs
		if (markerRepere != null) {
			// Récupération des matrices de modelView
			// La matrice mDeprojection est utilisée pour déprojeter les autres
			mDeprojection = markerRepere.getModelMatrix();
			// On inverse la matrice de mDeprojection pour calculer la déprojection
			Matrix.invertM(mDeprojection, 0, mDeprojection, 0);
		}
		if (markerTower != null) {
			// Récupération des matrices de modelView
			// La matrice mDeprojection est utilisée pour déprojeter les autres
			mDeprojectionTower = markerTower.getModelMatrix();

			// On inverse la matrice de mDeprojection pour calculer la déprojection
			Matrix.invertM(mDeprojectionTower, 0, mDeprojectionTower, 0);

		}

		// On recupere les matrice de chacun des marqueurs
		float[] mModelMarker = new float[16];
		float[] mModelMarkerTowerView = new float[16];
		//float[] mModelTopDown = new float[16];

		for (ARObject ob : listeVisible) {
			// Si on à le repere alors on peut calculer la déprojection
			if (markerRepere != null) {
				// Puis on multiplie la matrice du marqueur pour obtenir la matrice "model" du markeur par rapport au marqueur "repere"
				Matrix.multiplyMM(mModelMarker, 0, mDeprojection, 0,
						ob.getModelMatrix(), 0);
				if (mDeprojectionTower != null) {
					Matrix.multiplyMM(mModelMarkerTowerView, 0,
							mDeprojectionTower, 0, ob.getModelMatrix(), 0);
				}

				// On reintitialise la matrice mModelMatrix a une matrice identity
				//Matrix.setIdentityM(mModelTopDown, 0);
				// On ne copie que les coordonées en X et Y et Z utilisé pour la compensation (TODO : A AMELIORER)
				//mModelMatrix = mModelMarker;
				//mModelTopDown[13] = mModelMarker[13];
				//mModelTopDown[12] = mModelMarker[12];
				//mModelTopDown[14] = mModelMarker[14];

				// Project model in projection topDown
				//Matrix.multiplyMM(mModelMarker, 0, mViewMatrixProjTopDown, 0,
				//		mModelTopDown, 0);
				// On dessine le marqueur
				//drawTriangle(mTriangle3Vertices);
			}

			boolean bNewObject = true;
			// On verifie tout les objects déjà existants
			for (ScreenObject screenObj : _screenObject) {
				// Si l'ID correspond alors on update les positions
				if (screenObj.getId() == ob.getIdRA()) {

					if (markerRepere == ob) {
						Repere = screenObj;
					}

					bNewObject = false;
					screenObj.setPosition(mModelMarker[12], mModelMarker[13]);
					if (_TowerView != null)
						screenObj.setPositionTowerWise(
								mModelMarkerTowerView[12],
								mModelMarkerTowerView[13]);
					break;
				}
			}

			// Si on a pas encore déjà creer l'objet, alors on le fait maintenant
			if (bNewObject) {
				// Par défaut c'est une tour
				ScreenObectType type = ScreenObectType.SCREEN_OBJECT_TOWER;

				// Si l'Id correspond a l'ID du départ on definit l'objet comme le départ
				if (this.idStart == ob.getIdRA()) {
					type = ScreenObectType.SCREEN_OBJECT_START;

				}
				// Si l'Id correspond a l'ID de l'objectif on definit l'objet comme l'objectif
				else if (this.idObjectif == ob.getIdRA()) {
					type = ScreenObectType.SCREEN_OBJECT_OBJECTIVE;
				}

				ScreenObject screenObj = new ScreenObject(ob.getIdRA(), type,
						mModelMarker[12], mModelMarker[13]);
				screenObj.setARObject(ob);
				if (_TowerView != null)
					screenObj.setPositionTowerWise(mModelMarkerTowerView[12],
							mModelMarkerTowerView[13]);

				// Ne pas oublier de l'ajouter dans la liste des objets
				_screenObject.add(screenObj);
			}
		}

		int cptTower = 0;
		// On verifie si toutes les positions sont mises
		for (ScreenObject screenObj : _screenObject) {
			// Si l'ID correspond alors on update les positions
			if (screenObj.getARObject() != null
					&& screenObj.getARObject().isVisible()) {
				switch (screenObj.getType()) {
				case SCREEN_OBJECT_ENEMIE:
					break;
				case SCREEN_OBJECT_OBJECTIVE:
					if (screenObj.getPosX() != 0 || screenObj.getPosY() != 0)
						bObjective = true;
					break;
				case SCREEN_OBJECT_START:
					//if (screenObj.getPosX() != 0 || screenObj.getPosY() != 0)
					bStart = true;
					break;
				case SCREEN_OBJECT_TOWER:
					if (screenObj.getPosX() != 0 || screenObj.getPosY() != 0)
						if (++cptTower >= nbTowerOk)
							bTower = true;
					break;
				default:
					break;

				}
			}
		}
		// Calcul des position enemies
		if (bMarkerVisible && (bStart && bObjective && bTower)) {
			Game currentGame = GameActivity.getInstance().getGame();

			ArrayList<Tower> listTower = GameActivity.getInstance().getGame()
					.getListTowers();
			boolean bNewTower = true;
			for (ScreenObject screenObj : _screenObject) {
				if (screenObj.getType() == ScreenObectType.SCREEN_OBJECT_TOWER) {
					for (Tower tower : listTower) {
						if (tower.getIdTower() == screenObj.getId()) {
							tower.setPosition(new Position(screenObj.getPosX(),
									screenObj.getPosY()));
							bNewTower = false;
							break;
						}
					}
					if (bNewTower) {
						System.out
								.println("A tower has been found but not in the network");
						Tower tower = new Tower(screenObj.getId(),
								new Position(screenObj.getPosX(),
										screenObj.getPosY()));
						currentGame.addTower(tower);
						//TODO: rescan network
					}
				} else if (!currentGame.isPlaying()) {
					if (screenObj.getType() == ScreenObectType.SCREEN_OBJECT_OBJECTIVE) {
						currentGame.setObjectiveEnemie(new Position(screenObj
								.getPosX(), screenObj.getPosY()));

					} else if (screenObj.getType() == ScreenObectType.SCREEN_OBJECT_START) {
						currentGame.setStartPointEnemie(new Position(screenObj
								.getPosX(), screenObj.getPosY()));
						//_TowerView = screenObj;
					}
				}
			}
			if (!currentGame.isPaused()) {
				if (System.currentTimeMillis()
						- GameActivity.getInstance().getGame()
								.getLastGameTick() >= 100) {
					GameActivity.getInstance().getGame().gameTick();
				}
			}
		}
	}

	@Override
	public void EnemieSpawned(Enemie en) {
		ScreenObject sc = new ScreenObject(en.getId(),
				ScreenObectType.SCREEN_OBJECT_ENEMIE, 0, 0);
		sc.set_enemie(en);
		_screenObject.add(sc);

	}

	@Override
	public void EnemieDied(Enemie en) {
		ScreenObject toRemove = null;
		for (ScreenObject sc : _screenObject) {
			if (sc.getId() == en.getId()
					&& sc.getType() == ScreenObectType.SCREEN_OBJECT_ENEMIE) {
				toRemove = sc;
				break;
			}
		}
		_screenObject.remove(toRemove);
	}
}
