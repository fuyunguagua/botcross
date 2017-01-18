
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Main {
	/*
	 *��ȡ�����ļ�����ʼ��һ��������A-plane�е���
	 *�������ļ��ϣ���ΪH
	 *��������H�����μ���ÿ�������Ľ�ʬ�÷֣����÷�С����ֵ������ˢ��
	 *�������ƶ� ���ٲ�ξ��ࣨ��ʱ��д��
	 *�����������ļ���һ����A�Ľ����һ����C�Ľ����
	 */
	public static void main(String[] args){
		try {
			BufferedReader bReaderA = new BufferedReader(new InputStreamReader( new FileInputStream(new File("AResult.csv"))));
			BufferedReader bReaderC = new BufferedReader(new InputStreamReader( new FileInputStream(new File("CResult.csv"))));
			String line = null;
			Set<Host> H = new HashSet<Host>();
			ArrayList<Set<Host>> Alist = new ArrayList<Set<Host>>();
			ArrayList<Set<Host>> Clist = new ArrayList<Set<Host>>();
			//��A����ļ�
			int hostIndex = 0;
			while((line = bReaderA.readLine())!=null){
				String [] arr = line.split(",");
				if(hostIndex == 0){
					//�ļ������һ���Ǹ�������ip��ַ
					for(int i = 0;i<arr.length;i++){
						Set<Host> aSet = new HashSet<Host>();
						Alist.add(aSet);
					}
				}
				//һ����¼��Ӧһ������
				Host host = new Host();
				host.setID(hostIndex);
				H.add(host);//������������
				
				for(int i = 0;i<arr.length;i++){
					if(Integer.parseInt(arr[i])==1){
						Alist.get(i).add(host);
					}
				}
				hostIndex++;
			}
			//��C����ļ�
			//��������������Id��ô��Ӧ�ϣ�ip
			hostIndex = 0;
			while((line = bReaderC.readLine())!=null){
				String [] arr = line.split(",");
				if(hostIndex == 0){
					for(int i = 0;i<arr.length;i++){
						Set<Host> aSet = new HashSet<Host>();
						Clist.add(aSet);
					}
				}
				//һ����¼��Ӧһ������
				Host host = new Host();
				host.setID(hostIndex);
				
				//�˴����������������
				for(int i = 0;i<arr.length;i++){
					if(Integer.parseInt(arr[i])==1){
						Clist.get(i).add(host);
					}
				}
				hostIndex++;
			}
			
			
			//�������������Ľ�ʬ�÷�
			for(Host h: H){
				double score = botScore(h, Alist, Clist);
				h.setScore(score);
			}
			//�������ƶ�
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	//����ĳ�������Ľ�ʬ�÷�
	//��������h����Aƽ������ �ȼ��㽻���ĸ��������󲢼��ĸ��� ������� 
	public static double botScore(Host h,ArrayList<Set<Host>> Alist,ArrayList<Set<Host>> Clist){
		double score = 0;
		int w = 1;
		for(int i = 0;i<Alist.size();i++){
			Set<Host> Ai = Alist.get(i);
			for(int j=i+1;j<Alist.size();j++){
				Set<Host> Aj = Alist.get(j);
				Set<Host> intersection = new HashSet<Host>();
				intersection.addAll(Ai);
				intersection.retainAll(Aj);
				Set<Host> union = new HashSet<Host>();
				union.addAll(Ai);
				union.addAll(Aj);
				score += w*w*intersection.size()/union.size();
			}
		}
		
		for(int i = 0;i<Alist.size();i++){
			Set<Host> Ai = Alist.get(i);
			for(int k=0;k<Clist.size();k++){
				Set<Host> Ck = Clist.get(k);
				Set<Host> intersection = new HashSet<Host>();
				intersection.addAll(Ai);
				intersection.retainAll(Ck);
				Set<Host> union = new HashSet<Host>();
				union.addAll(Ai);
				union.addAll(Ck);
				score += w*intersection.size()/union.size();
			}
		}
		return score;
	}
	//�����������������ƶ�
	//�ȶ������������������������������ٰ��չ�ʽ����
	public static int similarity(Host h1,Host h2,ArrayList<Set<Host>> AClist,int mb){
		int sim = 0;
		StringBuilder host1 = new StringBuilder();
		StringBuilder host2 = new StringBuilder();
		for(Set<Host> set : AClist){
			if(set.contains(host1)){
				host1.append("1");
			}else{
				host1.append("0");
			}
			
			if(set.contains(host2)){
				host2.append("1");
			}else{
				host2.append("0");
			}
		}
		char [] c1 = host1.toString().toCharArray();
		char [] c2 = host2.toString().toCharArray();
		//��Ӧʽ�ӵ�ǰ�벿��
		for(int i = 0;i<mb;i++){
			if(c1[i] == c2[i]){
				sim++;
			}
		}
		//��벿��
		int temp = 0;
		for(int i = mb;i<AClist.size();i++){
			if(c1[i] == c2[i]){
				temp++;
			}
		}
		if (temp>=1) {
			sim++;
		}
		
		return sim;
	}
}






