import java.util.HashSet;
import java.util.Set;

public class Test {
	public static void main(String[] args) {
		Host h1 = new Host();
		h1.setIp("10,10,10,1");
		Host h2 = new Host();
		h2.setIp("10,10,10,2");
		Host h3 = new Host();
		h3.setIp("10,10,10,2");
		Host h4 = new Host();
		h4.setIp("10,10,10,3");
		Host h5 = new Host();
		h5.setIp("10,10,10,1");
		Host h6 = new Host();
		h6.setIp("10,10,10,2");
		Host h7 = new Host();
		h7.setIp("10,10,10,1");
		Host h8 = new Host();
		h8.setIp("10,10,10,1");
		Host h9 = new Host();
		h9.setIp("10,10,10,2");
		Set<Host> s1 = new HashSet<Host>();
		Set<Host> s2 = new HashSet<Host>();
		s1.add(h1);
		s1.add(h2);
		s1.add(h3);
		s1.add(h4);
		s1.add(h5);
		s1.add(h6);
		s1.add(h7);
		s1.add(h8);
		
		s2.add(h1);
		s2.add(h2);
		s2.add(h3);
		s2.add(h5);
		s2.add(h6);
		s2.add(h7);
		s2.add(h8);
		System.out.println(s1);
		s1.retainAll(s2);//½»¼¯
		System.out.println(s1);
	}
}
