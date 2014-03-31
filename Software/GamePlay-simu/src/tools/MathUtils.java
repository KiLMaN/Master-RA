package tools;

import gameplay.Position;

public class MathUtils {
	static public double AngleBetweenVectors(Position StartA, Position StopA,
			Position StartB, Position StopB) {
		Position DeltaA = new Position();
		Position DeltaB = new Position();

		double angle;

		// Calcul des deltas
		DeltaA.setPositionX(StartA.getPositionX() - StopA.getPositionX());
		DeltaA.setPositionY(StartA.getPositionY() - StopA.getPositionY());

		DeltaB.setPositionX(StartB.getPositionX() - StopB.getPositionX());
		DeltaB.setPositionY(StartB.getPositionY() - StopB.getPositionY());

		// Calcul du Cos avec le produit scalaire
		double cosa = (double) ((DeltaA.getPositionX() * DeltaB.getPositionX() + DeltaA
				.getPositionY() * DeltaB.getPositionY()))
				/ (Math.sqrt(DeltaA.getPositionX() * DeltaA.getPositionX()
						+ DeltaA.getPositionY() * DeltaA.getPositionY()) * Math
							.sqrt(DeltaB.getPositionX() * DeltaB.getPositionX()
									+ DeltaB.getPositionY()
									* DeltaB.getPositionY()));

		// angle en degré
		angle = ((180.0 / Math.PI) * Math.acos(cosa));

		// Signe de l'angle

		int sign = (DeltaA.getPositionX() * DeltaB.getPositionY() - DeltaA
				.getPositionY() * DeltaB.getPositionX()) > 0 ? 1 : -1;

		return angle * sign;
	}

	// Calcul de l'angle entre deux vecteurs (StartA,StopA) et (StartB,StopB)
	// (retourne [0,360])
	static public double TrueAngleBetweenVectors(Position StartA,
			Position StopA, Position StartB, Position StopB) {
		double a = AngleBetweenVectors(StartA, StopA, StartB, StopB);
		a = (a + 360.0) % 360.0;
		return a;
	}
}
