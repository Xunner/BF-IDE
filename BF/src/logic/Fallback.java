package logic;

import java.util.ArrayList;

/**
 * 用于undo操作和redo操作
 * @author 巽
 *
 */
public class Fallback {
	private ArrayList<String> former = new ArrayList<String>();
	private ArrayList<String> latter = new ArrayList<String>();
	
	public Fallback(String code){
		former.add(code);
	}
	
	public boolean isPushable(String code){
		if(former.size()==0 || !former.get(former.size()-1).equals(code)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void push(String code){
		former.add(code);
		latter.clear();
	}
	
	public String undo(){
		final int indexOfLast = former.size() - 1;
		if(indexOfLast<0){
			return "";
		}
		else if(indexOfLast==0){
//			System.out.println();
//			for(int i=0; i<former.size(); i++){
//				System.out.println(former.get(i));
//			}
			return former.get(indexOfLast);
		}
		else{
			latter.add(former.get(indexOfLast));
			former.remove(indexOfLast);
//			System.out.println();
//			for(int i=0; i<former.size(); i++){
//				System.out.println(former.get(i));
//			}
			return former.get(indexOfLast-1);
		}
	}
	
	public String redo(){
		final int indexOfLast = latter.size() - 1;
		if(indexOfLast>=0){
			former.add(latter.get(indexOfLast));
			latter.remove(indexOfLast);
		}
		return former.get(former.size()-1);
	}
}
