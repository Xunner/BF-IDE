package data;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8401923436488146777L;
	private String name;
	private String password;
//	transient private ImageView avatar;
	ArrayList<String> fileNamesOfCodes;
	
	public User(String name, String password){
		this.name = name;
		this.password = password;
		fileNamesOfCodes = new ArrayList<String>();
//		this.avatar = null;
	}
	
	public void setPassword(String newPassword){
		this.password = newPassword;
	}
	
//	public void setAvatar(ImageView avatar){
//		this.avatar = avatar;
//		File file = new File(name+".png");
//		if(file.exists()){
//			file.delete();
//		}
//		try {
//			ImageIO.write(SwingFXUtils.fromFXImage(avatar.getImage(), null), "png", file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public boolean isPasswordCorrect(String inputPassword){
		return password.equals(inputPassword);
	}
	
	public boolean addFileName(String fileName){
		return fileNamesOfCodes.add(fileName);
	}
	
	public ArrayList<String> getFileList(){
		return fileNamesOfCodes;
	}
	
//	public ImageView getAvatar(){
//		File file = new File(name+".png");
//		if(file.exists()){
//			return new ImageView(new Image(file.toURI().toString()));
//		}
//		else{
//			return this.avatar;
//		}
//	}
	
	public String getPassword(){
		return password;
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
