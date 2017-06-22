package data;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.image.ImageView;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7148369978762072720L;
	private String name;
	private String password;
	private ImageView avatar;
	private boolean isLoggedIn;
	ArrayList<String> fileNamesOfCodes;
	
	public User(String name, String password){
		this.name = name;
		this.password = password;
		fileNamesOfCodes = new ArrayList<String>();
		this.avatar = null;
		this.isLoggedIn = false;
	}
	
	public void setPassword(String newPassword){
		this.password = newPassword;
	}
	
	public void setAvatar(ImageView avatar){
		this.avatar = avatar;
	}
	
	public boolean isPasswordCorrect(String inputPassword){
		return password.equals(inputPassword);
	}
	
	public boolean addFileName(String fileName){
		return fileNamesOfCodes.add(fileName);
	}
	
	public ArrayList<String> getFileList(){
		return fileNamesOfCodes;
	}
	
	public ImageView getAvatar(){
		return this.avatar;
	}
	
	public String getPassword(){
		return password;
	}
	
	public boolean isLoggedIn(){
		return this.isLoggedIn;
	}
	
	public boolean logIn(){
		if(isLoggedIn==true){
			return false;
		}
		else{
			this.isLoggedIn = true;
			return true;
		}
	}
	
	public void logOut(){
		this.isLoggedIn = false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
