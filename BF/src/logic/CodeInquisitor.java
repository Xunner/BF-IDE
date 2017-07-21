package logic;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 实时检查当前代码是否含有不合法字符
 * @author 巽
 *
 */
public class CodeInquisitor {
	static private HashSet<Character> bfCharacters;	//	合法字符池，下同
	static private HashSet<String> ookCharacters;
	
	public CodeInquisitor(){
		bfCharacters = new HashSet<Character>(Arrays.asList(new Character[]{',', '.', '<', '>', '+', '-', '[', ']'}));
		ookCharacters = new HashSet<String>(Arrays.asList(new String[]{"Ook.", "Ook?", "Ook!"}));
	}
	
	public boolean isCodeLegal(String code, String language){
		String standardCode = code.replaceAll(" ", "").replaceAll("\r\n", "").replaceAll("\n", "");
		switch(language){
		case ui.MainFrameController.BF:
			for(char c : standardCode.toCharArray()){
				if(!bfCharacters.contains(c)){
					return false;
				}
			}
			break;
		case ui.MainFrameController.OOK:
			if(standardCode.isEmpty()){
				break;
			}
			else if(standardCode.length()<4){
				return false;
			}
			else{
				for(int i=0; i+4<=standardCode.length(); i++){
					if(ookCharacters.contains(standardCode.substring(i, i+4))){
						i+=3;
					}
					else{
						return false;
					}
				}
			}
			break;
		}
		return true;
	}
}
