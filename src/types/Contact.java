package types;

import java.io.Serializable;

public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 888790484735681874L;
	int id;
	String nickname;
	
	public Contact(int id, String nickname){
		this.id = id;
		this.nickname = nickname;
	}
	
	public String getUsername(){
		return this.nickname;
	}
	
	public int getId(){
		return this.id;
	}
	
	@Override
	public String toString() {
		return "[" + id + ", " + this.getUsername() + "]";
	}
	
}
