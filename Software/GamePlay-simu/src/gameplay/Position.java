package gameplay;

public class Position {

	private float positionX;
	private float positionY;

	public Position() {
		this.positionX = 0;
		this.positionY = 0;
	}

	public Position(Position position) {
		this.positionX = position.getPositionX();
		this.positionY = position.getPositionY();
	}

	public Position(float positionX, float positionY) {
		super();
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public float distanceTo(Position otherPosition) {
		float deltaX = this.positionX - otherPosition.getPositionX();
		float deltaY = this.positionY - otherPosition.getPositionY();
		return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
	}

	public String toString() {
		return "[X : " + positionX + " | Y : " + positionY + "]";
	}

	public void moveTo(Position objectif, float distance) {
		float deltaX = this.positionX - objectif.getPositionX();
		float deltaY = this.positionY - objectif.getPositionY();

		// TODO : PATHFINDING FOLLOWER
		if (Math.abs(deltaX) > Math.abs(deltaY)) {
			this.positionX += (deltaX < 0) ? distance : -distance;
		} else if (Math.abs(deltaX) < Math.abs(deltaY)) {
			this.positionY += (deltaY < 0) ? distance : -distance;
		} else {
			if (deltaX == 0)
				return;
			else
				this.positionX += (deltaX < 0) ? distance : -distance;
		}
	}

	public Position clone() {
		return new Position(this);
	}
}
