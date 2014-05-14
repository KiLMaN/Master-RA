package gameplay;

import java.util.ArrayList;

public class Path {
	private ArrayList<Position> pathPoints;

	public Path() {
		this.pathPoints = new ArrayList<Position>();
	}

	@SuppressWarnings("unchecked")
	public Path(Path path) {
		this.pathPoints = (ArrayList<Position>) path.pathPoints.clone();
	}

	public boolean isCloseToPath(Position currentPosition) {
		return currentPosition.distanceTo(this.pathPoints.get(0)) <= GameConfig.PATH_THRESHOLD;
	}

	/*
	 * public Position getNextPathPoint(Position currentPosition) { float
	 * Distance = -1; Position next = null; for (Position position : pathPoints)
	 * { if(position.distanceTo(currentPosition) <= Distance || Distance == -1)
	 * { Distance = position.distanceTo(currentPosition); next = position; } }
	 * return next; }
	 */
	public void removeFirstPoint() {
		this.pathPoints.remove(0);
	}

	public Position getNextPoint() {
		if (this.pathPoints.size() > 0)
			return this.pathPoints.get(0);
		else
			return null;
	}

	public Path clone() {
		return new Path(this);
	}

	public void addPoint(Position position) {
		this.pathPoints.add(position);
	}

	public ArrayList<Position> getPath() {
		return this.pathPoints;
	}
}
