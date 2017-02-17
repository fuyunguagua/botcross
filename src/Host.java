
public class Host{
	private int ID;
	private double score;//��ʬ�÷�
	private String ip;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Host){
			if(this.ID == ((Host)obj).getID()){
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		return ID;
	}
	
}
