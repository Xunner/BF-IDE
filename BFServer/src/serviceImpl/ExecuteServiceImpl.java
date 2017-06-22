//请不要修改本文件名
package serviceImpl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import service.ExecuteService;

public class ExecuteServiceImpl implements ExecuteService {

	/**
	 * 请实现该方法
	 */
	@Override
	public String BFExecute(String code, String param) throws RemoteException {
		HashMap<Integer, Integer> indexLeftToRight = new HashMap<Integer, Integer>();	//	[ -> ]
		HashMap<Integer, Integer> indexRightToLeft= new HashMap<Integer, Integer>();	//	] -> [
		ArrayList<Character> memory = new ArrayList<Character>();	//	内存
		StringBuilder output = new StringBuilder();	//	输出
		char[] cmds = code.toCharArray();	//	代码
		int index = 0;	//	输入指针
		int ptr = 0;	//	内存指针
		//	遍历代码，记录"["与"]"的位置
		ArrayList<Integer> indexesOfLeft = new ArrayList<Integer>();
		for(int i=0; i<cmds.length; i++){
			if(cmds[i]=='['){
				indexesOfLeft.add(i);
			}
			else if(cmds[i]==']'){
				if(indexesOfLeft.isEmpty()){
					return "MMP";
				}
				else{
					int tmp = indexesOfLeft.get(indexesOfLeft.size()-1);
					indexLeftToRight.put(tmp, i);
					indexRightToLeft.put(i, tmp);
					indexesOfLeft.remove(indexesOfLeft.size()-1);
				}
			}
		}
		if(!indexesOfLeft.isEmpty()){
			return "NMH";
		}
		
		//	执行代码
		memory.add('\0');
		for(int i=0; i<cmds.length; i++){
			switch(cmds[i]){
			case '>':
				ptr++;
				if(ptr>=memory.size()){
					memory.add('\0');
				}
				break;
			case '<':
				ptr--;
				if(ptr<0){
					memory.add(0, '\0');
					ptr = 0;
				}
				break;
			case '+':
				memory.set(ptr, (char)(memory.get(ptr)+1));
				break;
			case '-':
				memory.set(ptr, (char)(memory.get(ptr)-1));
				break;
			case ',':
				memory.set(ptr, param.charAt(index++));
				break;
			case '.':
				output.append(memory.get(ptr));
				break;
			case '[':
				if(memory.get(ptr)=='\0'){
					i = indexLeftToRight.get(i);
				}
				break;
			case ']':
				if(memory.get(ptr)!='\0'){
					i = indexRightToLeft.get(i);
				}
				break;
			default:
			}
		}
		return output.toString();
	}

	@Override
	public String OOKExecute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
