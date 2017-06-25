//需要客户端的Stub
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javafx.scene.image.ImageView;

public interface UserService extends Remote{
	public boolean register(String username, String password) throws RemoteException;
	
	public boolean login(String username, String password) throws RemoteException;

	public boolean logout(String username) throws RemoteException;

	public ImageView getAvatar(String username) throws RemoteException;
	
	public boolean setAvatar(String username, ImageView avatar) throws RemoteException;
}
