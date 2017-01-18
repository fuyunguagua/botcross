
public class Host{
	private int ID;
	private double score;//½©Ê¬µÃ·Ö
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
			if(this.ip.equals(((Host)obj).getIp())){
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		char [] arr = this.getIp().toCharArray();
		int code = 0;
		for(int i =0;i<arr.length;i++){
			code += (int)arr[i];
		}
		return code;
	}
	
}
