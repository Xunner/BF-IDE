package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import service.ExecuteService;
import service.IOService;
import service.UserService;
import serviceImpl.ExecuteServiceImpl;
import serviceImpl.IOServiceImpl;
import serviceImpl.UserServiceImpl;

public class DataRemoteObject extends UnicastRemoteObject implements IOService, UserService, ExecuteService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4029039744279087114L;
	private IOService iOService;
	private UserService userService;
	private ExecuteService executeService;
	protected DataRemoteObject() throws RemoteException {
		iOService = new IOServiceImpl();
		userService = new UserServiceImpl();
		executeService = new ExecuteServiceImpl();
	}

	@Override
	public boolean register(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.register(username, password);
	}

	@Override
	public boolean login(String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.login(username, password);
	}

	@Override
	public boolean logout(String username) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.logout(username);
	}

	@Override
	public String BFExecute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		return executeService.BFExecute(code, param);
	}

	@Override
	public String OOKExecute(String code, String param) throws RemoteException {
		// TODO Auto-generated method stub
		return executeService.OOKExecute(code, param);
	}

	@Override
	public String readDirList(String userId) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.readDirList(userId);
	}

	@Override
	public ArrayList<String> readFileList(String userId, String dirName) throws RemoteException {
		// TODO Auto-generated method stub
		return userService.readFileList(userId, dirName);
	}

	@Override
	public boolean newDir(String userId, String dirName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.newDir(userId, dirName);
	}

	@Override
	public boolean addFile(String file, String userId, String dirName, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.addFile(file, userId, dirName, fileName);
	}

	@Override
	public String readFile(String userId, String dirName, String fileName) throws RemoteException {
		// TODO Auto-generated method stub
		return iOService.readFile(userId, dirName, fileName);
	}

//	@Override
//	public ImageView getAvatar(String username) throws RemoteException{
//		// TODO Auto-generated method stub
//		return userService.getAvatar(username);
//	}
//
//	@Override
//	public boolean setAvatar(String username, ImageView avatar) throws RemoteException {
//		// TODO Auto-generated method stub
//		return userService.setAvatar(username, avatar);
//	}

}
