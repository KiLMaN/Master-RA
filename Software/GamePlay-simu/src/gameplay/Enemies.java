package gameplay;

public class Enemies {

	private int idEnemi;
	private String nameEnemi;	
	private int lifePointsEnemis;
	private boolean lifeEnemi;
	
	
	public Enemies(int idEnemi, String nameEnemi, int lifePointsEnemis, boolean lifeEnemi) {
		super();
		this.idEnemi = idEnemi;
		this.nameEnemi = nameEnemi;
		this.lifePointsEnemis = lifePointsEnemis;
		this.lifeEnemi = lifeEnemi;
	}
	public int getIdEnemi() {
		return idEnemi;
	}
	public void setIdEnemi(int idEnemi) {
		this.idEnemi = idEnemi;
	}
	public String getNameEnemi() {
		return nameEnemi;
	}
	public void setNameEnemi(String nameEnemi) {
		this.nameEnemi = nameEnemi;
	}
	public int getLifePointsEnemis() {
		return lifePointsEnemis;
	}
	public void setLifePointsEnemis(int lifePointsEnemis) {
		this.lifePointsEnemis = lifePointsEnemis;
	}
	public boolean isLifeEnemi() {
		return lifeEnemi;
	}
	public void setLifeEnemi(boolean lifeEnemi) {
		this.lifeEnemi = lifeEnemi;
	}
		
		
}
