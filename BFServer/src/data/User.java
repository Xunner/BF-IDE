package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4057474556142926958L;
	private String name;
	private String password;
//	transient private ImageView avatar;
	HashMap<String, ArrayList<String>> dirNames;	//	文件夹名清单（也即项目名清单）（内包含历史版本名）
	
	public User(String name, String password){
		this.name = name;
		this.password = password;
		dirNames = new HashMap<String, ArrayList<String>>();
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
	
	public boolean newDir(String dirName){
		if(dirNames.containsKey(dirName)){
			return false;
		}
		else{
			dirNames.put(dirName, new ArrayList<String>());
			return true;
		}
	}
	
	public boolean addFileName(String dir, String fileName){
		ArrayList<String> files = dirNames.get(dir);
		if(files!=null){
			return files.add(fileName);
		}
		else{
			return false;
		}
	}
	
	public Set<String> getDirNameList(){
		return dirNames.keySet();
	}
	
	public ArrayList<String> getFileNameList(String dirName){
		return dirNames.get(dirName);
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
