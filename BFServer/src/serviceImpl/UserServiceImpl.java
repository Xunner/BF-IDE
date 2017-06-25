package serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;

import data.User;
import javafx.scene.image.ImageView;
import service.UserService;

public class UserServiceImpl implements UserService{
	private static ArrayList<User> list;	//	用户清单
	
	@SuppressWarnings("unchecked")
	public UserServiceImpl(){
		File file = new File("list.ser");
		if(file.exists()){
			try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
				Object o = ois.readObject();
				if(o instanceof ArrayList<?>){
					list = (ArrayList<User>) o;
					for(User u : list){
						System.out.println(u);
					}
				}
				else{
					System.out.println("读取list错误");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else{
			list = new ArrayList<User>();
			writeList();
		}
	}

	@Override
	public boolean login(String username, String password) throws RemoteException {
		boolean ret = false;
		User u = searchUser(username);
		if(u!=null && u.isPasswordCorrect(password)){
			ret = true;
		}
		writeList();
		return ret;
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		boolean ret = false;
		User u = searchUser(username);
		if(u!=null){
			ret = true;
		}
		writeList();
		return ret;
	}

	@Override
	public boolean register(String username, String password) throws RemoteException {
		boolean ret = false;
		User u = searchUser(username);
		if(u==null){
			list.add(new User(username, password));
			writeList();
			ret = true;
		}
		return ret;
	}

	public static void writeList(){
		File file = new File("list.ser");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false))){
			oos.writeObject(list);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		for(User u : list){
//			System.out.println("用户名："+u+"；密码："+u.getPassword());
//		}
//		System.out.println();
	}

	public static ArrayList<User> getUserList(){
		return list;
	}
	
	private User searchUser(String username){
		for(User u : list){
			if(u.toString().equals(username)){
				return u;
			}
		}
		return null;
	}
	
	@Override
	public ImageView getAvatar(String username) {
		User u = searchUser(username);
		if(u!=null){
			return u.getAvatar();
		}
		else{
			return null;
		}
	}

	@Override
	public boolean setAvatar(String username, ImageView avatar) throws RemoteException {
		User u = searchUser(username);
		if(u!=null){
			u.setAvatar(avatar);
			return true;
		}
		else{
			return false;
		}
	}
}
