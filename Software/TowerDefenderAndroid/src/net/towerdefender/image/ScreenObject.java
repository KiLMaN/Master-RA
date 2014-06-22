package net.towerdefender.image;

import gameplay.Enemie;
import android.opengl.Matrix;

public class ScreenObject {
	public enum ScreenObectType {
		SCREEN_OBJECT_TOWER, SCREEN_OBJECT_START, SCREEN_OBJECT_OBJECTIVE, SCREEN_OBJECT_ENEMIE
	}

	private float _posX = 0;
	private float _posY = 0;
	private int _id = 0;

	private ScreenObectType _type = ScreenObectType.SCREEN_OBJECT_ENEMIE;

	private ARObject _object;

	private Enemie _enemie = null;

	public ScreenObject(int id, ScreenObectType type, float posX, float posY) {
		this._id = id;
		this._type = type;
		this._posX = posX;
		this._posY = posY;
	}

	public void setARObject(ARObject object) {
		_object = object;
	}

	public ARObject getARObject() {
		return _object;
	}

	public float getPosX() {
		if (_type == ScreenObectType.SCREEN_OBJECT_ENEMIE)
			return _enemie.getPosition().getPositionX();
		return _posX;
	}

	public void setPosX(float _PosX) {
		if (_type == ScreenObectType.SCREEN_OBJECT_ENEMIE)
			_enemie.getPosition().setPositionX(_PosX);
		else
			this._posX = _PosX;
	}

	public float getPosY() {
		if (_type == ScreenObectType.SCREEN_OBJECT_ENEMIE)
			return _enemie.getPosition().getPositionY();
		return _posY;
	}

	public void setPosY(float _PosY) {
		if (_type == ScreenObectType.SCREEN_OBJECT_ENEMIE)
			_enemie.getPosition().setPositionY(_PosY);
		else
			this._posY = _PosY;
	}

	public ScreenObectType getType() {
		return _type;
	}

	public void setType(ScreenObectType _type) {
		this._type = _type;
	}

	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public void setPosition(float _PosX, float _PosY) {
		setPosY(_PosX);
		setPosY(_PosY);
	}

	public Enemie get_enemie() {
		return _enemie;
	}

	public void set_enemie(Enemie _enemie) {
		this._enemie = _enemie;
	}

	public float[] getModelMatrix(float[] matrixProj) {
		float[] tran = new float[16];

		Matrix.setIdentityM(tran, 0);
		tran[12] = this.getPosX();
		tran[13] = this.getPosY();
		Matrix.multiplyMM(tran, 0, matrixProj, 0, tran, 0);
		return tran;

	}

}
