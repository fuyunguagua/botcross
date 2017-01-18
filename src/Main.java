
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
	 *读取输入文件，初始化一个包含在A-plane中的所
	 *有主机的集合，记为H
	 *遍历集合H，依次计算每个主机的僵尸得分，将得分小于阈值的主机刷掉
	 *计算相似度 ，再层次聚类（暂时不写）
	 *输入有两个文件，一个是A的结果，一个是C的结果。
	 */
	public static void main(String[] args){
		try {
			BufferedReader bReaderA = new BufferedReader(new InputStreamReader( new FileInputStream(new File("AResult.csv"))));
			BufferedReader bReaderC = new BufferedReader(new InputStreamReader( new FileInputStream(new File("CResult.csv"))));
			String line = null;
			Set<Host> H = new HashSet<Host>();
			ArrayList<Set<Host>> Alist = new ArrayList<Set<Host>>();
			ArrayList<Set<Host>> Clist = new ArrayList<Set<Host>>();
			//读A结果文件
			int hostIndex = 0;
			while((line = bReaderA.readLine())!=null){
				String [] arr = line.split(",");
				if(hostIndex == 0){
					//文件里最后一列是该主机的ip地址
					for(int i = 0;i<arr.length-1;i++){
						Set<Host> aSet = new HashSet<Host>();
						Alist.add(aSet);
					}
				}
				//一条记录对应一个主机
				String ip = arr[arr.length-1];
				Host host = new Host();
				host.setID(hostIndex);
				host.setIp(ip);
				H.add(host);//更新主机集合
				
				for(int i = 0;i<arr.length-1;i++){
					if(Integer.parseInt(arr[i])==1){
						Alist.get(i).add(host);
					}
				}
				hostIndex++;
			}
			//读C结果文件
			//两个结果里的主机Id怎么对应上？ip
			hostIndex = 0;
			while((line = bReaderC.readLine())!=null){
				String [] arr = line.split(",");
				if(hostIndex == 0){
					for(int i = 0;i<arr.length-1;i++){
						Set<Host> aSet = new HashSet<Host>();
						Clist.add(aSet);
					}
				}
				//一条记录对应一个主机
				String ip = arr[arr.length-1];
				Host host = new Host();
				host.setID(hostIndex);
				host.setIp(ip);
				//此处不需更新主机集合
				for(int i = 0;i<arr.length-1;i++){
					if(Integer.parseInt(arr[i])==1){
						Clist.get(i).add(host);
					}
				}
				hostIndex++;
			}
			
			
			//计算所有主机的僵尸得分
			for(Host h: H){
				double score = botScore(h, Alist, Clist);
				h.setScore(score);
			}
			//计算相似度
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	//计算某个主机的僵尸得分
	//包含主机h所有A平面聚类的 先计算交集的个数，再求并集的个数 两者相除 
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
	//计算两个主机的相似度
	//先对两个主机生成两个二进制向量，再按照公式计算
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
		//对应式子的前半部分
		for(int i = 0;i<mb;i++){
			if(c1[i] == c2[i]){
				sim++;
			}
		}
		//后半部分
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






