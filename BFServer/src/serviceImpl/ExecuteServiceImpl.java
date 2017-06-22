//请不要修改本文件名
package serviceImpl;

import java.rmi.RemoteException;
import java.util.ArrayList;

import service.ExecuteService;

public class ExecuteServiceImpl implements ExecuteService {

	/**
	 * 请实现该方法
	 */
	@Override
	public String execute(String code, String param) throws RemoteException {
		ArrayList<Character> memory = new ArrayList<Character>();	//	内存
		StringBuilder output = new StringBuilder();	//	输出
		char[] cmds = code.toCharArray();	//	代码
		int index = 0;	//	输入指针
		int ptr = 0;	//	内存指针
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
					int tmp = code.substring(i).indexOf(']');
					if(tmp<0){
						return "nmh";
					}
					else{
						i += tmp;
					}
				}
				break;
			case ']':
				if(memory.get(ptr)!='\0'){
					int tmp = code.substring(0, i).lastIndexOf('[');
					if(tmp<0){
						return "mmp";
					}
					else{
						i = tmp;
					}
				}
				break;
			default:
			}
		}
		return output.toString();
	}

}
