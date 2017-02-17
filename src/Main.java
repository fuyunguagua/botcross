
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
	private static int a = 0;
	public static void main(String[] args){
		try {
			Set<Host> H = new HashSet<Host>();
			ArrayList<Set<Host>> Alist = new ArrayList<Set<Host>>();
			ArrayList<Set<Host>> Clist = new ArrayList<Set<Host>>();
			getAlist(Alist, H);
			getClist(Clist);
			//System.out.println(Alist.size());
			//System.out.println(Clist.size());

			getAllBotScore(H,Alist,Clist);
			outputScoreToFile(H);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void getClist(ArrayList<Set<Host>> Clist) throws NumberFormatException, IOException{
		int CIndex = -1;
		String line = null;
		BufferedReader bReaderC = new BufferedReader(new InputStreamReader( new FileInputStream(new File("CResult.csv"))));
		while((line = bReaderC.readLine())!=null){
			String [] arr = line.split(",");
			if(	Integer.parseInt(arr[0]) != CIndex){
				Set<Host> set = new HashSet<Host>();
				Clist.add(set);
				CIndex = Integer.parseInt(arr[0]);
			}
			Host host = new Host();
			host.setID(Integer.parseInt(arr[1])-1);
			Clist.get(Clist.size()-1).add(host);
		}
		bReaderC.close();
	}
	public static void getAlist(ArrayList<Set<Host>> Alist, Set<Host> H) throws NumberFormatException, IOException{
		BufferedReader bReaderA = new BufferedReader(new InputStreamReader( new FileInputStream(new File("AResult.csv"))));
		String line = null;
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
		bReaderA.close();
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
				score += w*w*(((double)intersection.size())/union.size());
				
				
			}
		}
		System.out.print(++a+" "+score);
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
				score += w*(((double)intersection.size())/union.size());
				
			}
		}
		System.out.println("__"+score);
		return score;
	}
	public static void outputScoreToFile(Set<Host> H){
		try {
			BufferedWriter bWriter = new BufferedWriter(new PrintWriter(new File("score.txt")));
			for(Host h: H){
				bWriter.write((h.getID()+1)+","+h.getScore());
				bWriter.newLine();
			}
			bWriter.flush();
			bWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void getAllBotScore(Set<Host> H, ArrayList<Set<Host>> Alist, ArrayList<Set<Host>> Clist) {
		for(Host h: H){		
			ArrayList<Set<Host>> TempAlist = new ArrayList<Set<Host>>();//包含主机h的所有A集合
			ArrayList<Set<Host>> TempClist = new ArrayList<Set<Host>>();//包含主机h的所有C集合	
			for(Set<Host> set:Alist){
				if (set.contains(h)) {
					TempAlist.add(set);
				}
			}
			//System.out.println(Clist.size());
			for(Set<Host> set:Clist){
				if (set.contains(h)) {
					TempClist.add(set);
				}
			}
			double score = botScore(h, TempAlist, TempClist);
			h.setScore(score);
		}
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






