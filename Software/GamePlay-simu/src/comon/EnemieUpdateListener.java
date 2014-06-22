package comon;

import gameplay.Enemie;

public interface EnemieUpdateListener {
	public void EnemieSpawned(Enemie en);

	public void EnemieDied(Enemie en);
}
