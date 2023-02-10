public class Compteur implements java.io.Serializable {
	
	int data;
	
	public Compteur() {
		data = 0;
	}
	
	public void compter(int enplus) {
		data = data + enplus;
	}
	
	public int read() {
		return data;	
	}
	
}
