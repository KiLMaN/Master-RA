package net.towerdefender.image;

import gameplay.Enemie;
import gameplay.Tower;

public class ScreenObject {
	public enum ScreenObectType {
		SCREEN_OBJECT_TOWER, SCREEN_OBJECT_START, SCREEN_OBJECT_OBJECTIVE, SCREEN_OBJECT_ENEMIE
	}

	private float _posX = 0;
	private float _posY = 0;
	private int _id = 0;

	private ScreenObectType _type = ScreenObectType.SCREEN_OBJECT_ENEMIE;

	private ARObject _object;

	private Tower _tower = null;
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
		return _posX;
	}

	public void setPosX(float _PosX) {
		this._posX = _PosX;
	}

	public float getPosY() {
		return _posY;
	}

	public void setPosY(float _PosY) {
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
		this._posX = _PosX;
		this._posY = _PosY;
	}
}
