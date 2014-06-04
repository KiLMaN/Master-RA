package gameplay;

public class Player {

	private int idPlayer;
	private String nickname;
	private int lifesPlayer;
	private int pointsPlayer;
	private int numberEnemiesKilled;

	public Player(int idPlayer, String nickname, int lifesPlayer,
			int pointsPlayer, int numberEnemiesKilled) {
		super();
		this.idPlayer = idPlayer;
		this.nickname = nickname;
		this.lifesPlayer = lifesPlayer;
		this.pointsPlayer = pointsPlayer;
		this.numberEnemiesKilled = numberEnemiesKilled;
	}

	public int getIdPlayer() {
		return idPlayer;
	}

	public void setIdPlayer(int idPlayer) {
		this.idPlayer = idPlayer;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getLifesPlayer() {
		return lifesPlayer;
	}

	public void setLifesPlayer(int lifesPlayer) {
		this.lifesPlayer = lifesPlayer;
	}

	public int getPointsPlayer() {
		return pointsPlayer;
	}

	public void setPointsPlayer(int pointsPlayer) {
		this.pointsPlayer = pointsPlayer;
	}

	public int getNumberEnemiesKilled() {
		return numberEnemiesKilled;
	}

	public void setNumberEnemiesKilled(int numberEnemiesKilled) {
		this.numberEnemiesKilled = numberEnemiesKilled;
	}

}
