//需要客户端的Stub
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface UserService extends Remote{
	public boolean register(String username, String password) throws RemoteException;
	
	public boolean login(String username, String password) throws RemoteException;

	public boolean logout(String username) throws RemoteException;
	
	public String readDirList(String userId)throws RemoteException;
	
	public ArrayList<String> readFileList(String userId, String dirName)throws RemoteException;

//	public ImageView getAvatar(String username) throws RemoteException;
	
//	public boolean setAvatar(String username, ImageView avatar) throws RemoteException;
}
