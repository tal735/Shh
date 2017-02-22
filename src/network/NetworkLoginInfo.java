package network;

import java.io.Serializable;

public class NetworkLoginInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9189000921398985550L;
	private byte[] password;
	private String username;
	private boolean loginSuccess = false;
	
	//password should be hashed in this class
	public NetworkLoginInfo(String username, byte[] password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername(){
		return this.username;
	}
	
	public byte[] getHashedPassword(){
		return this.password;
	}

	public void setLoginSuccess(boolean success){
		this.loginSuccess = success;
	}
	
	public boolean isLoginSuccess(){
		return this.loginSuccess;
	}
	
}
