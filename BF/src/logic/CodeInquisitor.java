package logic;

import java.util.Arrays;
import java.util.HashSet;

public class CodeInquisitor {
	static private HashSet<Character> bfCharacters;
	static private HashSet<String> ookCharacters;
	
	public CodeInquisitor(){
		bfCharacters = new HashSet<Character>(Arrays.asList(new Character[]{',', '.', '<', '>', '+', '-', '[', ']'}));
		ookCharacters = new HashSet<String>(Arrays.asList(new String[]{"Ook.", "Ook?", "Ook!"}));
	}
	
	public boolean isCodeLegal(String code, String language){
		String standardCode = code.replaceAll(" ", "").replaceAll(System.lineSeparator(), "");
		switch(language){
		case "BF":
			for(char c : standardCode.toCharArray()){
				if(!bfCharacters.contains(c)){
					return false;
				}
			}
			break;
		case "OOK":
			for(int i=0; i+4<=standardCode.length(); i++){
				if(ookCharacters.contains(code.substring(i, i+4))){
					i+=3;
				}
				else{
					return false;
				}
			}
			break;
		}
		return true;
	}
}
