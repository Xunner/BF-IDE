//需要客户端的Stub
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
public interface IOService extends Remote{
	public boolean newDir(String userId, String dirName)throws RemoteException;
	
	public boolean addFile(String file, String userId, String dirName, String fileName)throws RemoteException;
	
	public String readFile(String userId, String dirName, String fileName)throws RemoteException;
}
