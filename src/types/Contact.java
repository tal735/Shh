package types;

import java.io.Serializable;
import java.net.Socket;

public class Contact implements Serializable {

	int id;
	String nickname;
	transient  Socket socket;
	
	public Contact(int id, String nickname, Socket socket){
		this.id = id;
		this.nickname = nickname;
		this.socket = socket;
	}
	
	
	public String getUsername(){
		return this.nickname;
	}
	
	public int getId(){
		return this.id;
	}
	
	public Socket getSocket(){
		return this.socket;
	}
	public void setSocket(Socket socket){
		this.socket = socket;
	}

	
}
