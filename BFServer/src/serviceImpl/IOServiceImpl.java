package serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import data.User;
import service.IOService;

public class IOServiceImpl implements IOService{
	
	private String formatFileName(String userId, String fileName){
		return userId + "_" + fileName;
	}
	
	@Override
	public boolean writeFile(String file, String userId, String fileName) {
		//	存储代码
		File f = new File(formatFileName(userId, fileName));
		try (FileWriter fw = new FileWriter(f, false)){
			fw.write(file);
			fw.flush();
			
			//	更新目录
			ArrayList<User> list = UserServiceImpl.getUserList();	//	用户清单
			for(User u : list){
				if(u.toString().equals(userId)){
					u.addFileName(fileName);
					
					//	写入list
					UserServiceImpl.writeList();
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("错误：未找到用户");
		return false;
	}

	@Override
	public String readFile(String userId, String fileName) {
		File f = new File(formatFileName(userId, fileName));
		StringBuilder sb = new StringBuilder();
		try(BufferedReader reader = new BufferedReader(new FileReader(f))) {
			String line;
			while((line=reader.readLine()) != null){
				sb.append(line+System.lineSeparator());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public String readFileList(String userId) {
		ArrayList<User> list = UserServiceImpl.getUserList();	//	用户清单
		StringBuilder sb = new StringBuilder();
		for(User u : list){
			if(u.toString().equals(userId)){
				for(String line : u.getFileList()){
					sb.append(line+System.lineSeparator());
				}
				return sb.toString();
			}
		}
		System.out.println("读取list错误");
		return "ERROR";
	}
	
}
